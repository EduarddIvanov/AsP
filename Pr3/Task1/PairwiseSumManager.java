package task1;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import Validator.Validator; 

public class PairwiseSumManager {

    public static void run(Scanner scanner) {
        System.out.println("\n--- Завдання 1: Попарна сума ---");
        System.out.print("Введіть кількість елементів масиву (напр., 20 000 000): ");
        int size = Validator.getValidatedInt(scanner, 2, Integer.MAX_VALUE);

        System.out.print("Введіть початкове значення діапазону (min): ");
        int min = Validator.getValidatedInt(scanner, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.print("Введіть кінцеве значення діапазону (max): ");
        int max = Validator.getValidatedInt(scanner, min, Integer.MAX_VALUE); // max не менше min

        // 1. Генерація масиву
        int[] array = generateArray(size, min, max);
        System.out.println("Масив згенеровано.");
        if (size <= 50) {
            System.out.println("Згенерований масив: " + Arrays.toString(array));
        } else {
            System.out.println("Згенерований масив (перші 50 ел.): " +
                    Arrays.toString(Arrays.copyOf(array, 50)) + "...");
        }

        // 2. Версія Work Stealing (Fork/Join)
        runWorkStealing(array);

        // 3. Версія Work Dealing (ExecutorService)
        runWorkDealing(array);
    }

    private static void runWorkStealing(int[] array) {
        System.out.println("\n--- Запуск (Work Stealing / ForkJoinPool) ---");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        PairwiseSum_WorkStealing task = new PairwiseSum_WorkStealing(array, 0, array.length - 1);
        
        long startTime = System.nanoTime();
        long totalSum = forkJoinPool.invoke(task); // Запуск і очікування
        long endTime = System.nanoTime();
        
        forkJoinPool.shutdown();

        System.out.println("Результат (Work Stealing): " + totalSum);
        System.out.printf("Час виконання (Work Stealing): %.4f мс\n", (endTime - startTime) / 1_000_000.0);
    }

    private static void runWorkDealing(int[] array) {
        System.out.println("\n--- Запуск (Work Dealing / ExecutorService) ---");
        PairwiseSum_WorkDealing workDealing = new PairwiseSum_WorkDealing(array);

        long startTime = System.nanoTime();
        long totalSum = workDealing.calculate();
        long endTime = System.nanoTime();

        System.out.println("Результат (Work Dealing): " + totalSum);
        System.out.printf("Час виконання (Work Dealing): %.4f мс\n", (endTime - startTime) / 1_000_000.0);
    }

    private static int[] generateArray(int size, int min, int max) {
        int[] array = new int[size];
        Random random = new Random();
        int bound = (max - min) + 1;
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(bound) + min;
        }
        return array;
    }
}
