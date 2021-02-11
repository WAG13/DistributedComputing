package knu.dc.lab1.taskB;

import knu.dc.lab1.SliderChangerThread;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.*;

public class TaskB extends JFrame {
    private JPanel mainPanel;
    private JLabel stateLabel;
    private JSlider slider1;
    private JButton startThread1Button;
    private JButton stopThread1Button;
    private JButton startThread2Button;
    private JButton stopThread2Button;

    private Thread thread1;
    private Thread thread2;
    private AtomicInteger semaphore = new AtomicInteger(0);

    public TaskB(){
        super("Lab1 Task B");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
        pack();

        // Thread1
        startThread1Button.addActionListener( e -> {
            if (semaphore.compareAndSet(0,1)){
                thread1 = new SliderChangerThread(-1, slider1, MIN_PRIORITY);
                thread1.start();
                stateLabel.setText("Thread 1 is running");
                stopThread1Button.setEnabled(true);
                stopThread2Button.setEnabled(false);
            } else if (semaphore.get() == 1){
                stateLabel.setText("Thread 1 is still running");
            } else {
                stateLabel.setText("Thread 2 is still running");
            }
        });

        stopThread1Button.addActionListener( e -> {
            if (semaphore.compareAndSet(1,0)){
                thread1.interrupt();
                stateLabel.setText("");
                stopThread1Button.setEnabled(false);
            }
        });

        // Thread2
        startThread2Button.addActionListener( e -> {
            if (semaphore.compareAndSet(0,2)){
                thread2 = new SliderChangerThread(1, slider1, MAX_PRIORITY);
                thread2.start();
                stateLabel.setText("Thread 2 is running");
                stopThread2Button.setEnabled(true);
                stopThread1Button.setEnabled(false);
            } else if (semaphore.get() == 1){
                stateLabel.setText("Thread 1 is still running");
            } else {
                stateLabel.setText("Thread 2 is still running");
            }
        });

        stopThread2Button.addActionListener( e -> {
            if (semaphore.compareAndSet(2,0)){
                thread2.interrupt();
                stateLabel.setText("");
                stopThread2Button.setEnabled(false);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new TaskB();
    }

}
