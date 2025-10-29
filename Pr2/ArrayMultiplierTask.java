import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayMultiplierTask implements Callable<Integer> {

    private final List<Integer> dataChunk;
    private final int multiplier;
    private final CopyOnWriteArrayList<Integer> resultList; 

    public ArrayMultiplierTask(List<Integer> dataChunk, int multiplier, CopyOnWriteArrayList<Integer> resultList) {
        this.dataChunk = dataChunk;
        this.multiplier = multiplier;
        this.resultList = resultList;
    }

    @Override
    public Integer call() throws Exception {
        int processedCount = 0;
        try {
            for (Integer number : dataChunk) {
                int result = number * multiplier;
                resultList.add(result);
                processedCount++;
                Thread.sleep(10); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            System.err.println("Потік був перерваний: " + Thread.currentThread().getName());
        }
        
        return processedCount;
    }
}
