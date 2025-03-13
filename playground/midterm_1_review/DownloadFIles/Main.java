package playground.midterm_1_review.DownloadFIles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        String[] files = {"file1.zip", "file2.mp4", "file3.pdf", "file4.jpg"};

        // threadpool
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores+1);

        DownloadTask[] task = new DownloadTask[files.length];

        for (int i = 0; i < files.length; i++) {
            task[i] = new DownloadTask(files[i]);
            pool.execute(task[i]);
        }

        System.out.println("Main thread is done");



    }
}
