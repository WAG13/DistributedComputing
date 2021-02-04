package knu.dc.lab1.taskA;

import javax.swing.*;
public class SliderChangerThread extends Thread {

    private int changer;
    private JSlider slider;

    public SliderChangerThread(int changer, JSlider slider) {
        this.changer = changer;
        this.slider = slider;
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
        while (!Thread.currentThread().isInterrupted()) {
            moveSlider();
        }
    }

    private void moveSlider()  {
        synchronized (slider) {
            slider.setValue(slider.getValue() + changer);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
