package Week_11.Lecture_3;


/*
  Ref: Chapter 15 in Java book 

  -- Recap Atomic Interger
  - Atomic Long / Advantages and Disadvantages - Performance
  - Algorithm Design to improve the performance ----> Most important part - How to think and incorporate atomic data type to algorithms 

  Recap: 

  Introduced atomic data types in Java.
  - Atomic classes under java .util.concurrent.atomic / in Java they are classes 
  WHat is atomic data type? 
  - They give you certain operations and these are lock free operations yet thread safe. -- They dont use locks yet even if that atomic value is shared between multiple thread it is still thread safe

  - Why is it thread safe and lock free? Becauses of the CAS operation which is compare and set/exchange. 

  lock cmpxchg [shared_val], ecx 

  CAS is about memory update but with a memory condition - 2 things 
  Trying update a memory and put a value in it 

  if(read(addr1) == y) {
  write(addr1,x)
    }

    This is 3 operations in software stack but the assembly code lock cmpxchg [shared_val], ecx will execute it as a single atomic operation, only ONE - that is why it is atomic

    This is thread safe because it is one operation and another thread cannot interrupt it.

    use this code
    
    public final int incrementAndGet() {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev + 1; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }

    This code is lock free and thread safe because it uses CAS operation and is not corrupted by other threads, worst case scenario you will just retry to fetch the data.


    Weird case:

    Atomic Interger x = 5; 

    x.add(5) --> x = 10 
    x.add(10) --> x = 20
    // there will be another thread that will call x.add(5) before x.add(20) is finished - atomic means you cannot predict the order of execution of the threads
    x.add(20) --> x = 45 // this becomes 45 and not 40, why 45? - I will get 40 if i put a reentrant lock around the add method 

    // INCREMENT doesnt care about the value but just that a number has been added to the variable - its not an add and PREDICT operation, just add - the only way to predict is through locks but in atomics we are not locking 

    Most challenging: you cannot predict the final part - other threads could have changed the value of x in between the two operations and it becomes before + 2 or before + 3 - this is the worst case scenario - you cannot predict the final value of x because other threads could have changed it in between the two operations.

    Atomic long is just a long version of atomic interger - it is a 64 bit value and the atomic interger is a 32 bit value. 
    Similar to Atomic integer, is lock free and thread safe.


    HOMEWORK: In longadder - explain how sumThenReset() is thread safe - it sums the values and resets them to 0 

    
    Imagine im scannign the array - once I finish scanning the array will be 0 - if I have a thread that changes the value of a cell during the scanning and it becomes 0 after the scan, the value will be thrown away.
    
    
 */

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicDataType_Part2 {

    int x;
    int before = x;
    int after = x.increment();

    // you can never assume that always if(after == before + 1) because another thread could have changed the value of x in between the two operations and it becomes before + 2 or before + 3 
    
    

    // this is worse than add2 because of performance O(n) due to the for loop
    // also i cannot do add(-50) 
    public final int add(int x) {
        for (int i = 0; i < 10; i++) {
            incrementAndGet();
        }
    }


    // this is better than add because of the performance O(1) 
    public final int add2(int x) {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev + x; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }

    // Where can things slow down? Imagine you have a thread and it tries to add a number but in which part there might be a delay in adding - in the while and the while will occur when there is a contention like if you are trying to add and that number has already been added by another thread - this is where the contention will occur - the more contention you have on the atomic object the slower it gets
    // if you have the object shared with more threads you get more contetion as it gets bombarded with add - everyone will try to modify the same memory address 
    // How to fix this contention problem such that the more threads we have the less this while loop have effect?  We have to remove the contention by: 
    // 1. Use a data structure - t1 and t2 are adding a and b to x = i want x + a + b. I will put a in a cell, only shared by t1, and another cell for b, only shared by t2. If t2 wants to add c as well, it will add it to the cell of t2 which is b+c. 
    // If I want to see x + a + b + c, I will just scan the data structure and add the values of the cells. Array of cells - this is a data structure that will help me to remove the contention.
    // I removed the contention, I made write faster, it becomes O(1) and the read is O(n) - this is a trade off. 
    // LongAdder is a datastructure that holds updates for you - the add() will create a cell for you and the get() will scan the cells and add them up. The sum() will scan it and return the sum of all the cells.
 
 /*
  Advantages of LongAdder:

  1. Alot of write but few reads
  2. Better performance - better time complexity by reducing the contention

  Disadvantages of LongAdder:
  
  1. More memory overload - worse space complexity

  2. Eventually Consistent - while atomic long is strictly consistent. 

  The main reason why long adder has better performance is because you are throwing away consistency - Atomic long is just one number so I just ask the memory for the value - garanteed to be consistent and correct.
   But with longadder it is not because you have to scan the datastructure 
  - Imagine while im scanning but there is a thread coming in and changes the value of a cell I havent scanned yet - NO gaurantee that the value is correct at this moment - it is eventually consistent.
  -- You always get performance for losing consistency - this is the trade off.


  Advantages of AtomicLong:

    1. Alot of reads but few writes
    2. Less memory overload - better space complexity

  Disadvantages of AtomicLong:

  1. More contention - worse time complexity by increasing the contention
  2. Slower performance - slower than LongAdder


  */
    public final int add3(int x) {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev + x; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }

    public final int multiply(int x) {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev * x; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }

    public final int divide(int x) {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev / x; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }

    public final int subtract(int x) {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev - x; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }


    
    public final int incrementAndGet() {
        int prev, next;
        do {
            prev = get(); // read current value
            next = prev + 1; // compute new value
        } while (!compareAndSet(prev, next)); // retry until successful
        return next;
    }
    
}
