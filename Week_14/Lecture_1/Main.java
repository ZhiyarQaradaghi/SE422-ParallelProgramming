package Week_14.Lecture_1;

/*
Reference JSR 133 (in moodle) - For next three lecture


 Memor Barriers (Memory Fences) in programming
 Agenda:
 Terminology - Store, Load 
 Memory Models 
 Memory Fences 
 Volatile - how it plays with memory fences     

 recap:

 Code order and execution order are not the same thing because of low level optimizations - like reordering of the operations - some will occur before contradicting what you wrote in the code 

 -- Sequential Consisting because SLOW performance -- but dont worry about reordering 
 -- CPUs give up consistency for performance - partially consistent 

 Optimizations is all about memory operations - like reordering operations is affecting memory and causality travels
 
 Store --> CPU writes to memory
 Load ---> CPU reads from memory

 in which order is the store and load ordered? - the whole reordering just reorders store and load operations 

 ALU storing and loading from and to Registry -- intermediate layer -- l caches storing and loading to dram and then dram to storage

 Memory reordering is just how the load and store operations are reordered

 4 Types of reordering can occur 
 1. Load load -- one read and another read will swap : A -> B = B -> A (both load operations)
 2. Load Store -- one read and one write will swap
 3. Store store - one write will swap with another write
 4. Store load -- one write will swap with a read 

 NO other reordering 

 High level programming language also offers memory model, in JAVA it offers JMM (java memory model -> what kind of reordering will occur, prevent what ordering, with tools ... etc)
 CPU publishes a memory model that will tell you if we have a load store operation they will reorder, they will tell you what they will reorder or not reorder

 Imagine a variable called AUIS, based on JMM it should not be reordered i any shape -- CPU says no we want to reorder it so on the Java side it will use certain tools to prevent the CPU reordering it

 The store total order consistency -- allowing Store --> Load reordering --- x86 Intel/AMD 

 ARM allows all 4 types of reordering 

 What is a memory fence/barrier?

 It is basically a barrier where you prevent certain types of reordering
 A tool to prevent certain memory operation reordering 

 Example: Store store fence -- as a fence it means im telling the CPU do not reorder this

 ALLOWED  <----------
 |write1           |
 |_______ <-- FENCE|
 |-> ALLOWED       |
 |write2-----------           
 |-> NOT ALLOWED   

 It is not allowed to put w1 AFTER w2
 The store before w1 the fence should not happen after the store after the fence w2  

 Read read fence 

 ALLOWED  <----------
 |--read1          |
 |_______ <-- FENCE|
 |-> ALLOWED       |
 |read2  -----------           
 |--> NOT ALLOWED   


 Same logic for load store and store load fences 


 Where is the load operations and store operations, then the memory fences


 in code example:
 From the DRAM and loads the data and put it in registry when loading the value of sum in loop
 The CPU will add a fence for the constructor when it is called before anything else 
 The CPU will never remove a fence and will always respect it 


 t1                                                                                                                                                 t2 
 x<-- volatile
 (write operations here will remain here not after the write of volatile)                                               |
 (load operations here will remain here not after the write of volatile)                                                |
 Happens before should remain here and not moved after the write x                                                      |  
 [STORE STORE fence]                                                                                                    |
 [LOAD STORE fence]  (both are called release fence -- all operation must finish then release before the write on x)    |
 (before WRITE)                                                                                                         |
                                                                                                                        |
 write operation on x in t1     |                                                                                           read operation on x in t2 <- THIS READ SHOULD NOT MOVE ANYWHERE BELOW
                                                                                                                            [LOAD LOAD]
                                                                                                                            [LOAD STORE]
                                                                                                                            (ACQUIRE FENCE -- lets finish the reading then we will continue)
                                                                                                                            acquiring all the data before continueing
 5 fences here because of volatile (the 5th fence will be shown next lecture)

 I would like to add 2 fences such that all the code below read on x should not move above it --

 HOMEWORK:
 can the read and the write be swapped between the threads?
 if i have another store operations after the write x, can it move up before the write x
 if i have a load operations after the write, can it move up before the write x 
 */
public class Main {
    public static void main(String[] args) {
        
    }    
}
