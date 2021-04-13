import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Main {
    private static final int N = 1000;

    static class Task extends RecursiveAction {
        private int[] A, B, C;
        private final int stringNum;

        Task(int[] a, int[] b, int[] c, int id) {
            A = a;
            B = b;
            C = c;
            stringNum = id;
        }

        Task(int[] a, int[] b, int[] c) {
            this(a, b, c, -1);
        }

        void mult(int num) {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    C[num * N + i] += A[num * N + j] * B[j * N + i];
        }

        @Override
        protected void compute() {
            if (stringNum < 0) {
                ArrayList tasks = new ArrayList<Task>();
                for (int i = 0; i < N; i++)
                    tasks.add(new Task(A, B, C, i));
                invokeAll(tasks);
            } else mult(stringNum);
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();

        int[] A = new int[N * N];
        int[] B = new int[N * N];
        int[] C = new int[N * N];

        for (int i = 0; i < N * N; i++) {
            A[i] = rand.nextInt(5);
            B[i] = rand.nextInt(5);
        }

        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new Task(A, B, C));
        long end = System.currentTimeMillis();

        System.out.println(N+"x"+N+": " + (end - start) + " ms");
    }
}