package playground.week8_review.waitnotify;
/*
 Problem:

 This is a common concurrency problem where one thread (producer) generates data, 
 and another thread (consumer) processes it. Proper synchronization ensures 
 that the producer doesn't overwrite data before it's consumed, and the consumer doesn't
consume data that hasn't been produced.
 */


class SharedResource {
	private boolean available = false;
	public int x=0;

	// The synchronized made the method locked, so that only one thread can access it at a time.
	public synchronized void produce() {
		while (available) {
			try {
				wait(); // wait if already produced
				// the wait only works if the current object is locked by the current thread
				// when the thread calls wait, it releases the lock on the object  -- wake up the other threads give them the lock and continue
				// and thread goes to sleep until another thread sends notify signal

				x = x + 1; // Simulate some processing
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Produced: " + x);
		available = true;
		notify(); // notify one waiting thread - this will wake up the consumer thread
		// notifyAll() can be used to wake up all waiting threads
	}

	public synchronized void consume() {
		while (!available) {
			try {
				wait(); // wait until something is produced -- there will be a thread starvation if the thread is waiting for a signal but it never happens
				x *= 2; // Simulate some processing
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Consumed: " + x);
		available = false;
		notify(); // notify one waiting thread -- release the lock so another thread can acquire it
	}
}




// The method wait and notify and notifyALl allows you to incorporate event driven programming in java.
// The wait method is used to make a thread wait until another thread invokes notify or notifyAll on the same object.
// The notify method wakes up a single thread that is waiting on the object's monitor.
// The notifyAll method wakes up all threads that are waiting on the object's monitor.
// The wait, notify, and notifyAll methods must be called from a synchronized context (i.e., within a synchronized block or method).
// The wait method releases the lock on the object and puts the thread into a waiting state.
// The notify and notifyAll methods do not release the lock on the object; they simply wake up waiting threads.
public class Main {
	public static void main(String[] args) {
		SharedResource resource = new SharedResource();

		// produce some data, then consume will print it - if data is available then print it
		// the consume thread needs to wait until the produce thread has produced something
		// the produce thread also needs to wait until the consume thread has consumed something 
		Thread producer = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				resource.produce();
			}
		});

		Thread consumer = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				resource.consume();
			}
		});

		producer.start();
		consumer.start();
	}
}
