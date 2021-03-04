package knu.dc.taskB;

import java.util.concurrent.locks.ReadWriteLock;

public class PrinterThread extends Thread {
    ReadWriteLock rwLock;
    Garden garden;
    boolean isConsole;

    PrinterThread(ReadWriteLock rwLock, Garden garden, boolean isConsole) {
        this.rwLock = rwLock;
        this.garden = garden;
        this.isConsole = isConsole;
    }

    @Override
    public void run() {
        while (true) {
            rwLock.readLock().lock();
            if (isConsole) {
                garden.printConsole();
            } else {
                garden.printToFile();
            }
            rwLock.readLock().unlock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

