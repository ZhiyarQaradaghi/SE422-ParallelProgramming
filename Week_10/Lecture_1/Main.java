package Week_10.Lecture_1;

import java.util.concurrent.SynchronousQueue;

// Synchronized Queue - Another type of Synchronizer 

/*
 1. What are Thread Synchonizers? (in multithread programming)
 2. Introduce Synchronous Queues 
 3. Java Documentation of Synchronous Queue class - talk about their properties in detail and how they impact multithread programming
 4. When to use it, why to use it, what is the side effect of using Synchronous Queue?
 */


// In a multi-thread programming - speed of a thread depends on 2 factors: 
// 1. The complexlity of the computation it performs (Big O notation)
// 2. Depends on the runtime environment - how hot was the CPU, how many other threads/applications are running to share resources - slowing JVM, etc.

/*
Speed/Performance of thread cannot be predicted - you want parts of code A to be run only when some parts of B is done running - this is called Synchronization

Synchronizing is putting a block of code in a thread synchronized with another block of code in another thread - you are defining the :
Happens-before relationship - how causality is going to be created and propagated at runtime 

synchronizer tools: 
1. join
2. wait/notify 
3. countdownlatch

4. (new) SynchronousQueue - used to synchronize threads in a producer-consumer scenario. 
   1. Queue is a data structure that allows for the storage and retrieval of elements in a FIFO (First In First Out) manner - Creates a Queue.
   2. The Queue is thread-safe, meaning that multiple threads can access it concurrently without causing data corruption or inconsistencies.
   3. SynchronousQueue<E> - generic - generic allows you to create a datastructure and choose what data type it should hold 

   4. The size of any SynchronousQueue always equals zero, because it does not store elements - this datastructure cannot hold any elements - this is what makes it different from other queues.
   5. If you have a thread that calls put() - it will enter sleeping mode until another thread calls take() 
    -- if you have a thread calls take() and the put is not called() then it will also be put to sleep until the put() is called 
    There will not be deadlock because there is no locking - the threads are not waiting for each other to finish - they are just waiting for the other thread to call the method that will wake them up.

    I dont need a size, because i can add data and remove data at the same time - this is a blocking queue that does not store elements. The adding is producer and remove is consumemr.



   - It is a blocking queue that does not store elements. 
   - When a producer thread tries to add an element to the queue, it will block until a consumer thread removes that element.
   - Similarly, when a consumer thread tries to remove an element from the queue, it will block until a producer thread adds an element.
   - This ensures that the producer and consumer threads are synchronized and can communicate with each other effectively.
 */

public class Main {
    public static void main(String[] args) {
        SynchronousQueue<String> queue = new SynchronousQueue<>(); // implements BlockingQueue interface - anytime you see BlockingQueue, then under certain conditions a thread can be put to sleep 


        // The synchronous queue is ENSURING that the producer and consumer are running in sync - in exactly the same speed 
        // - because there is no size in the queue like if one of the threads is trying to add more data then it cannot
        
        // Whats the advantage of this compared to wait and notify? 
        // The advantage is that this isnt just a synchronizer - it is also a data structure - it is a queue that allows you to exchange data between threads - the data can be anything - 
        // it can be a string, an object, etc. -- you will have a synchronizer and a channel to exchange data between threads - this is a very powerful tool.

        // All the producers should run in the same speed as the consumer regardless of how many threads you have - in SYNC
        Thread producer = new Thread(() -> {
            String[] messages = {"Hello", "World", "from", "SynchronousQueue"};
            try {
                for(String msg : messages) {
                    System.out.println("Producing...");
                    queue.put(msg); // Blocks until a consumer take it
                    System.out.println("Produced: " + msg);
                }
                queue.put("DONE"); // Signal the consumer to stop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                String msg;
                while (!(msg = queue.take()).equals("DONE")) { // Blocks until a producer puts it
                    System.out.println("Consuming...");
                    System.out.println("Consumed: " + msg);
                }
                System.out.println("Consumer finished.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        


        producer.start();
        consumer.start();
        

        // queue.add("Hello");
        // queue.peek();
    }    
}
