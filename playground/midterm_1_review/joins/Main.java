package playground.midterm_1_review.joins;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        
        Runnable[] tasks = new Runnable[5];
        Thread[] threads = new Thread[5];
        for (int t = 0; t < 5; t++) {
            tasks[t] = (()-> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Thread 1: " + i);
                }
            });

            threads[t] = new Thread(tasks[t]);
        };

        for (Thread thread : threads) {
            thread.join();
        }


        
        

    }
}
