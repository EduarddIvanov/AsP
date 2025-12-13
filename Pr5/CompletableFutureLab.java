import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CompletableFutureLab {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("=== ЗАВДАННЯ 1: Агрегація даних з кількох джерел ===");
        runDataAggregationTask();

        System.out.println("\n=== ЗАВДАННЯ 2: Планування подорожі та вибір маршруту ===");
        runTravelPlanningTask();
    }

    private static void runDataAggregationTask() {
        long start = System.currentTimeMillis();

        CompletableFuture<String> userProfileFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1000);
            return "User: ID=101, Name=Oleksii";
        });

        CompletableFuture<String> userOrdersFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1500);
            return "Orders: [Laptop, Mouse, Keyboard]";
        });

        CompletableFuture<String> userRecommendationsFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(800);
            return "Recommendations: [Monitor, HDMI Cable]";
        });

        CompletableFuture<Void> allData = CompletableFuture.allOf(
                userProfileFuture, userOrdersFuture, userRecommendationsFuture
        );

        allData.thenRun(() -> {
            try {
                String profile = userProfileFuture.get();
                String orders = userOrdersFuture.get();
                String recs = userRecommendationsFuture.get();
                
                System.out.println("FULL REPORT GENERATED:");
                System.out.println(profile);
                System.out.println(orders);
                System.out.println(recs);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).join();

        System.out.printf("Task 1 finished in %d ms\n", System.currentTimeMillis() - start);
    }

    private static void runTravelPlanningTask() throws ExecutionException, InterruptedException {
        CompletableFuture<String> server1 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(300);
            return "Server UA-East";
        });
        CompletableFuture<String> server2 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(100);
            return "Server UA-West";
        });
        CompletableFuture<String> server3 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(500);
            return "Server UA-Central";
        });

        CompletableFuture<Object> fastServer = CompletableFuture.anyOf(server1, server2, server3);
        System.out.println("Fastest booking server responding: " + fastServer.get());

        CompletableFuture<TravelOption> trainFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(500);
            return 1200.0; 
        }).thenCombine(
            CompletableFuture.supplyAsync(() -> {
                simulateDelay(500);
                return 8.5; 
            }),
            (price, time) -> new TravelOption("Train", price, time)
        );

        CompletableFuture<TravelOption> busFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(400);
            return 800.0;
        }).thenCombine(
            CompletableFuture.supplyAsync(() -> {
                simulateDelay(400);
                return 10.0;
            }),
            (price, time) -> new TravelOption("Bus", price, time)
        );

        CompletableFuture<TravelOption> planeFuture = CompletableFuture.supplyAsync(() -> {
            return "Kyiv-Warsaw";
        }).thenCompose(route -> CompletableFuture.supplyAsync(() -> {
            simulateDelay(200);
            double basePrice = 3000.0;
            double tax = 500.0; 
            return new TravelOption("Plane (" + route + ")", basePrice + tax, 1.5);
        }));

        CompletableFuture<Void> allOptions = CompletableFuture.allOf(trainFuture, busFuture, planeFuture);

        allOptions.thenRun(() -> {
            try {
                List<TravelOption> options = new ArrayList<>();
                options.add(trainFuture.get());
                options.add(busFuture.get());
                options.add(planeFuture.get());

                System.out.println("--- Comparison Results ---");
                options.forEach(System.out::println);

                TravelOption bestOption = options.stream()
                        .sorted((o1, o2) -> Double.compare(o1.score(), o2.score()))
                        .findFirst()
                        .orElseThrow();

                System.out.println(">>> Best Recommended Option: " + bestOption.type);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).join();
    }

    private static void simulateDelay(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static class TravelOption {
        String type;
        double price;
        double durationHours;

        public TravelOption(String type, double price, double durationHours) {
            this.type = type;
            this.price = price;
            this.durationHours = durationHours;
        }

        public double score() {
            return price * durationHours;
        }

        @Override
        public String toString() {
            return String.format("%s: %.2f UAH, %.1f hours", type, price, durationHours);
        }
    }
}
