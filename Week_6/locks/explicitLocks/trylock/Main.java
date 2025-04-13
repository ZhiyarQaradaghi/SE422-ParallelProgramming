package trylock;

import java.util.concurrent.locks.*;

/*
Deadlocks occur due to circular waiting and can be avoided using mechanisms like tryLock().
Starvation happens when threads are unfairly denied access to resources.
Consistent lock acquisition order reduces the risk of deadlocks.
Fair locks can prevent starvation but may reduce performance.
Always release locks in a finally block to avoid resource leaks.
 */

class SharedResource{
	public static final ReentrantLock LOCK1= new ReentrantLock();
	public static final ReentrantLock LOCK2= new ReentrantLock();
}


// By using tryLock(), threads can avoid deadlocks because they can check if a lock is available before proceeding. 
// If a thread cannot acquire a lock, it can release any other locks it holds and retry later.
/*
 Example of a deadlock:

 if ThreadOne acquires LOCK1 and ThreadTwo acquires LOCK2, both threads will 
 wait indefinitely for the other lock to be released. This creates a circular dependency, leading to a deadlock.
 
 Example:
    ThreadOne holds LOCK1 and waits for LOCK2.
    ThreadTwo holds LOCK2 and waits for LOCK1.
    Neither thread can proceed, resulting in a deadlock.


Starvation occurs when a thread is perpetually denied access to resources because other threads with higher priority or frequent access dominate resource usage.

How Starvation Can Occur:
If one thread repeatedly acquires both locks before the other thread gets a chance, the second thread may experience starvation.
In this code, starvation is less likely because both threads release their locks and retry, giving the other thread a chance to acquire the locks.
Difference from Deadlocks:
In a deadlock, no thread can proceed.
In starvation, some threads proceed while others are indefinitely delayed.

 */
class ThreadOne extends Thread{
	@Override
	public void run(){
		boolean init = true;
		while(true){

			// Making the two threads out of sync
			// if(!init){
			//	Random r = new Random();
			//	try{Thread.sleep(r.nextInt(100));}catch(Exception ex){}
			// }else{
			//	init = false;
			// }

			if(SharedResource.LOCK1.tryLock()){
				try{

					System.out.println("ThreadOne: Acquired LOCK1...");

					try{Thread.sleep(100);}catch(Exception ex){}

					if(SharedResource.LOCK2.tryLock()){
						try{
							System.out.println("ThreadOne: Acquired LOCK2...");

							System.out.println("ThreadOne: The thread has the two locks");
							return;
						}finally{
							SharedResource.LOCK2.unlock();
						}
					}

				}finally{
					SharedResource.LOCK1.unlock();
					System.out.println("ThreadOne: Release LOCK1!");
				}
			}
		}
	}
}


class ThreadTwo extends Thread{
	@Override
	public void run(){
		while(true){
			if(SharedResource.LOCK2.tryLock()){
				try{

					System.out.println("ThreadTwo: Acquired LOCK2...");

					try{Thread.sleep(100);}catch(Exception ex){}

					if(SharedResource.LOCK1.tryLock()){
						try{
							System.out.println("ThreadTwo: Acquired LOCK1...");

							System.out.println("ThreadTwo: The thread has the two locks");
							return;
						}finally{
							SharedResource.LOCK1.unlock();
						}
					}

				}finally{
					SharedResource.LOCK2.unlock();
					System.out.println("ThreadOne: Release LOCK1!");
				}
			}
		}
	}
}

public class Main{
	public static void main(String...args){
		Thread t1 = new ThreadOne();
		Thread t2 = new ThreadTwo();

		t1.start();
		t2.start();

	}
}
