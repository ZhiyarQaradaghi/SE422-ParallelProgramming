package Week_13.Lecture_1;

/*
    Software Models and Low Level Optimizations 

    Agenda:
    1. Software Models - describe
    2. Low Level Optimizations - 2 types: 1. Removing blocks of code | 2. Code reordering
    3. Both have affect on the Execution Order 
    4. How these are related to the Memory Order 
    
    A software model is a model that describes a computation for you. The code you write is a software model and the compiled machine code is another software model.

    Code order describes the order of the operations - sequencial order of operations - your code

    Machine code (your compiled code) describes the Execution Order - both the code order and machine code end up generating the execution order (what has been executed) in most cases. 
    
    In other classes you never cared about the execution order, because it is generally the same. In SE422, the mismatch between the code order and the execution order is important and can be extremely harmful. 
    This is because the orders do not care about multi threading but only care about single thread.

    Code ---> Bytecode ---> JVM ---> Machine Code
          | (Low level optimizations that remove blocks of code to make it faster)

    The execution order is dynamic, it is changing all the time. The code order is fixed will not change. When I load it into hardware layer, maybe a variable in the code at certain point will be removed.
    The low level optimization will always change the execution order.


 
    */
public class Main {
    public static void main(String[] args) {
        
        for (int i = 0; i < 100; i++) {
            int x = 10;// removed by the JVM
            int y = 20; // removed by the JVM

            int result = x + y; // removed by the JVM

            // This is a low level optimization that removes blocks of code
            // The JVM will remove all the code that is not used -- it will happen after a while not immediately
            int unusedCalculation = result * 1000; // removed by the JVM

            System.out.println("Result: " + result);
        }

        // re ordering of variables
        int x;
        int y;

        // JVM may reorder these lines
        // SOME reordering of the code is OK some are NOT because it will affect the result 
        // What is OK and what is NOT OK? It is about what is consistent between the code order and the execution order. What does the consistency mean here?
        // Consistency has nothing to do with the order of instructions... You were looking at the output to determine whether the code was consistent or not.
        // The output is whether you remain in the same STATE - whether you will reach the same state after the reordering -
        // SOFTWARE CONSISTENCY - Whether the state of the software is the same after the reordering or not -- Whether the reordering of the memory operations will affect the state of the software or not
        /*
        we will get the same MEMORY state after the reordering -- if the state remains the same then it is OK and the reordering is PERMITTED
        The JVM knows the reordering is OK because the state of the memory is the same. 

        Code order:
        x=0                 Main memory
        x=10                x = 10
        y=0                 y = 20
        y=20


        What defines consistency is crucial. This is the key to make the entire architecture work. 
        The JVM will reorder the code as long as it does not change the output of a single thread. BUTTT they are ignoring multi-threading -- some of these reordering will affect the multi-threading.
        From a single thread perspective, the reordering is OK. But from a multi-threading perspective, the reordering is NOT OK.
        The x86 architecture doesnt reorder much but the ARM architecture does reorder a lot.

        // SUM UP: Consistency ---> Memory Reordering (whether the order of these operations will produce the same STATE or not) 
        They are trying to make the software faster but at the cost of consistency, here the inconsistency is the multi-threading, one thread is ok but multi-threading is not consistent.
        Why software consistency just for memory and not cache or registry or other things? Because memory is the only thing that is shared between threads/cores. 

                    The shared resource is the memory
        Core - thread 1   ---->  Memory <---- Core - thread 2
        
        */ 
        x = 0;
        x = 10;

        y = 0;
        y = 20;


        int result = x + y; 
        System.out.println("Result: " + result);
    }
}
