package knu.dc.taskA;

public class MainA {
    public static void main(String[] args) throws InterruptedException {
        ReadWriteLock rwLock = new ReadWriteLock();
        PhoneBook phoneBook = new PhoneBook();

        InfoAdder infoAdder1 = new InfoAdder(phoneBook, rwLock, "A", "12345");
        InfoAdder infoAdder2 = new InfoAdder(phoneBook, rwLock, "B", "23451");
        InfoAdder infoAdder3 = new InfoAdder(phoneBook, rwLock, "C", "34512");
        InfoAdder infoAdder4 = new InfoAdder(phoneBook, rwLock, "D", "45123");

        InfoDeleter infoDeleter1 = new InfoDeleter(phoneBook, rwLock, 2);
        InfoDeleter infoDeleter2 = new InfoDeleter(phoneBook, rwLock, 3);

        InfoFinder infoFinder1 = new InfoFinder(phoneBook, rwLock, true, "D");
        InfoFinder infoFinder2 = new InfoFinder(phoneBook, rwLock, true, "E");
        InfoFinder infoFinder3 = new InfoFinder(phoneBook, rwLock, false, "23451");
        InfoFinder infoFinder4 = new InfoFinder(phoneBook, rwLock, false, "51234");

        infoAdder1.start();
        infoAdder2.start();
        infoAdder3.start();
        infoAdder4.start();

        infoDeleter1.start();
        infoDeleter2.start();

        infoFinder1.start();
        infoFinder2.start();
        infoFinder3.start();
        infoFinder4.start();

        Thread.sleep(100);
        phoneBook.closeWriter();
    }
}