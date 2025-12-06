import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Task1AsyncArray {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<int[]> initialArrayFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            int[] arr = new Random().ints(10, 1, 10).toArray();
            printTime("Генерація масиву", start);
            return arr;
        });

        initialArrayFuture.thenAcceptAsync(arr -> 
            System.out.println("Початковий масив: " + Arrays.toString(arr))
        );

        CompletableFuture<int[]> modifiedArrayFuture = initialArrayFuture.thenApplyAsync(originalArr -> {
            long start = System.nanoTime();
            int[] newArr = Arrays.stream(originalArr).map(x -> x + 5).toArray();
            printTime("Збільшення елементів (+5)", start);
            return newArr;
        });

        modifiedArrayFuture.thenAcceptAsync(arr -> 
            System.out.println("Модифікований масив: " + Arrays.toString(arr))
        );

        CompletableFuture<BigInteger> factorialFuture = modifiedArrayFuture.thenApplyAsync(modArr -> {
            long start = System.nanoTime();
            int sumMod = Arrays.stream(modArr).sum();
            int sumOrig = Arrays.stream(modArr).map(x -> x - 5).sum();
            int totalSum = sumMod + sumOrig;
            
            BigInteger result = calculateFactorial(totalSum);
            printTime("Обчислення факторіалу", start);
            return result;
        });

        factorialFuture.thenAcceptAsync(result -> 
            System.out.println("Результат факторіалу: " + result)
        ).thenRunAsync(() -> 
            System.out.println("Завдання 1 завершено")
        );

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BigInteger calculateFactorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private static void printTime(String operation, long startTime) {
        long duration = System.nanoTime() - startTime;
        System.out.printf("Час виконання [%s]: %d нс%n", operation, duration);
    }
}
