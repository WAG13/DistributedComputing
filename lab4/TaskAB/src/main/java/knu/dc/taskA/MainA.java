package knu.dc.taskA;

public class MainA {
    public static void main(String[] args) throws InterruptedException {
        ReadWriteLock rwLock = new ReadWriteLock();
        PhoneBook phoneBook = new PhoneBook();

        InfoAdder infoAdder1 = new InfoAdder(phoneBook, rwLock, "first", "8734953");
        InfoAdder infoAdder2 = new InfoAdder(phoneBook, rwLock, "second", "4366");
        InfoAdder infoAdder3 = new InfoAdder(phoneBook, rwLock, "third", "43624");
        InfoAdder infoAdder4 = new InfoAdder(phoneBook, rwLock, "forth", "2346");

        InfoDeleter infoDeleter1 = new InfoDeleter(phoneBook, rwLock, 2);
        InfoDeleter infoDeleter2 = new InfoDeleter(phoneBook, rwLock, 3);

        InfoFinder infoFinder1 = new InfoFinder(phoneBook, rwLock, true, "forth");
        InfoFinder infoFinder2 = new InfoFinder(phoneBook, rwLock, true, "tenth");
        InfoFinder infoFinder3 = new InfoFinder(phoneBook, rwLock, false, "4366");

        infoAdder1.start();
        infoAdder2.start();
        infoAdder3.start();
        infoAdder4.start();

        infoDeleter1.start();
        infoDeleter2.start();

        infoFinder1.start();
        infoFinder2.start();
        infoFinder3.start();

        Thread.sleep(100);
        phoneBook.closeWriter();
    }
}