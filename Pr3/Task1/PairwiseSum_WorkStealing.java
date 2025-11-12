package task1;

import java.util.concurrent.RecursiveTask;

public class PairwiseSum_WorkStealing extends RecursiveTask<Long> {
    private static final int THRESHOLD = 100_000; 
    private final int[] array;
    private final int startPairIndex; 
    private final int endPairIndex;   
    public PairwiseSum_WorkStealing(int[] array, int startPairIndex, int endPairIndex) {
        this.array = array;
        this.startPairIndex = startPairIndex;
        this.endPairIndex = endPairIndex;
    }

    @Override
    protected Long compute() {
        int numPairs = endPairIndex - startPairIndex;
        if (numPairs <= THRESHOLD) {
            return computeSequentially();
        }
        int midPairIndex = startPairIndex + (numPairs / 2);
        PairwiseSum_WorkStealing leftTask = new PairwiseSum_WorkStealing(array, startPairIndex, midPairIndex);
        PairwiseSum_WorkStealing rightTask = new PairwiseSum_WorkStealing(array, midPairIndex, endPairIndex);
        leftTask.fork();
        
        long rightResult = rightTask.compute();
        long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = startPairIndex; i < endPairIndex; i++) {
            sum += (long) array[i] + (long) array[i + 1];
        }
        return sum;
    }
}
