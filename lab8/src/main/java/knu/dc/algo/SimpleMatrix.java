package knu.dc.algo;

import mpi.MPI;

/** Послідовний алгоритм */
public class SimpleMatrix {
    public static Matrix calculate(String[] args, Matrix matrixA, Matrix matrixB) {

        MPI.Init(args);

        int procRank = MPI.COMM_WORLD.Rank();

        Matrix matrixC = new Matrix(matrixA.height, matrixB.width, "C");
        long startTime = 0L;

        if (procRank == 0) {
            startTime = System.currentTimeMillis();


        for (int i = 0; i < matrixA.width; i++)
            for (int j = 0; j < matrixB.height; j++)
                for (int k = 0; k < matrixA.height; k++)
                    matrixC.matrix[i * matrixA.width + j] += matrixA.matrix[i * matrixA.width + k] * matrixB.matrix[k * matrixB.width + j];


            long endTime = System.currentTimeMillis();
            System.out.print("simple: " + matrixC.height + " x " + matrixC.width + ", ");
            System.out.println(endTime - startTime + " ms");
        }
        MPI.Finalize();

        return matrixC;
    }
}