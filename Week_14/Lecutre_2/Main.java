package Week_14.Lecutre_2;

/*
    Reference: JSR 133 Document in moodle

 Synchronious Consistency and Volatile Variables

 Agenda:
 Recap + idea of having memory model
 Voltile keyword <-- effect 
 Synchronous Consistency 
 How it is implemented in x86 architecture - memory fences (lock)
 Trade off of volatile - limitations 

 Understanding the behavior of volatile how it deals with fences 
 Understanding Synchronous Consistency 

 Memory model -- DEFINING Constraints on memory operations - tells you what kind of operations can be reordered and the constraints on the reordering operations 
 - in JAVA it is called JMM (Java Memory Model) -- how java treats the memory with the constraints on the memory especially the order of the operations (happens before relations) and the reordering constraints
 
 JMM will tell the compiler, the JVM, and the hardware to follow its memory model 

 The type of operations in HW (CPU) is memory - store and load operations (how can these 2 operations be reordered)

 A memory fence is restricting the reordering of certain operations - because by default the CPU might reorder them
 Memory fence example: Constructor in java -- constructor code must finish then the rest of the code can 

 Type of memory fences (barriers)
 
 [ Store load ]
 
 |Store
 |------
 |-> ALLOWED
 |Store
 |-> ALLOWED
 |Load
 |-> Not ALLOWED
 |Load
 |-> NOT ALLOWED  

 The store cannot be reordered such that it happens after the Load operation

 When you are using VOLATILE -- JAVA will add 5 fences in 5 different places to restrict the code reordering


 VOLATILE VARIABLE X 

 [Thread 1]                                                            [Thread 2]

 If I have store or load here then I cannot move it 
 after the Store X - nothing before should cross that
 volatile  

 Called Release fence -- releasing the data first then write 
 volatile and move on
 ---------
[STORE STORE]                                                           (anything here can still move down)
[LOAD STORE]
 Store X                                                                [STORE LOAD]<-- ensure Store X -> Load X  

          (anything here can move up)
[STORE LOAD]<-- ensure Store X -> Load X                                Load X
                                                                        -----------
                                                                        [LOAD LOAD]
                                                                        [LOAD STORE]
                                                                        After the load 
                                                                        These operaitons after the read should not be moved above the Load X
                                                                        Everything after the load x should remain after it and not be reordered 

                                                                        THe operations should not HAPPEN before the Load X

                                                                        Always prevent whats up not to go down -- I cannot describe the operations to not to go up 
                                                                        but I can describe the Load X to NOT go down

                                                                        ACQUIRE Fence -- acquire all the data then move on
                                                                        (operations)



(new topic starts here) Do we have a gaurantee that the Load X must happen before the Store X or the Store X -> Load X ?? No, because the Load or the store can move up and down as it pleases

THats why in volatile they want to create a happens before relation with Store X --> Load X using [STORE LOAD] fence ---> STORE finishes then Load Starts <--- SYNCHRONOUS MEMORY CONSISTENCY

Everything around the Store X must happen before EVERYTHING In Load X 

Side affect -- the main memory finishes the write and is up to date so the Load X will read the most updated and recent value 
            -- VISIBLITY, if i write data to volatile then the next read should see it GAURANTEED <-------- TOTAL ORDER for these 2 operations 

previously maybe the read starts while im writing or before im writing to X <--- before the Synchronous Memory Consistency 



[STORE STORE] (in x86 this will not be needed)
[LOAD STORE] (in x86 this will not be needed)
Store X                                     
                                                                                                        [STORE LOAD] <--- YOU CAN remove this or the other one, only 1 of them, and still have synchronous memory consistency 
[STORE LOAD] - prefix lock add (mfence wil never be used because high cost)
                                                                                                         LOAD X
                                                                                                        [LOAD STORE]  (in x86 this will not be needed)
                                                                                                        [LOAD LOAD] (in x86 this will not be needed)



Synchronizing through memory is different from the locks --- the memory view across the threads is the same -- store X finishes and everyone will see it 
I am NOT delaying the threads, not putting the threads to sleep or whatever -- When we write, the next read will see it GAURANTEE

The cost of volatile are the fences (store load):


[Thread 1]                               [Thread 2]


volatile int x
volatile int y                         [STORE STORE]
                                        [LOAD STORE]
                                       x = 20
                                       [STORE LOAD] <-- I CAn remove this one  -- when the 2nd write (y = 10) finishes it, i will ensure that everything before it is done including writing x and y, i will release the data with up to date memory and move on -- Synchronized
                                       -- I can do this with Java just by writing y in the end -- the last one volatile

                                       [STORE STORE]
                                       [LOAD STORE]
                                        y = 10
                                        [STORE LOAD]
 








 */
public class Main {
    
}
