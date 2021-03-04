package knu.dc.taskB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Garden {
    private int[][] garden;
    int size;
    Random rand;

    Garden(int size) {
        this.size = size;
        rand = new Random();
        garden = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                garden[i][j] = rand.nextInt(3);
            }
        }
    }

    public void printConsole() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(garden[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printToFile() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("garden.txt"));

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    writer.append(String.valueOf(garden[i][j]));
                }
                writer.append(System.getProperty("line.separator"));
            }
            writer.close();
        } catch (IOException e) {


        }
    }

    public void nature() {
        int first = rand.nextInt(9);
        int second = rand.nextInt(9);
        if (garden[first][second] > 0) {
            garden[first][second]--;
        }
    }

    public void gardener() {
        int first = rand.nextInt(9);
        int second = rand.nextInt(9);
        if (garden[first][second] < 2) {
            garden[first][second]++;
        }
    }
}

