package Week_4.Lecture_3;

// Main reason this course is 400 level is because i do not know which thread happens before which thread

// INSTRINSIC LOCKS
public class Main {
    public static void main(String[] args) {
        System.out.println("Start of Main");
        SharedCounter sharedCounter = new SharedCounter();

        Thread t1 = new Thread(()->{
            for (int i = 0; i < 10000; i++) {
                // System.out.println("t1");
                sharedCounter.increment();
                try {
                    // Thread.sleep(1);
                } catch (Exception e) {}
            }
        });

        Thread t2 = new Thread(()->{
            for (int i = 0; i < 10000; i++) {
                // System.out.println("t2");
                sharedCounter.increment();
                try {
                    // Thread.sleep(1);
                } catch (Exception e) {}
            }
        });
        
        t1.start();
        t2.start();
        
        // I want Main thread to wait for t1 and t2, once they are done then execute the print end statement
        try {
            t1.join(); // the thread that is executing the join method (main thread is executing the join) is going to wait for t1 to finish
            t2.join(); // same here but for t2 
            System.out.println("Result= "+sharedCounter.getCount());
            // Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}


class SharedCounter {
    private int count = 0;

    // synchronized = intrinsic lock - only one thread can access this method at a time
    // enough to fix this to be thread safe 
    // public synchronized  void increment() { // data race - when multiple threads are trying to access the same data at the same time
    //     count++; // this line is not thread safe - it means if i have multiple threads trying to access this code then i will get unpredictable results 
    //             // like the correct behavior should be 20000 but it will be a random number because 2 threads running parallel or concurrently are trying to access the same variable/data - count  
    // }

    public void increment() { // the 2 prints can run in parallel - but the increment should be done one at a time
        System.out.println("Incrementing count");
        synchronized (this) { // only one of them is going to lock the object - the other one is going to wait until the lock is unlocked 
            count++;
        } // once i exit this scope, the lock is unlocked
        // synchronized (System.in) {
        //     count++;
        // }
        System.out.println("Incremented count");
    }

    public int getCount() {
        return count;
    }
}