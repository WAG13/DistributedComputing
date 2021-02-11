package knu.dc.lab1.taskA;

import knu.dc.lab1.SliderChangerThread;

import javax.swing.*;

public class TaskA extends JFrame {
    private JPanel mainPanel;
    private JLabel thread1Priority;
    private JLabel thread2Priority;
    private JSlider slider1;
    private JButton startButton;
    private JButton decreaseThread1PriorityButton;
    private JButton increaseThread1PriorityButton;
    private JButton decreaseThread2PriorityButton;
    private JButton increaseThread2PriorityButton;

    private SliderChangerThread thread1;
    private SliderChangerThread thread2;

    public TaskA(){
        super("Lab1 Task A");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
        pack();

        thread1 = new SliderChangerThread(-1, slider1);
        thread2 = new SliderChangerThread(1, slider1);

        thread1Priority.setText(""+thread1.getPriority());
        thread2Priority.setText(""+thread2.getPriority());

        startButton.addActionListener(e ->  {
            startButton.setEnabled(false);
            thread1.start();
            thread2.start();
        });

        decreaseThread1PriorityButton.addActionListener(e -> {
            thread1.decPriority();
            thread1Priority.setText(""+thread1.getPriority());
        });

        increaseThread1PriorityButton.addActionListener(e -> {
            thread1.incPriority();
            thread1Priority.setText(""+thread1.getPriority());
        });

        decreaseThread2PriorityButton.addActionListener(e ->  {
            thread2.decPriority();
            thread2Priority.setText(""+thread2.getPriority());
        });

        increaseThread2PriorityButton.addActionListener(e ->  {
            thread2.incPriority();
            thread2Priority.setText(""+thread2.getPriority());
        });
    }

    public static void main(String[] args) {
        JFrame frame = new TaskA();
    }

}
