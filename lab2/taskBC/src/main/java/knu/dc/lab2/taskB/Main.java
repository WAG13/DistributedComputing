package knu.dc.lab2.taskB;

import knu.dc.lab2.taskB.handlers.IvanovHandler;
import knu.dc.lab2.taskB.handlers.NechiporchukHandler;
import knu.dc.lab2.taskB.handlers.PetrovHandler;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int stolenThingsNumber = 50;
        ProducerConsumerQueue<Integer> ivanovQueue = new ProducerConsumerQueue<>(10);
        ProducerConsumerQueue<Integer> necheporycQueue = new ProducerConsumerQueue<>(10);

        IvanovHandler ivanovHandler = new IvanovHandler(ivanovQueue, stolenThingsNumber);
        PetrovHandler petrovHandler = new PetrovHandler(ivanovQueue, necheporycQueue, stolenThingsNumber);
        NechiporchukHandler nechiporchukHandler = new NechiporchukHandler(necheporycQueue, stolenThingsNumber);

        Thread[] threads = new Thread[] {
                new Thread(ivanovHandler),
                new Thread(petrovHandler),
                new Thread(nechiporchukHandler)
        };

        for (int i = 0; i < 3; i++)
            threads[i].start();
        threads[2].join();
        System.out.println("Total sum = " + nechiporchukHandler.getSum()+"$");
    }
}