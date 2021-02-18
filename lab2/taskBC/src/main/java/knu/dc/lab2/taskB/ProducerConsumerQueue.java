package knu.dc.lab2.taskB;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private int maxSize;

    public ProducerConsumerQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void put(T elem) throws InterruptedException {
        while (queue.size() >= maxSize)
            wait();
        notifyAll();
        queue.add(elem);
    }

    public synchronized T get() throws InterruptedException {
        while (queue.size() == 0)
            wait();
        notifyAll();
        return queue.remove();
    }
}
