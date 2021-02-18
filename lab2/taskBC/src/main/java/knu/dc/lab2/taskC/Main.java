package knu.dc.lab2.taskC;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final Integer monksNumber = 55;
        Competition comp = new Competition(monksNumber);
        ForkJoinPool pool = new ForkJoinPool();
        Monk winner = pool.invoke(comp);
        System.out.print("Winner: "+winner.toString());
    }
}

