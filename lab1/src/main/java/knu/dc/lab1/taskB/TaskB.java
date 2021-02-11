package knu.dc.lab1.taskB;

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
    public static AtomicInteger semaphore = new AtomicInteger(0);

    public TaskB(){
        super("Lab1 Task B");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
        pack();

        // Thread1
        startThread1Button.addActionListener( e -> {
            thread1 = new SemaphoreSliderChangerThread(-1, slider1, MIN_PRIORITY);
            thread1.start();
            stateLabel.setText("Thread 1 is running");
            stopThread1Button.setEnabled(true);
            stopThread2Button.setEnabled(false);

            if (thread1.isAlive()){
                stateLabel.setText("Thread 1 is still running");
            } else if (thread2.isAlive()){
                stateLabel.setText("Thread 2 is still running");
            }
        });

        stopThread1Button.addActionListener( e -> {
            if (thread1.isAlive()){
                thread1.interrupt();
                stateLabel.setText("");
                stopThread1Button.setEnabled(false);
            }
        });

        // Thread2
        startThread2Button.addActionListener( e -> {
            thread2 = new SemaphoreSliderChangerThread(1, slider1, MAX_PRIORITY);
            thread2.start();
            stateLabel.setText("Thread 2 is running");
            stopThread2Button.setEnabled(true);
            stopThread1Button.setEnabled(false);

            if (thread1.isAlive()){
                stateLabel.setText("Thread 1 is still running");
            } else if (thread2.isAlive()) {
                stateLabel.setText("Thread 2 is still running");
            }
        });

        stopThread2Button.addActionListener( e -> {
            if (thread2.isAlive()){
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
