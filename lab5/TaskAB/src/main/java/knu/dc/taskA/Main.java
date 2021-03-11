package knu.dc.taskA;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        int size = 200;
        int threadCap = 50;
        int numberThread = size / threadCap;

        List<RegulateThread> regulateThreadList = new ArrayList<>();
        Barrier barrier = new Barrier();
        for(int i = 0; i < numberThread; i++){
            int[] rank = new int[threadCap];
            for(int j = 0; j < threadCap; j++){
                rank[j] = (int)(Math.random() * 10) % 2;
                System.out.print(rank[j]);
            }
            System.out.println();
            regulateThreadList.add(new RegulateThread(rank, barrier, numberThread));
        }
        for(int i = 0; i < numberThread; i++){
            if(i > 0){
                regulateThreadList.get(i).setLeft(regulateThreadList.get(i - 1));
            }
            else{
                regulateThreadList.get(i).setLeft(null);
            }

            if(i < numberThread - 1){
                regulateThreadList.get(i).setRight(regulateThreadList.get(i + 1));
            }
            else{
                regulateThreadList.get(i).setRight(null);
            }
            barrier.register();
        }


        for(int i = 0; i < numberThread; i++){
            regulateThreadList.get(i).start();
        }

    }
}

