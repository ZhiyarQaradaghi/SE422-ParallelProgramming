package playground.week8_review.countdownlatch;

import java.util.concurrent.CountDownLatch;



/*  
you do not have control over the speed of the threads
Most difficult part of multi-threading is causality propagation 
Tools help us to keep track of causality - these tools are called "thread synchronizors" 
-- already know 2 synchronizors:
1. join() - wait for a thread to finish (a thread to run after another thread) -
t3 will go into sleeping mode until t1 is done then t3 will wake up and execute -- 
the waiting creates the happens -before relationship

-- problem with join(): imagine if i want a part of the thread to run in parallel with another part of the thread
-- join() is not a good solution for this problem --- because everything must be executed in one thread for join()
   to work and wake up the other thread

   So the problem is: how to let t3 run after some parts of t1 are done but not all of them? 
   Lets use the wait and notify methods to solve this problem

2. Wait and Notify methods -  

	Use an object and share it between the threads
	t3 will go on sleeping mode by calling wait() on the object
	t1 will call notify() on the object when it is done with the part of the thread that t3 needs to run after
	t3 will wake up and execute 

	When t1 calls notify, t1 owns the object by locking it
	When t3 calls wait, t3 owns the object by locking it
	When t1 calls notify, t1 will release the lock on the object and t3 will acquire the lock on the object

	This is a problem because parallelism is not guaranteed due to the locking mechanism
	with the synchronization when calling wait and notify

	Challenging part: The sleep itself is not the only factor that affects the causality propagation, but also the locks 

	When wait and notify dont work and cause thread starvation: 

	A thread can only wait for one signal at a time
	
	So we willl use a countdown latch to solve this problem:
	Designed where you want multiple threads to execute but you have certain parts of code that must run after each other

	Imagine we have a system to build a rocket - the launcher is the thread responsible for igniting the rocket and stuff 

	But before the rocket is ignited, we need to check the fuel system, navigation system and weather check 

	!!!! IMPORTANT - I want to build my system such that the launcher thread will wait for the other threads to finish 
	their work before it can ignite the rocket

	We have a class called CountDownLatch - it is a synchronizer that allows one or more threads 
	to wait until a set of operations being performed in other threads completes

	The main methods of the CountDownLatch class are:
	1. countDown() - Decreases the count of the latch by 1
	2. await() - Causes the current thread to wait until the latch has counted down to zero, 
		which means that all the threads have finished their work
	3. getCount() - Returns the current count of the latch
	4. reset() - Resets the latch to the initial count
	5. isCountedDown() - Returns true if the latch has counted down to zero, false otherwise
	

*/

public class Main {

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(3); // countdown starts at 3

		new Thread(new Launcher(latch)).start();

		// Worker threads
		new Thread(new Worker(latch, "Fuel System")).start();
		new Thread(new Worker(latch, "Navigation System")).start();
		new Thread(new Worker(latch, "Weather Check")).start();

	}
}

class Worker implements Runnable {
	private CountDownLatch latch;
	private String name;

	public Worker(CountDownLatch latch, String name) {
		this.latch = latch;
		this.name = name;
	}

	public void run() {
		System.out.println(name + " is preparing...");
		try {
			Thread.sleep((long) (Math.random() * 3000)); // Simulate work
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name + " is ready!");
		latch.countDown(); // Decrease the count - this will decrease it by 1 for every thread that is done
		// When the count reaches 0, the latch will be released and the main thread will continue
		// The main thread will wait until all the threads are done
		
	}
}

class Launcher implements Runnable {
	private CountDownLatch latch;

	public Launcher(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run(){
		try {
			System.out.println("Main thread waiting for systems to be ready...");
			latch.await(); // Waits until the count reaches 0 --
			// this thread is going to go to sleeping mode until the value of the latch is 0
			// this launcher is gauranteed to wake up and run after the other threads are done (latch = 0)
			System.out.println("All systems ready. Launching rocket!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

