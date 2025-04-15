package Week_10.Lecture_2;
// They are custom because they are created by the user meaning that the user can define the number of threads, the maximum number of threads, 
// and the minimum number of threads. The custom thread pool can be used to execute tasks in parallel, which can improve performance and 
// reduce the overhead of creating and destroying threads.

/*
 Custom Thread Pools 

How do thread synchronizers play with ThreadPools?

Creating a fixed thread pool:
ExecutorService executor = Executors.newFixedThreadPool(4);

What is the class Executors? 
The Executors class is a factory class that provides static methods for creating different types of thread pools.

ExecutorService is an interface that provides methods for managing and controlling the execution of tasks in a thread pool.
The newFixedThreadPool method implements the ExecutorService interface and creates a thread pool with a fixed number of threads.

Internal Mechanism of ExecutorService:
The ExecutorService has task submitters, which are used to submit tasks for execution, a task queue that holds the tasks, and a thread pool that executes the tasks.
The process for these 3 works in this way: 
1. The task submitter submits a task to the task queue.
2. The task queue holds the task(Runnable Object) until a thread from the thread pool is available to execute it.
3. The thread pool executes the task by giving it to a thread from the pool and returns the result to the task submitter.

Problem with Fixed Thread Pool:
Fixed threads so if 4 then it remains 4. 
If a thread is free, it will take the task from the queue and execute it. The condition: The thread must be free then we can attach a runnable object to it.

What does it mean if a thread is free? 
A thread is busy when it performs any runnable object, regardless of what the runnable object is doing.
If a thread is free, it means that the thread is not executing any task and is available to take on a new task.

Thread being busy has nothing to do if you are using the CPU, it just means do i have a runnable object attached to the thread or not.

I can have 5 threads that are not doing anything in the CPU, but if they are not attached to a runnable object, they are not busy.

If the run() method is not executing, then the thread is not busy - it is free.
Whatever you put in the run() method will make the thread busy.

One example that can tank the business of a thread is with Thread Synchronizers.

Imagine i create a pool of 4 threads, and all of them are waiting for a signal to wake up but it never shows up so we have thread starvation as all the threads are busy.
If we submit another runnable object to the queue, it will not be executed until one of the threads in the pool becomes free.
From your end, it will be added to the queue but will not be executed until one of the threads in the pool becomes free. 

The solution for the above is to use a custom thread pool - more dynamic - based on certain conditions we will add threads or terminate them. Usually really good when we are using thread synchronizers.
The main class we will use for custom thread pool is ThreadPoolExecutor. 
It implements the ExecutorService interface and provides methods for managing and controlling the execution of tasks in a thread pool.
Example:
ExecutorService executor = new ThreadPoolExecutor(
    corePoolSize, // the number of threads to keep in the pool, even if they are idle - how many threads should we have if the pool is not under stress - normal conditions
    maximumPoolSize, // the maximum number of threads to allow in the pool - how many threads should we have if the pool is under stress - maximum conditions 
    keepAliveTime, // when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating
    TimeUnit.SECONDS, // the time unit for the keepAliveTime parameter
    new LinkedBlockingQueue<Runnable>() // the queue to use for holding tasks before they are executed - this argument asks for a type of queue, we can use any type of thread safe data structure
    // the queue is a blocking queue, which means that if the queue is full, the thread will wait until a thread becomes free to execute the task
);

Example with ThreadPoolExecutor:
 		ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        4 core threads, 6 non core threads, 60 seconds of keep alive time, and a blocking queue.
        It will first create 4 threads, and if the pool is under stress, it will create up to 10 threads. It will keep the extra 6 threads alive for 60 seconds before terminating them.
        
The difficult part is how can i play with these arguments and what are the side effects of each of them. 
If I set the core pool size to 4, we will add a thread under a condition 
1. if all the current threads are busy executing runnable objects.
2. You submitted a task to the queue and the queue is full - we reached its maximum capacity  

So both side is full, we will keep adding threads until we reach the maximum pool size.

Another case (why i want to use blocking queue):
I have the maximum capacity of 10 threads, and the task queue is maxed out, and all the threads are busy,
the linked blocking queue will put the threads in task submitters to wait until a thread becomes free because it is a blocking queue.

There is no hard limit on the size of the queue, but I will never add more than the maximum pool size of threads unless i set the core pool size to be greater than the maximum pool size.
// 		ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20));
The 20 is the maximum capacity of the queue, so if we reach that limit, we will start adding threads until we reach the maximum pool size of 10 threads.
Once I reach 20 runnable objects in the queue, then the thread pool will start adding threads until it reaches the maximum pool size of 10 threads.

The most aggressive queue towards creating threads is - SynchronousQueue<Runnable>() - it has no capacity,
 so if you submit a task and all the threads are busy in the pool then you immediately push the threadpool to create a new thread in the pool 
 - more rapidly willing to expand.

 new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
 -- This is a fixed Thread Pool with 10 threads, and it will not create any new threads.


 new ThreadPoolExecutor(0, 10, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
 -- This means I can rapdily expand as I submit tasks to the queue, and it will create new threads until it reaches the maximum pool size of 10 threads.
 -- I will wait 60 seconds to terminate the threads, and if the queue is empty, it will terminate all the threads in the pool.

 new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
 The max value implies that the pool can grow indefinitely, and it will not terminate the threads in the pool until the queue is empty - up to 2 million threads.
 -- This is good for reusing threads, if i created 2 threads and within the 60 seconds I have tasks submitted then I will give them the tasks.
 -- This threadpool cannot be effected by thread synchronizers because it will just create new threads - 
 -- When to use it? WHen I have alot of tiny tasks that are not CPU intensive and I want to reuse the threads. Tiny tasks that use a couple of seconds.  
 -- But not good if i have alot of long running tasks because it will create a lot of threads and consume a lot of memory.
 -- No hardware limitation because of context switching. 

 In synchronous queue, will always slow down the task submitters the moment it reaches the maximum pool size.

-- the above is already implemented as newCachedThreadPool() 

 newCachedThreadPool() - it is a cached thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available.
 implementation: it returns this threadpoolexecutor: 
    new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

*/
public class Main {
    public static void main(String[] args) {
        // ExecutorService executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20));
    }
}
