package knu.dc.algo;

import mpi.Cartcomm;
import mpi.MPI;

import java.util.Arrays;

// Метод Кенона
public class CannonMatrix {
    // Координати поточного потоку в декартовій решітці потоків
    private static int[] gridCoords = new int[2];

    // Комунікатор для рядків потоків
    private static Cartcomm ColComm;

    // Комунікатор для ствпчиків потоків
    private static Cartcomm RowComm;

    // Розподілення блоків між потоками
    private static void matrixScatter(int[] matrix, int[] matrixBlock, int matSize, int blockSize) {
        int[] matrixRow = new int[blockSize * matSize];
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
        int gridSize = (int) Math.sqrt(procNum);

        int matSize = matrixA.height;
        if (procNum != gridSize * gridSize) {
            if (procRank == 0)
                System.out.println("Cannon: " + matSize + " x " + matSize + ", 0 ms (procNum != gridSize * gridSize)");
            MPI.Finalize();
            return null;
        }


        Cartcomm gridComm;


        int blockSize = matSize / gridSize;

        // Виділення кожному з потоків місця для зберігання блоків з кожної матриці
        Matrix matrixC = new Matrix(matrixA.height, matrixB.width, "C");

        int[] ABlock = new int[blockSize * blockSize];
        int[] BBlock = new int[blockSize * blockSize];
        int[] CBlock = new int[blockSize * blockSize];

        long startTime = 0L;
        if (procRank == 0) {
            startTime = System.currentTimeMillis();
        }

        // Потреба у фіксації виміру решітки потоків
        boolean[] subdims = new boolean[2];


        gridComm = MPI.COMM_WORLD.Create_cart(new int[]{gridSize, gridSize}, new boolean[]{false, false}, true);


        gridCoords = gridComm.Coords(procRank);

        subdims[1] = true;
        RowComm = gridComm.Sub(subdims);


        subdims[0] = true;
        subdims[1] = false;
        ColComm = gridComm.Sub(subdims);

        // Розподілення задач для потоків декартової решітки
        matrixScatter(matrixA.matrix, ABlock, matSize, blockSize);
        matrixScatter(matrixB.matrix, BBlock, matSize, blockSize);


        if (gridCoords[0] != 0) {
            int nextProc = gridCoords[1] - gridCoords[0];
            if (nextProc < 0)
                nextProc += gridSize;
            RowComm.Sendrecv_replace(ABlock, 0, blockSize * blockSize, MPI.INT, nextProc, 0, MPI.ANY_SOURCE, 0);
        }

        if (gridCoords[1] != 0) {
            int nextProc = gridCoords[0] - gridCoords[1];
            if (nextProc < 0) nextProc += gridSize;
            ColComm.Sendrecv_replace(BBlock, 0, blockSize * blockSize, MPI.INT, nextProc, 1, MPI.ANY_SOURCE, 1);
        }


        MPI.COMM_WORLD.Barrier();


        for (int i = 0; i < blockSize; i++)
            for (int j = 0; j < blockSize; j++)
                for (int k = 0; k < blockSize; k++)
                    CBlock[i * blockSize + j] += ABlock[i * blockSize + k] * BBlock[k * blockSize + j];

        for (int iter = 0; iter < gridSize - 1; iter++) {
            int nextProc = gridCoords[1] - 1;
            if (nextProc < 0)
                nextProc += gridSize;
            RowComm.Sendrecv_replace(ABlock, 0, blockSize, MPI.INT, nextProc, 0, MPI.ANY_SOURCE, 0);

            nextProc = gridCoords[0] - 1;
            if (nextProc < 0)
                nextProc += gridSize;


            ColComm.Sendrecv_replace(BBlock, 0, blockSize, MPI.INT, nextProc, 1, MPI.ANY_SOURCE, 1);

            for (int i = 0; i < blockSize; i++)
                for (int j = 0; j < blockSize; j++)
                    for (int k = 0; k < blockSize; k++)
                        CBlock[i * blockSize + j] += ABlock[i * blockSize + k] * BBlock[k * blockSize + j];
        }


        int[] resultRow = new int[matSize * blockSize];
        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(CBlock, i * blockSize, CBlock.length);
            int[] subRowRes = new int[gridSize * blockSize];

            RowComm.Gather(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            System.arraycopy(subRowRes, 0, resultRow, i * matSize, gridSize * blockSize);
        }

        if (gridCoords[1] == 0)
            ColComm.Gather(resultRow, 0, blockSize * matSize, MPI.INT, matrixC.matrix, 0, blockSize * matSize, MPI.INT, 0);

        if (procRank == 0) {
            long endTime = System.currentTimeMillis();
            System.out.print("Cannon: " + matrixC.height + " x " + matrixC.width + ", ");
            System.out.println(endTime - startTime + " ms");
        }
        MPI.Finalize();

        return matrixC;
    }
}