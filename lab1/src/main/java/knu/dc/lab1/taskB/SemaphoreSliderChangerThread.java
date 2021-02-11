package knu.dc.lab1.taskB;

import knu.dc.lab1.SliderChangerThread;

import javax.swing.*;

public class SemaphoreSliderChangerThread extends SliderChangerThread {

    public SemaphoreSliderChangerThread(int changer, JSlider slider, int priority) {
        super(changer,slider, priority);
    }

    @Override
    public void run() {
        if (TaskB.semaphore.compareAndSet(0,1)){
        while (!Thread.interrupted()) moveSlider();
            TaskB.semaphore.set(0);
        }
    }

}
