package Week_4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Fixed Thread Pool
public class ThreadPoolExample {
    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores+1); // why cores + 1 is faster than cores? If you keep it cores, sometimes where you have a task is very short and finish quick - between the time this core is free and until it gets another - this time is wasted 
        // with core + 1, once the core finsihes a task quickly and is free, it will immediately get the next task 
        
        // only 3 threads, no less no more threads - maximum number of threads that this pool is going to use 
        // 3 threads are created and are waiting in idle 

            Runnable task = () -> {
            for(int i=0; i < 100000; i++) {
                System.out.println("AUIS");
            }
        };
        pool.execute(task);
    }    
}
