package task2;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import Validator.Validator; 

public class FileSearchManager {

    public static void run(Scanner scanner) {
        scanner.nextLine(); 
        
        System.out.println("\n--- Завдання 2: Пошук файлів ---");
        
        String directoryPath = Validator.getValidatedDirectory(scanner);
        String extension = Validator.getValidatedExtension(scanner);

        File startDir = new File(directoryPath);
        
        ForkJoinPool pool = new ForkJoinPool();
        FileSearch_WorkStealing task = new FileSearch_WorkStealing(startDir, extension);

        System.out.println("Розпочинаю пошук...");
        long startTime = System.nanoTime();
        
        long count = pool.invoke(task);
        
        long endTime = System.nanoTime();
        pool.shutdown();

        System.out.println("\n--- Результат ---");
        System.out.println("Директорія: " + directoryPath);
        System.out.println("Розширення: " + extension);
        System.out.println("Знайдено файлів: " + count);
        System.out.printf("Час виконання: %.4f мс\n", (endTime - startTime) / 1_000_000.0);
    }
}
