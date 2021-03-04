package knu.dc.taskA;

public class InfoDeleter extends Thread {
    PhoneBook phoneBook;
    ReadWriteLock rwLock;
    int deleteId;

    InfoDeleter(PhoneBook phoneBook, ReadWriteLock rwLock, int deleteId) {
        this.phoneBook = phoneBook;
        this.rwLock = rwLock;
        this.deleteId = deleteId;
    }

    @Override
    public void run() {
        try {
            rwLock.lockWrite();
            System.out.println("Delete info");
            phoneBook.delete(deleteId);
            rwLock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
