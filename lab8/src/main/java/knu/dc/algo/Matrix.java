package knu.dc.algo;

import java.util.Random;

public class Matrix {
    int[] matrix;
    int height;
    int width;
    String name;

    public Matrix(int size, String name) {
        this.height = size;
        this.width = size;
        this.matrix = new int[width * height];
        this.name = name;
    }

    public Matrix(int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.matrix = new int[width * height];
        this.name = name;
    }

    public void print(){
        System.out.println("Matrix "+name);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(matrix[i*width+j]+" ");
            }
            System.out.println();
        }
    }
    public void fillRandom(int maxNumber) {
        Random rand = new Random();
        for (int i = 0; i < height * width; i++)
            this.matrix[i] = rand.nextInt(maxNumber);
    }
}