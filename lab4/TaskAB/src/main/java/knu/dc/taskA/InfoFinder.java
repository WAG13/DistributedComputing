package knu.dc.taskA;

import knu.dc.taskA.PhoneBook;
import knu.dc.taskA.ReadWriteLock;

public class InfoFinder extends Thread {
    PhoneBook phoneBook;
    ReadWriteLock rwLock;
    boolean findByName;
    String toFind;

    InfoFinder(PhoneBook phoneBook, ReadWriteLock rwLock, boolean findByName, String toFind) {
        this.phoneBook = phoneBook;
        this.rwLock = rwLock;
        this.toFind = toFind;
        this.findByName = findByName;
    }

    @Override
    public void run() {
        try {
            rwLock.lockRead();
            if (findByName) {
                phoneBook.findByName(toFind);
            } else {
                phoneBook.findByPhone(toFind);
            }
            rwLock.unlockRead();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
