package knu.dc.taskA;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Pot pot = new Pot(10);
        Bear bear = new Bear(pot);
        int numOfBees = 5;

        List<Bee> bees = new ArrayList<>(numOfBees);
        for(int i =0 ; i < numOfBees; i++){
            bees.add(new Bee(i, bear, pot));
        }
        bees.get(0).setActive();
        Thread threadBear = new Thread(bear);
        threadBear.start();

        for(int i =0 ; i < numOfBees; i++){
            Bee bee = bees.get(i);
            Thread threadBee = new Thread(bee);
            threadBee.start();
        }
    }
}