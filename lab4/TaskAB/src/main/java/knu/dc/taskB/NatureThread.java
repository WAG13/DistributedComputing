package knu.dc.taskB;

import java.util.concurrent.locks.ReadWriteLock;

public class NatureThread extends Thread {
    ReadWriteLock rwLock;
    Garden garden;

    NatureThread(ReadWriteLock rwLock, Garden garden) {
        this.rwLock = rwLock;
        this.garden = garden;
    }

    @Override
    public void run() {
        while (true) {
            rwLock.writeLock().lock();
            garden.nature();
            rwLock.writeLock().unlock();
        }
    }
}

