package knu.dc.taskB;

import java.util.concurrent.locks.ReadWriteLock;

public class GardenerThread extends Thread {
    ReadWriteLock rwLock;
    Garden garden;

    GardenerThread(ReadWriteLock rwLock, Garden garden) {
        this.rwLock = rwLock;
        this.garden = garden;
    }

    @Override
    public void run() {
        while (true) {
            rwLock.writeLock().lock();
            garden.gardener();
            rwLock.writeLock().unlock();
        }
    }
}