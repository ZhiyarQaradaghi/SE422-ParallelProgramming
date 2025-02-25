package Week_3.Lecture_2;


public class Main {
    public static void main(String[] argzs) throws Exception {
        // while (true) {  // this line will be executed by the main thread - when JVM init he created the main thread and the main thread executes and dive into the main method and this line will be executed 
        //     System.out.println("loop 1"); // execution goes from top to bottom
        //     Thread.sleep(100); // will put the MAIN THREAD to sleeping mode - not the process itself because the entire execution is the main thread 
        // } // main thread wil go back to the start of the loop 

        // while(true) {
        //     System.err.println("loop 2");
        //     Thread.sleep(1000);
        // }
        Thread1 t1 = new Thread1();
        Thread2 t2 = new Thread2();

        // IN ORDER TO LAUNCH A THREAD YOU NEED A THREAD - SIDE AFFECT!! 
        // if i do not have a thread then i will not be able to create another thread - byproduct - one process = atleast one thread from that thread you can launch as many threads as you want 


        // MOST DIFFICULT PART 
        /*
         Imagine at thread 2 - how many lines of code will be executed at the other threads 

         At the same time - 2 lines of code could be executed from 2 different threads - main obstacle - execution path is no longer top to bottom in the entire process (when you have more than one thread)

        

         */

        
        t1.start(); // now it compiles but only the first loop will run - no paralleism/concurrency  - the start method will be finished executed in the main thread once the thread t1 is created 
        t2.start();
    }
}
