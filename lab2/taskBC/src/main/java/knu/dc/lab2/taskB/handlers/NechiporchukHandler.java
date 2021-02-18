package knu.dc.lab2.taskB.handlers;

import knu.dc.lab2.taskB.ProducerConsumerQueue;

public class NechiporchukHandler implements Runnable{
    private ProducerConsumerQueue<Integer> queue;
    private int elementsToHandle, sum;

    public int getSum() {
        return sum;
    }

    public NechiporchukHandler(ProducerConsumerQueue<Integer> queue, int elementsToHandle) {
        this.queue = queue;
        this.elementsToHandle = elementsToHandle;
    }

    @Override
    public void run() {
        for (int i = 0; i < elementsToHandle; i++) {
            try {
                Integer elem = queue.get();
                sum += elem;
                System.out.println("Nechiporchuk got item with value\t" + elem + "$");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}