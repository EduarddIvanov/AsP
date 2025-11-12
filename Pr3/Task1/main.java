import java.util.Scanner;
import task1.PairwiseSumManager;
import task2.FileSearchManager;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Головне меню ---");
            System.out.println("1. Завдання 1: Попарна сума елементів масиву");
            System.out.println("2. Завдання 2: Пошук файлів у директорії");
            System.out.println("0. Вихід");
            System.out.print("Виберіть опцію: ");

            int choice = Validator.getValidatedInt(scanner, 0, 2);

            switch (choice) {
                case 1:
                    PairwiseSumManager.run(scanner);
                    break;
                case 2:
                    FileSearchManager.run(scanner);
                    break;
                case 0:
                    System.out.println("Завершення роботи.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}
