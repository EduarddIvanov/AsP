import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Task2AsyncSequence {

    public static void main(String[] args) {
        CompletableFuture<Void> mainTask = CompletableFuture.runAsync(() -> {
            long globalStart = System.nanoTime();

            List<Integer> sequence = CompletableFuture.supplyAsync(() -> {
                List<Integer> list = new ArrayList<>();
                Random rand = new Random();
                for (int i = 0; i < 20; i++) {
                    list.add(rand.nextInt(100) + 1);
                }
                return list;
            }).join();

            System.out.println("Згенерована послідовність: " + sequence);

            int minPairSum = CompletableFuture.supplyAsync(() -> {
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < sequence.size() - 1; i++) {
                    int sum = sequence.get(i) + sequence.get(i + 1);
                    if (sum < min) {
                        min = sum;
                    }
                }
                return min;
            }).join();

            System.out.println("Мінімальна сума суміжних пар: " + minPairSum);

            long globalEnd = System.nanoTime();
            System.out.println("Час роботи асинхронних операцій: " + (globalEnd - globalStart) + " нс");
        });

        try {
            mainTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
