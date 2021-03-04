package knu.dc.taskA;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhoneBook {
    private BufferedWriter writer;
    private List<Pair<String, String>> phonebook = new ArrayList<>();

    PhoneBook() {
        try {
            writer = new BufferedWriter(new FileWriter("phonebook.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInfo(String name, String phone) {
        phonebook.add(new Pair(name, phone));
        try {
            writer.append(name + " " + phone + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        if (id >= phonebook.size()) {
            System.out.println("Cant delete. Doesnt exists");
            return;
        }
        phonebook.remove(id);
        try {
            writer.write("");
            for (Pair<String, String> item : phonebook) {

                writer.append(item.getKey() + " " + item.getValue() + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findByName(String name) {
        for (Pair<String, String> item : phonebook) {
            if (item.getKey().equals(name)) {
                System.out.println("found" + item.getKey() + ". Phone: " + item.getValue());
                return;
            }
        }
        System.out.println("not found");
    }

    public void findByPhone(String phone) {
        for (Pair<String, String> item : phonebook) {
            if (item.getValue().equals(phone)) {
                System.out.println("found" + item.getKey() + ". Phone: " + item.getValue());
                return;
            }
        }
        System.out.println("not found");
    }

    public void closeWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

