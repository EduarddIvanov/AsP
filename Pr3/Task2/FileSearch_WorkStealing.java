package task2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FileSearch_WorkStealing extends RecursiveTask<Long> {

    private final File directory;
    private final String extension;

    public FileSearch_WorkStealing(File directory, String extension) {
        this.directory = directory;
        this.extension = extension;
    }

    @Override
    protected Long compute() {
        long count = 0;
        List<FileSearch_WorkStealing> subTasks = new ArrayList<>();
        
        File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                FileSearch_WorkStealing task = new FileSearch_WorkStealing(file, extension);
                task.fork(); 
                subTasks.add(task);
            } else {
                if (file.getName().endsWith(extension)) {
                    count++;
                }
            }
        }
        for (FileSearch_WorkStealing task : subTasks) {
            count += task.join(); 
        }

        return count;
    }
}
