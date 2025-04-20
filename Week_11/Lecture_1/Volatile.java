package Week_11.Lecture_1;



// Volatile in Java
/*
 Why use Volatile? Problem: 
 
 We have 2 threads, main and t1. Both of them will have the variable done with the value false. t1 has an infinite loop because it is waiting for done to become true.

 When the main wants to modify the value of done to true, this write operation will only be visible in main thread but not in t1. Why does it not show in t1? 

 It doesn't show in t1 because when you perform write on data to memory, the request will be processed - the core will change the value of done variable in local caches (L1, CPU registry, some L2). There are multiple copies of the variable done in the CPU cahces of the other threads.

 This means that we change the value in the cache layer only visible by core 1 - this will not automatically propagate to the other cores. L1 and CPU registry are per core caches. 

 The write request will be added to a queue and the CPU will go through all of them and update the memory. This queue data structure is called the STORE BUFFER. 
 
 Store buffer: All the write request will be added to the store buffer and then the CPU will go through them and update the memory.

 We have this to improve performance because writing to main memory is super slow. With the store buffer, the data is immediately visible and the core does not wait for the main memory to finish, it will add it to the queue then move on to the next instruction.
    
 Initially the value of the done variable is cached in the L1 cache of core 1. When the main thread wants to change the value of done to true, it will not be immediately visible to t1 because t1 is running on core 2 and it has its own L1 cache.
 The problem is that the other core (t1) will not see the change in the value of done because it is not updated in the main memory yet.

 VOlatile: 

 1. A keyword in java, telling the JVM to change how data are written to CPU. 
 2. The JVM is going to talk to the CPU such that it will not rely on caches whether for read or write operations - if you have a variable marked as volatile the write operation will be different
 3. How will the write operaiton be different? First, when the core1 wants to execute the main thread first the write request will be added to the store buffer queue like before but now we will instruct the CPU to make the entire update ATOMIC,
 this is done first by instructing the CPU to flush the store buffer before we move to the next instruction so that all the write operations are flushed into the main memory - this is atomic because no other core can add other writes in the middle of that operation.
 Once this write instruction is flushed, the memory will be updated and the done will contain the value true in the main memory.
 
 4. Through the cache coherent protocol, we are going to invalidate all the cached values for this variable - if you cached this data in this or other cores then you are going to signal them if they are holding this value for this variable to drop it and get the value from the main memory.


 So in summary the volatile keyword is going to make sure that the write operation is atomic and that all the other cores will invalidate their cached values for this variable, and everytime the other cores that want to get the value they wont get it from their cache but from the main memory. 

 Advantages of volatile:
 1. Visiblity - the write operation will be visible to all the threads and all the cores.

 Disadvantages of volatile:
 1. Performance hit - previously the write was extremely fast but now i need to flush it then make sure the main memory got the writes successfully and the other cores need to fetch it from the main memory. 
 Thats why in java, non volatile variables are the default.


What happens if we use volatile with an object like static volatile Student s = new Student();

1. We have  pointer s that points to an student object. If I change the pointer to point at another object, the value of that pointer will be visible to all the threads but has nothing to do with the object itself.

To make the object volatile, we need to make sure that the object itself is volatile. This means that all the variables of the object should be volatile.

 */
// public class Volatile {
//     static volatile boolean done = false;

//     public static void main(String[] args) throws Exception {
//         Thread t1 = new Thread(() -> {
//             while(!done) {

//             }
//             System.out.println("Thread 1 finished");
//         });

//         t1.start();


//         Thread.sleep(1000);

//         done = true; // this value will not be seen by thread 1 because it is not volatile yet
//         System.out.println("Main is done and the value becomes true");
//         t1.join();
//     }    
// }



// HOMEWORK: 
/*
 Memory Fences in Programming and how is it related to volatile:
1. Memory fences are used to prevent the CPU from reordering instructions in a way that would violate the intended order of operations.
2. In Java, the volatile keyword acts as a memory fence by ensuring that all reads and writes to a volatile variable are done in the order they appear in the code.
3. This means that if a thread writes to a volatile variable, all previous writes to other variables will be visible to other threads that read the volatile variable.
4. This is important for ensuring that the program behaves correctly in a multi-threaded environment, where multiple threads may be accessing and modifying shared variables.
// 5. Memory fences are used to ensure that the CPU does not reorder instructions in a way that would violate the intended order of operations.
 */
public class Volatile{
    static volatile int count = 0;
    public static void main(String[] args) throws Exception {
        Runnable incTask = () -> {
            count = 42;

            // case were volatile will not work because its not thread safe, volatile is not a lock but it doesnt limit access to variable to one thread at a time
            // the write operation is gauranteed to be visible to everyone as its from main memory, the read operation also 
            // lets assume t1 added 1 to 100 and it becomes 101 in main memory, a single read and write is thread safe but NOT multiple read and write operations 
            // because when t1 writes the value to main memory, t2 will read the value from main memory and then add 1 to it and write it back to main memory. 
            // the only case where this is thread safe is having 1 producer and multiple consumers, the producer will write the value to main memory and the consumers will read it from main memory.
            // TAKEAWAY: individual operations is thread safe but not multiple operations.
            for (int i = 0; i < 1000000; i++) {
                count++;
            } 
        };

        Thread t1 = new Thread(incTask);
        Thread t2 = new Thread(incTask);
        t1.start();
        t2.start();

        // 2 threads that will change the value to 42 

        t1.join();
        t2.join();

        System.out.println("Main Count: " + count); 
    }
}