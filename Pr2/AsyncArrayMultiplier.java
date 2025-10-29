import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class AsyncArrayMultiplier {

    private static final int CHUNK_SIZE = 10; 
    private static final int MIN_RANGE = -100;
    private static final int MAX_RANGE = 100;
    private static final int RANGE_BOUND = MAX_RANGE - MIN_RANGE + 1; 

    public static void main(String[] args) {

        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(System.in);
        int arraySize = getValidatedInput(scanner, "Введіть розмір масиву (40-60): ", 40, 60);
        int multiplier = getValidatedInput(scanner, "Введіть множник: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        scanner.close();
        List<Integer> sourceArray = generateRandomArray(arraySize);
        System.out.println("Оригінальний масив (" + sourceArray.size() + " ел.): " + sourceArray);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        List<Future<Integer>> futures = new ArrayList<>();
        
        CopyOnWriteArrayList<Integer> resultArray = new CopyOnWriteArrayList<>();

        System.out.println("\nРозподіл завдань...");
        for (int i = 0; i < arraySize; i += CHUNK_SIZE) {
            int start = i;
            int end = Math.min(i + CHUNK_SIZE, arraySize);
            
            List<Integer> subArray = sourceArray.subList(start, end);

            Callable<Integer> task = new ArrayMultiplierTask(subArray, multiplier, resultArray);

            Future<Integer> future = executor.submit(task);
            futures.add(future);
            System.out.println(" -> Створено завдання для індексів " + start + " - " + (end - 1));
        }

        System.out.println("\nОчікування результатів...");
        int totalProcessed = 0;
        
        for (Future<Integer> future : futures) {
            try {
                System.out.println("Перевірка... Завдання завершено? " + future.isDone() + 
                                   ", Скасовано? " + future.isCancelled());

                int processedCount = future.get(); 
                totalProcessed += processedCount;

                System.out.println("  [OK] Завдання успішно завершено. Оброблено: " + processedCount + 
                                   " елементів. (isDone: " + future.isDone() + ")");

            } catch (CancellationException e) {
                System.out.println("  [CANCELLED] Завдання було скасовано.");
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("  [ERROR] Помилка під час виконання завдання: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\nВсі завдання виконано. Загалом оброблено: " + totalProcessed);
        executor.shutdown(); 
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); 
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.out.println("\nФінальний масив (" + resultArray.size() + " ел.): " + resultArray);
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        System.out.println("=============================================");
        System.out.println("Загальний час виконання програми: " + durationMs + " мс");
        System.out.println("=============================================");
    }

    private static List<Integer> generateRandomArray(int size) {
        List<Integer> array = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(RANGE_BOUND) + MIN_RANGE; 
            array.add(number);
        }
        return array;
    }

    private static int getValidatedInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Помилка: Значення має бути в діапазоні [" + min + ", " + max + "].");
                }
            } else {
                System.out.println("Помилка: Будь ласка, введіть ціле число.");
                scanner.next(); 
            }
        }
        return input;
    }
}
