package knu.dc.taskA;

public class Bee implements Runnable{
    Bear bear;
    Pot pot;
    int numberOfBee;
    volatile boolean isActive = false;

    Bee (int numberOfBee, Bear bear, Pot pot){
        this.numberOfBee = numberOfBee;
        this.bear = bear;
        this.pot = pot;
    }

    public void setActive(){
        isActive = true;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Bee #" + numberOfBee + " brought honey");
            pot.addHoney();
            if (pot.isFull()) {
                System.out.println("Pot is FULL");
                bear.wakeUp();
            }
            isActive = false;
        }
    }
}