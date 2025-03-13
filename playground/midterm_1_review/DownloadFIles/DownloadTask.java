package playground.midterm_1_review.DownloadFIles;

import java.util.Random;

public class DownloadTask implements Runnable {
    private String fileName;

    public DownloadTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        simulateDownload();
    }

    private void simulateDownload() {
        Random random = new Random();
        int progress = 0;

        while(progress < 100) {
            try {
                Thread.sleep(1000);
                progress += random.nextInt(10);
                if (progress > 100) {
                    progress = 100;
                }

                synchronized (this) {
                    System.out.println("Downloading:  "+ fileName + " - " + progress + "%");
                }
            } catch (Exception e) {
            }
        }

        synchronized (this) {
            System.out.println("Downloaded: " + fileName);
        }
    }
    
}
