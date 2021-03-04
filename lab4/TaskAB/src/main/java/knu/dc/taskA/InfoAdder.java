package knu.dc.taskA;

import knu.dc.taskA.PhoneBook;
import knu.dc.taskA.ReadWriteLock;

public class InfoAdder extends Thread {
    PhoneBook phoneBook;
    ReadWriteLock rwLock;
    String name;
    String phone;

    InfoAdder(PhoneBook phoneBook, ReadWriteLock rwLock, String name, String phone) {
        this.phoneBook = phoneBook;
        this.rwLock = rwLock;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public void run() {
        try {
            rwLock.lockWrite();
            System.out.println("Add info" + name + " " + phone);
            phoneBook.addInfo(name, phone);
            rwLock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
