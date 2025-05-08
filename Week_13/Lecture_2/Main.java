package Week_13.Lecture_2;

/*
Part 2 for Software Models and Low Level Optimizations

 - ReOrder operations -
 Agenda:
    1. part 1 - recap
    2. ReOrdering operations 
    3. How is the reordering done at hardware level?
    4. Consistency vs Performance
    5. Sequential Consistency 



    Code order/Program order - order of logical operations
    When the computation is done, how is the comutation actually done? - Execution order, what will be executed is not what the code actually is - There are some optimizations that are done by the compiler and the JVM so the original code is not the same as the execution order.

    Why say Order? What is an order? An order is the order of the elements, describing which one comes after which one. The low level optimization all comes down to playing with the global order. 

    Imagine these operations:
    A -- swap these two and change order 
       |
    B -|
    C -- IF I remove this as OPTIMIZATION
    D

    The Key difference is in Hardware is it will ReOrder the operations. How can we reorder the operations in a certain way so that in our hardware it will be faster?
    Some ReOrdering is OK, some are NOT because it will affect the result. 

    When do we know that the reordering is not OK? It comes down to the data flow.
    x= 0; ----
    y = 0;  |
            |
    x = 10;-- data flow, they play with the same memory address -- data flow is about finding the common memory addresses
    y = 20;

    |- Write Address 1
    |
    |- Write Address 2 -- swapping this with any of the address 1 operations is OK because there is no data flow between adress 1 and address 2 operations
    |
    |- Read Address 1 -- if you swap address 1 operations with each other, you will get inconsistent results because there is a data flow between the two

    ReOrdering: What are the memory operations before in the code and what are the memory operations after in the execution order, and try to reorder them to eventually make them faster.

    Reordering only cares about 1 core at a time - when they look the operations they look at it at a -- single thread -- perspective. 

    If you have a single thread program, you will never have to worry about reordering. 

    Why is it a problem that the reordering doesnt care about multi threading? !!! 

    1. All about memory
    2. Reordering: When I flip things around, I will have speed increase but also you will REDEFINE THE HAPPENS BEFORE relations: x->y, after reorder: y->x ----> CAUSALITY AFFECTED!! 
    3. In Multithreading, this is a problem because causality propagated from other threads after reordering operations.

    From hardware perspective: 90% of the time always ReOrdering - NEVER TOUCH THE MACHINE CODE (only compiler or JVM) -- They will ALSO !! DELAY OPERATIONS
    1. H.W. will Re Order and Delay Operations
    
    
    The CPU will take a while to write the data to memory late, it will add it to a queue and do it later 

    Takeaway:

    1. When you are reordering and you only care about single thread perspective (SINGLE THREAD ORDER), it doesnt mean you will be fine with multi thread (GLOBAL ORDER) -- will have inconsistencies 
    2. Why the Global Order will be inconsistent? Because of causality - it takes only one variable for causality to travel to another thread 
    3. In SE355, you need to write code to travel causality - but in SE422 it only takes one variable to be shared between cores so that reordering is going to affect global order and causality to travel and cause inconsistencies
    


    How to fix? How to have a re ordering that will keep Global Order consistent?

    Solution proposed 60 years ago - Sequential Order 

    Cores:
    P1  P2  P3  P4
    --------------- the processes wants to talk to the memory

    Sequential Order: Allow only one process to talk to the memory at a time - One operations at a time - P1 will start and once its done, P2 will start and so on ... -- RESTRICTING MEMORY OPERATIONS

    p1:     p2:
    A       X
    B       Y

    Operations order: A -> B -> X -> Y
    rerun: X->A->Y->B  --- both fine in a single thread and multi thread perspective 
    Sequential Order offers a TOTAL GLOBAL ORDER 
    
    -- Not a single manufacturer will implement this because the PERFORMANCE IS TRASH - CPU will be bottlenecked heavily to achieve total global order 

    There is always a trade of:
    Performance <-------> Consistency 

    Total Global Order has max consistency but 0 performance
    
    Some CPUs: like ARM has weaker consistency and high performance -- this means more Complexity to application layer because you have to interfere and ensure reordering doesnt ruin consistency 

    H.W.: In the code (check moodle) the reordering makes a = 1 


 */
public class Main {
    
}
