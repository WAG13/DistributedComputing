package knu.dc.taskB;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainB {
    public static void main(String[] args) throws InterruptedException {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Garden garden = new Garden(10);
        NatureThread natureThread = new NatureThread(readWriteLock, garden);
        GardenerThread gardenerThread = new GardenerThread(readWriteLock, garden);
        PrinterThread print1 = new PrinterThread(readWriteLock, garden, true);
        PrinterThread print2 = new PrinterThread(readWriteLock, garden, false);

        natureThread.start();
        gardenerThread.start();
        print1.start();
        print2.start();
    }
}
