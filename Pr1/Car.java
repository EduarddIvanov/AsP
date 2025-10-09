import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;


public class Car implements Runnable {
    private final String name;
    private final Semaphore parkingSemaphore;
    private final Random random = new Random();

    public Car(String name, Semaphore semaphore) {
        this.name = name;
        this.parkingSemaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            System.out.printf("%s прибуває до парковки...%n", name);

            if (parkingSemaphore.tryAcquire(5, TimeUnit.SECONDS)) {
                System.out.printf("%s припаркувався. Вільних місць залишилось: %d%n",
                        name, parkingSemaphore.availablePermits());

                Thread.sleep((1 + random.nextInt(5)) * 1000);

                parkingSemaphore.release();
                System.out.printf("%s виїхав. Вільних місць тепер: %d%n",
                        name, parkingSemaphore.availablePermits());
            } else {
                System.out.printf("%s не дочекався місця і поїхав геть.%n", name);
            }

        } catch (InterruptedException e) {
            System.out.printf("⚠%s: помилка під час паркування.%n", name);
        }
    }
}
