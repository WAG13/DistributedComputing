package knu.dc.taskB;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        int numThreads = 4;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads);
        Controller controller = new Controller(numThreads);
        StrChangerThread strChangerThread1 = new StrChangerThread("ABCDABCD", cyclicBarrier, controller);
        StrChangerThread strChangerThread2 = new StrChangerThread("AAACAACC", cyclicBarrier, controller);
        StrChangerThread strChangerThread3 = new StrChangerThread("ACDCACDC", cyclicBarrier, controller);
        StrChangerThread strChangerThread4 = new StrChangerThread("CDABCDAB", cyclicBarrier, controller);
        strChangerThread1.start();
        strChangerThread2.start();
        strChangerThread3.start();
        strChangerThread4.start();
    }
}
