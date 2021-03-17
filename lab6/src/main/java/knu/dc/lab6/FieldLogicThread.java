package knu.dc.lab6;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldLogicThread extends Thread {

    private final Field field;
    private final ReentrantReadWriteLock lock;
    private final CyclicBarrier barrier;

    private final int fromX;
    private final int toX;
    private final int numberOfCivilizations;

    public FieldLogicThread(Field field, CyclicBarrier barrier, ReentrantReadWriteLock lock, int fromX, int toX, int numberOfCivilization) {
        this.field = field;
        this.barrier = barrier;
        this.lock = lock;
        this.fromX = fromX;
        this.toX = toX;
        this.numberOfCivilizations = numberOfCivilization;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            lock.writeLock().lock();
            field.simulate(numberOfCivilizations, fromX, toX);
            lock.writeLock().unlock();

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                //e.printStackTrace();
            }
        }
    }
}

