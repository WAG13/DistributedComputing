package knu.dc.lab2.taskB.handlers;

import knu.dc.lab2.taskB.ProducerConsumerQueue;

public class PetrovHandler implements Runnable{
    private ProducerConsumerQueue<Integer> ivanovQueue;
    private ProducerConsumerQueue<Integer> nechiporchukQueue;
    private int elementsToHandle;

    public PetrovHandler(ProducerConsumerQueue<Integer> ivanovQueue, ProducerConsumerQueue<Integer> nechiporchukQueue, int elementsToHandle) {
        this.ivanovQueue = ivanovQueue;
        this.nechiporchukQueue = nechiporchukQueue;
        this.elementsToHandle = elementsToHandle;
    }

    @Override
    public void run() {
        for (int i = 0; i < elementsToHandle; i++ ) {
            try {
                Integer elem = ivanovQueue.get();
                System.out.println("Petrov\t\tloaded item with value\t" + elem + "$");
                nechiporchukQueue.put(elem);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}