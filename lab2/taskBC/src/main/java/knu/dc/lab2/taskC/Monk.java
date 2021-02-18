package knu.dc.lab2.taskC;

import java.util.Random;

public class Monk implements Comparable{
    private Integer energy;
    private String monastery;

    Monk() {
        Random r = new Random();
        energy = r.nextInt(1000);
        monastery = (r.nextInt(2) == 0) ? "Huan-un" : "Huan-in";
    }

    Integer getEnergy() {
        return energy;
    }
    String getMonastery(){
        return monastery;
    }

    public String toString(){
        return "Monk from "+monastery+" with energy "+energy;
    }

    @Override
    public int compareTo(Object o) {
        Monk other = (Monk)o;
        if(this.energy > other.energy) {
            return 1;
        } else if (this.energy < other.energy){
            return -1;
        } else {
            return 0;
        }
    }
    public static Monk max(Object first, Object second) {
        return Monk.max((Monk)first, (Monk)second);
    }

    static Monk max(Monk first, Monk second){
        if(first.energy > second.energy){
            return first;
        } else {
            return second;
        }
    }
}
