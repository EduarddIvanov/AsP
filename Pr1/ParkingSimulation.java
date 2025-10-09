import java.util.concurrent.Semaphore;
import java.time.LocalTime;

public class ParkingSimulation {

    private static final int DAY_SPOTS = 5;
    private static final int NIGHT_SPOTS = 8;

    public static void main(String[] args) {
        System.out.println("Симуляція паркування розпочинається!\n");

        int availableSpots = getAvailableSpotsByTime();
        Semaphore parkingSemaphore = new Semaphore(availableSpots);

        System.out.printf("Зараз %s. Кількість доступних місць: %d%n%n",
                LocalTime.now().withNano(0), availableSpots);

        for (int i = 1; i <= 15; i++) {
            Thread carThread = new Thread(new Car("Автомобіль #" + i, parkingSemaphore));
            carThread.start();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Помилка: перервано затримку між приїздом машин.");
            }
        }
    }

    private static int getAvailableSpotsByTime() {
        int hour = LocalTime.now().getHour();
        if (hour >= 6 && hour <= 20) {
            return DAY_SPOTS;
        } else {
            return NIGHT_SPOTS;
        }
    }
}
