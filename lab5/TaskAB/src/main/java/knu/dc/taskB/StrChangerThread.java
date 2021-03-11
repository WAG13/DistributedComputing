package knu.dc.taskB;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class StrChangerThread extends Thread {
    private String str;
    private final CyclicBarrier cyclicBarrier;
    private final Controller controller;
    private boolean isRun = true;
    private int abCount = 0;

    StrChangerThread(String str, CyclicBarrier cyclicBarrier, Controller controller){
        this.str = str;
        this.cyclicBarrier = cyclicBarrier;
        this.controller = controller;
        for(int i =0 ; i< str.length(); i++){
            if(str.charAt(i) == 'A' || str.charAt(i) == 'B'){
                abCount++;
            }
        }
    }

    @Override
    public void run(){

        while(isRun) {
            int randIndex = (int)(Math.random() * str.length());
            switch (str.charAt(randIndex)) {
                case 'A' -> {
                    str = str.substring(0, randIndex) + 'C' + str.substring(randIndex + 1);
                    abCount--;
                }
                case 'B' -> {
                    str = str.substring(0, randIndex) + 'D' + str.substring(randIndex + 1);
                    abCount--;
                }
                case 'C' -> {
                    str = str.substring(0, randIndex) + 'A' + str.substring(randIndex + 1);
                    abCount++;
                }
                case 'D' -> {
                    str = str.substring(0, randIndex) + 'B' + str.substring(randIndex + 1);
                    abCount++;
                }
            }
            controller.getData(abCount);
            System.out.println(Thread.currentThread().getId() + " " + str + " " + abCount);
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println();

            isRun = controller.getIsRun();
        }
    }
}
