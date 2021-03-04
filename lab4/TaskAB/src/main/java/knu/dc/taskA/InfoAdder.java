package knu.dc.taskA;

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
            phoneBook.addInfo(name, phone);
            System.out.println("Add info: " + name + " " + phone);
            rwLock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
