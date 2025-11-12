import java.io.File;
import java.util.Scanner;

public class Validator {
    public static int getValidatedInt(Scanner scanner, int min, int max) {
        int input;
        while (true) {
            try {
                if (!scanner.hasNextInt()) {
                    System.out.println("Помилка: Введіть ціле число.");
                    scanner.next(); 
                    continue;
                }
                input = scanner.nextInt();
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.printf("Помилка: Число має бути в діапазоні [%d, %d].\n", min, max);
                    System.out.print("Спробуйте ще раз: ");
                }
            } catch (Exception e) {
                System.out.println("Некоректний ввід. Спробуйте ще раз: ");
                scanner.next(); // Очистити буфер
            }
        }
        return input;
    }


    public static String getValidatedDirectory(Scanner scanner) {
        String path;
        while (true) {
            System.out.print("Введіть повний шлях до директорії: ");
            path = scanner.nextLine();
            if (path.isEmpty()) continue; 
            
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                break;
            } else {
                System.out.println("Помилка: Директорія не існує або це не директорія.");
            }
        }
        return path;
    }

    public static String getValidatedExtension(Scanner scanner) {
        String extension;
        while (true) {
            System.out.print("Введіть розширення файлу (напр., .pdf або .txt): ");
            extension = scanner.nextLine();
            if (extension.startsWith(".") && extension.length() > 1) {
                break;
            } else {
                System.out.println("Помилка: Розширення має починатися з '.' (наприклад, .pdf).");
            }
        }
        return extension;
    }
}
