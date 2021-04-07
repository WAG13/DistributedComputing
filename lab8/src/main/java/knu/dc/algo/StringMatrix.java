package knu.dc.algo;

import mpi.MPI;

/** Стрічкова схема */
public class StringMatrix {
    public static Matrix calculate(String[] args, Matrix matrixA, Matrix matrixB) {

        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();

        // Кількість потоків
        int procNum = MPI.COMM_WORLD.Size();

        Matrix matrixC = new Matrix(matrixA.height, matrixB.width, "C");
        long startTime = 0L;

        int matSize = matrixC.height;
        int lineHeight = matSize / procNum;


        int[] bufferA = new int[lineHeight * matSize];
        int[] bufferB = new int[lineHeight * matSize];
        int[] bufferC = new int[lineHeight * matSize];

        if (procRank == 0) {
            startTime = System.currentTimeMillis();
        }

        MPI.COMM_WORLD.Scatter(matrixA.matrix, 0, lineHeight * matSize, MPI.INT, bufferA, 0, lineHeight * matSize, MPI.INT, 0);
        MPI.COMM_WORLD.Scatter(matrixB.matrix, 0, lineHeight * matSize, MPI.INT, bufferB, 0, lineHeight * matSize, MPI.INT, 0);

        int nextProc = (procRank + 1) % procNum;
        int prevProc = procRank - 1;
        if (prevProc < 0)
            prevProc = procNum - 1;
        int prevDataNum = procRank;


        for (int p = 0; p < procNum; p++) {
            for (int i = 0; i < lineHeight; i++)
                for (int j = 0; j < matSize; j++)
                    for (int k = 0; k < lineHeight; k++)

                        bufferC[i * matSize + j] += bufferA[prevDataNum * lineHeight + i * matSize + k] * bufferB[k * matSize + j];
            prevDataNum -= 1;
            if (prevDataNum < 0)
                prevDataNum = procNum - 1;


            MPI.COMM_WORLD.Sendrecv_replace(bufferB, 0, lineHeight * matSize, MPI.INT, nextProc, 0, prevProc, 0);
        }


        MPI.COMM_WORLD.Gather(bufferC, 0, lineHeight * matSize, MPI.INT, matrixC.matrix, 0, lineHeight * matSize, MPI.INT, 0);

        if (procRank == 0) {
            long endTime = System.currentTimeMillis();
            System.out.print("string: " + matrixC.height + " x " + matrixC.width + ", ");
            System.out.println(endTime - startTime + " ms");
        }
        MPI.Finalize();

        return matrixC;
    }
}