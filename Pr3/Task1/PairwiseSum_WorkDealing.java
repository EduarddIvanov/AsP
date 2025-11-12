package task1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PairwiseSum_WorkDealing {

    private final int[] array;
    private final ExecutorService executor;
    private final int numCores;

    public PairwiseSum_WorkDealing(int[] array) {
        this.array = array;
        this.numCores = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(numCores);
    }

    static class SumChunkCallable implements Callable<Long> {
        private final int[] array;
        private final int startPairIndex;
        private final int endPairIndex;

        SumChunkCallable(int[] array, int startPairIndex, int endPairIndex) {
            this.array = array;
            this.startPairIndex = startPairIndex;
            this.endPairIndex = endPairIndex;
        }

        @Override
        public Long call() {
            long sum = 0;
            for (int i = startPairIndex; i < endPairIndex; i++) {
                sum += (long) array[i] + (long) array[i + 1];
            }
            return sum;
        }
    }

    public long calculate() {
        int numPairs = array.length - 1;
        if (numPairs <= 0) return 0;
        List<Callable<Long>> tasks = new ArrayList<>();
        int chunkSize = numPairs / numCores;
        int start = 0;

        for (int i = 0; i < numCores; i++) {
            int end = (i == numCores - 1)
                    ? numPairs 
                    : start + chunkSize;

            if (start < end) { 
                tasks.add(new SumChunkCallable(array, start, end));
            }
            start = end;
        }

        long totalSum = 0;
        try {
            List<Future<Long>> futures = executor.invokeAll(tasks);
            for (Future<Long> future : futures) {
                totalSum += future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Помилка під час виконання Work Dealing: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        return totalSum;
    }
}
