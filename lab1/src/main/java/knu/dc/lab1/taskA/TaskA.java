package knu.dc.lab1.taskA;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskA extends JFrame {
    private JPanel mainPanel;
    private JButton startButton;
    private JSlider slider1;
    private JButton decreaseThread1PriorityButton;
    private JButton increaseThread1PriorityButton;
    private JButton decreaseThread2PriorityButton;
    private JButton increaseThread2PriorityButton;
    private JLabel thread1Priority;
    private JLabel thread2Priority;

    private static SliderChangerThread thread1;
    private static SliderChangerThread thread2;

    public TaskA(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        thread1 = new SliderChangerThread(1, slider1);
        thread2 = new SliderChangerThread(-1, slider1);
        thread1.setDaemon(true);
        thread2.setDaemon(true);

        thread1Priority.setText(""+thread1.getPriority());
        thread2Priority.setText(""+thread2.getPriority());

        // Start Button Click
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                thread1.start();
                thread2.start();
            }
        });

        // change Thread1 Priority
        decreaseThread1PriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread1.decPriority();
                thread1Priority.setText(""+thread1.getPriority());
            }
        });

        increaseThread1PriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread1.incPriority();
                thread1Priority.setText(""+thread1.getPriority());
            }
        });

        // change Thread2 Priority
        decreaseThread2PriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread2.decPriority();
                thread2Priority.setText(""+thread2.getPriority());
            }
        });

        increaseThread2PriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread2.incPriority();
                thread2Priority.setText(""+thread2.getPriority());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new TaskA("Lab1 Task A");
        frame.setVisible(true);
    }



}
