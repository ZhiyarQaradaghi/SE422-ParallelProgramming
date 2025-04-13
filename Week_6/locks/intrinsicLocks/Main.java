
import java.util.concurrent.locks.ReentrantLock;



class SharedCounter {
	private int count = 0;

	// reentrant lock - implicit lock - you don't have to unlock it yourself - the JVM will do it for you each time you leave the synchronized block {}
	public void increment() {
		// count++; // Not thread-safe
		synchronized(this){
			count++;
		}
	}

	public int getCount() {
		return count;
	}
}

public class Main {
	public static void main(String[] args) {
		SharedCounter2 counter = new SharedCounter2();

		// Thread 1
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				// System.out.println("Thread 1 incrementing");
				counter.increase();
				// counter.getCount();
				// counter.increment();
			}
		});

		// Thread 2
		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				// System.out.println("Thread 2 incrementing");
				counter.increase();
				// counter.getCount();
				// counter.increment();
			}
		});

		t1.start();
		t2.start();

		try {
			t1.join(); // this will make the main thread wait for t1 to finish
			t2.join(); // this will make the main thread wait for t2 to finish
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Final count: " + counter.getCount());
	}
}


// reentrant lock - explicit lock - you have to unlock it yourself - call unlock each time you lock it
class SharedCounter2 {
	private int count = 0;
	ReentrantLock lock = new ReentrantLock();

	public void increase() {
		lock.lock(); 
		try {
			count++;
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		// synchronized (this) {
		// }
	}

	public int getCount() {
		return count;
	}
}