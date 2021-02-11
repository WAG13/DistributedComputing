package knu.dc.lab1;

import javax.swing.*;
public class SliderChangerThread extends Thread {

    private int changer;
    private JSlider slider;

    public SliderChangerThread(int changer, JSlider slider) {
        this.changer = changer;
        this.slider = slider;
        setPriority(Thread.NORM_PRIORITY);
        setDaemon(true);
    }

    public SliderChangerThread(int changer, JSlider slider, int priority) {
        this.changer = changer;
        this.slider = slider;
        setPriority(priority);
        setDaemon(true);
    }

    public void incPriority() {
        if (getPriority() < Thread.MAX_PRIORITY)
            setPriority(getPriority() + 1);
    }

    public void decPriority() {
        if (getPriority() > Thread.MIN_PRIORITY)
            setPriority(getPriority() - 1);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) moveSlider();
    }

    protected void moveSlider()  {
        synchronized (slider) {
            slider.setValue(slider.getValue() + changer);
            try {
                sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
