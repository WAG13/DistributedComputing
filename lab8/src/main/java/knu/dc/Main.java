package knu.dc;

import knu.dc.algo.*;

public class Main {
    public static void main(String[] args) {
        int[] sizes = {100, 1000};

        for (int matSize : sizes) {

            Matrix matrixA = new Matrix(matSize, "A");
            Matrix matrixB = new Matrix(matSize, "B");

            matrixB.fillRandom(5);
            matrixA.fillRandom(5);

            SimpleMatrix.calculate(args, matrixA, matrixB);
            StringMatrix.calculate(args, matrixA, matrixB);
            FoxMatrix.calculate(args, matrixA, matrixB);
            CannonMatrix.calculate(args, matrixA, matrixB);
        }
    }
}