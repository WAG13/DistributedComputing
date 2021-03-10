package knu.dc.taskB;
import java.util.Arrays;

public class Controller {
    private boolean isRun = true;
    private int threadCounter = 0;
    private final int threadNum;
    private int[] allData;
    private boolean isAllThreadsArrived = false;

    Controller(int threadNum) {

        this.threadNum = threadNum;
        allData = new int[threadNum];
    }

    public boolean getIsRun() {
        return isRun;
    }

    public void isEqual() {
        boolean isEqual = true;
        Arrays.sort(allData);
        for (int i = 1; i < allData.length - 2; i++) {
            if (allData[i] != allData[i + 1]) {
                isEqual = false;
                break;
            }
        }
        if (isEqual) {
            if(allData[0] == allData[1] || allData[allData.length - 1] == allData[1]) {
                isRun = false;
                System.out.println("Are equal");
            }
        }
    }

    public synchronized void getData(int data) {
        allData[threadCounter] = data;
        threadCounter++;
        if (threadCounter == threadNum) {
            notifyAll();
            isAllThreadsArrived = true;
        }
        while (!isAllThreadsArrived) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadCounter--;
        if (threadCounter == 0) {
            isEqual();
            isAllThreadsArrived = false;
        }
    }
}