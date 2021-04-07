package knu.dc.algo;

import mpi.Cartcomm;
import mpi.MPI;

import java.util.Arrays;

// Алгоритм Фокса
public class FoxMatrix {
    // Координати поточного потоку в декартовій решітці потоків
    private static int[] gridCoords = new int[2];

    // Комунікатор для рядків потоків
    private static Cartcomm ColComm;

    // Комунікатор для стовпчиків потоків
    private static Cartcomm RowComm;

    // Розподілення блоків між потоками
    private static void matrixScatter(int[] matrix, int[] matrixBlock, int matSize, int blockSize) {
        // Допоміжний масив, розрахований на кількість елементів у одному рядку блоків (потоків)
        int[] matrixRow = new int[blockSize * matSize];

        /*
        Спочатку матриця розділяється на горизонтальні смуги. Ці смуги розподіляються на процеси, що становлять
        нульовий стовпець решітки потоків. Далі кожна смуга розділяється на блоки між потоками, які складають рядки
        решітки потоків.
        */
        if (gridCoords[1] == 0)
            ColComm.Scatter(matrix, 0, blockSize * matSize, MPI.INT, matrixRow, 0, blockSize * matSize, MPI.INT, 0);

        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(matrixRow, i * matSize, matrixRow.length);
            int[] subRowRes = new int[blockSize];

            RowComm.Scatter(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            System.arraycopy(subRowRes, 0, matrixBlock, i * blockSize, blockSize);
        }
    }

    public static Matrix calculate(String[] args, Matrix matrixA, Matrix matrixB) {
        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();
        int procNum = MPI.COMM_WORLD.Size();

        // Кількість блоків по горизонталі та вертикалі
        int gridSize = (int) Math.sqrt(procNum);

        int matSize = matrixA.height;
        if (procNum != gridSize * gridSize) {
            if (procRank == 0)
                System.out.println("Fox: " + matSize + " x " + matSize + ", 0 ms (procNum != gridSize * gridSize)");
            MPI.Finalize();
            return null;
        }

        // Комунікатор для декартової решітки потоків
        Cartcomm gridComm;
        // Розмір блоку
        int blockSize = matSize / gridSize;

        Matrix matrixC = new Matrix(matrixA.height, matrixB.width, "C");

        // Виділення кожному з потоків місця для зберігання блоків з кожної матриці та додаткового блоку матриці А
        int[] ABlock = new int[blockSize * blockSize];
        int[] BBlock = new int[blockSize * blockSize];
        int[] CBlock = new int[blockSize * blockSize];
        int[] tempABlock = new int[blockSize * blockSize];

        long startTime = 0L;
        if (procRank == 0) {
            matrixA.fillRandom(5);
            matrixB.fillRandom(5);
            startTime = System.currentTimeMillis();
        }

        boolean[] subdims = new boolean[2];


        gridComm = MPI.COMM_WORLD.Create_cart(new int[]{gridSize, gridSize}, new boolean[]{false, false}, true);


        gridCoords = gridComm.Coords(procRank);

        subdims[1] = true;
        RowComm = gridComm.Sub(subdims);


        subdims[0] = true;
        subdims[1] = false;
        ColComm = gridComm.Sub(subdims);


        matrixScatter(matrixA.matrix, tempABlock, matSize, blockSize);
        matrixScatter(matrixB.matrix, BBlock, matSize, blockSize);


        for (int iter = 0; iter < gridSize; iter++) {

            int pivot = (gridCoords[0] + iter) % gridSize;

            if (gridCoords[1] == pivot)
                if (blockSize * blockSize >= 0)
                    System.arraycopy(tempABlock, 0, ABlock, 0, blockSize * blockSize);


            RowComm.Bcast(ABlock, 0, blockSize * blockSize, MPI.INT, pivot);


            for (int i = 0; i < blockSize; i++)
                for (int j = 0; j < blockSize; j++)
                    for (int k = 0; k < blockSize; k++)
                        CBlock[i * blockSize + j] += ABlock[i * blockSize + k] * BBlock[k * blockSize + j];


            int nextProc = gridCoords[0] + 1;
            if (gridCoords[0] == gridSize - 1)
                nextProc = 0;
            int prevProc = gridCoords[0] - 1;
            if (gridCoords[0] == 0)
                prevProc = gridSize - 1;


            ColComm.Sendrecv_replace(BBlock, 0, blockSize * blockSize, MPI.INT, nextProc, 0, prevProc, 0);
        }

        int[] resultRow = new int[matSize * blockSize];
        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(CBlock, i * blockSize, CBlock.length);
            int[] subRowRes = new int[gridSize * blockSize];


            RowComm.Gather(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            if (gridSize * blockSize >= 0)
                System.arraycopy(subRowRes, 0, resultRow, i * matSize, gridSize * blockSize);
        }

        if (gridCoords[1] == 0)
            ColComm.Gather(resultRow, 0, blockSize * matSize, MPI.INT, matrixC.matrix, 0, blockSize * matSize, MPI.INT, 0);

        if (procRank == 0) {
            long endTime = System.currentTimeMillis();
            System.out.print("Fox: " + matrixC.height + " x " + matrixC.width + ", ");
            System.out.println(endTime - startTime + " ms");
        }
        MPI.Finalize();

        return matrixC;
    }
}