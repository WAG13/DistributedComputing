package knu.dc.taskB;
import java.util.concurrent.*;

public class SleepingBarber extends Thread {

    public static Semaphore customers = new Semaphore(0);
    public static Semaphore barber = new Semaphore(0);
    public static Semaphore accessSeats = new Semaphore(1);

    public static final int CHAIRS = 5;
    public static int numberOfFreeSeats = CHAIRS;

    static class Customer extends Thread {

        int iD;
        boolean notCut = true;

        public Customer(int i) {
            iD = i;
        }

        public void run() {
            try {
                accessSeats.acquire();
                if (numberOfFreeSeats > 0) {
                    numberOfFreeSeats--;
                    System.out.println("Customer " + this.iD + " just sat down.(" + numberOfFreeSeats + " free seats)");
                    customers.release();
                    accessSeats.release();
                    try {
                        barber.acquire();
                        notCut = false;
                        this.get_haircut();
                    } catch (InterruptedException ignored) {}
                }
                else  {
                    System.out.println("There are no free seats for customer " + this.iD);
                    accessSeats.release();
                    notCut=false;
                }
            }
            catch (InterruptedException ignored) {}
        }

        public void get_haircut(){
            System.out.println("Customer " + this.iD + " is getting hair cut");
            try {
                sleep(5050);
            } catch (InterruptedException ignored) {}
        }

    }

    static class Barber extends Thread {

        public Barber() {}

        public void run() {
            while(true) {
                try {
                    customers.acquire();
                    accessSeats.acquire();
                    numberOfFreeSeats++;
                    barber.release();
                    accessSeats.release();
                    this.cutHair();
                } catch (InterruptedException ignored) {}
            }
        }

        public void cutHair(){
            System.out.println("Barber is cutting hair");
            try {
                sleep(5000);
            } catch (InterruptedException ignored){ }
        }
    }

    public static void main(String[] args) {

        SleepingBarber barberShop = new SleepingBarber();
        barberShop.start();
    }

    public void run(){
        Barber barber = new Barber();
        barber.start();

        for (int i=0; i<10; i++) {
            Customer newCustomer = new Customer(i);
            newCustomer.start();
            try {
                sleep(2000);
            } catch(InterruptedException ignored) {};
        }
    }
}