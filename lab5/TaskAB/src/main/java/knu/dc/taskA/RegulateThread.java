package knu.dc.taskA;

public class RegulateThread extends Thread {
    int[] rank;
    int size;
    RegulateThread left;
    RegulateThread right;
    Barrier barrier;
    int numberThread;
    RegulateThread(int[] rank, Barrier barrier, int numberThread){
        this.rank = rank;
        this.size = rank.length;
        this.barrier = barrier;
        this.numberThread = numberThread;
    }
    public void setLeft(RegulateThread left){
        this.left = left;
    }
    public void setRight(RegulateThread right){
        this.right = right;
    }
    public int getLeft(){
        return rank[0];
    }
    public int getRight(){
        return rank[size - 1];
    }
    @Override
    public void run() {
        System.out.println("Enter" + Thread.currentThread().getId());
        boolean isSwap;
        do {
            isSwap = false;
            for (int i = 0; i < size; i ++) {
                if (i == size - 1) {
                    break;
                }
                if (rank[i] == 1 && rank[i + 1] == 0) {
                    rank[i] = 0;
                    rank[i + 1] = 1;
                    i++;
                    isSwap = true;
                }
            }
        } while (isSwap);
        barrier.arriveAndWait();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int j = 0; j < numberThread; j++){
            int leftVal;
            int rightVal;
            if(left == null){
                leftVal = -1;
            }
            else{
                leftVal = left.getRight();
            }

            if(right == null){
                rightVal = -1;
            }
            else{
                rightVal = right.getLeft();
            }
            barrier.arriveAndWait();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if ((leftVal == -1 || (rank[0] == 0 && leftVal == 0) || (rank[0] == 1)) && rank[size - 1] == 1 && rightVal == 0) {
                for (int i = 0; i < size; i++) {
                    rank[i] = 0;
                }
            }
            if ((rightVal == -1 || (rank[size - 1] == 1 && rightVal == 1) || (rank[size - 1] == 0)) && rank[0] == 0 && leftVal == 1) {
                for (int i = 0; i < size; i++) {
                    rank[i] = 1;
                }
            }
            barrier.arriveAndWait();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (barrier) {
            for (int i = 0; i < size; i++) {
                if (rank[i] == 1) {
                    System.out.print(">");
                } else {
                    System.out.print("<");
                }
            }
            System.out.println("|" + Thread.currentThread().getId());
        }
    }
}
