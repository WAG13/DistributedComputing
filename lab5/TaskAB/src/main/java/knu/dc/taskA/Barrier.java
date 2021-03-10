package knu.dc.taskA;

public class Barrier {
    public int arrived = 0;
    public int threadCount = 0;
    public int unarrived = 0;

    public synchronized void register() {
        threadCount++;
        unarrived++;
    }

    public synchronized void deregister() {
        if (arrived > 0) {
            return;
        }
        threadCount--;
        unarrived++;

    }

    public synchronized void arriveAndWait() {
        arrived++;
        unarrived--;
        if (arrived == threadCount) {
            notifyAll();
        }
        while (arrived != threadCount) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("in barrier " + Thread.currentThread().getId());
        unarrived++;
        if (unarrived == threadCount) {
            System.out.println("Clear");
            arrived = 0;
        }

    }
}