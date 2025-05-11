package Week_11.Lecture_2;


import java.util.concurrent.atomic.AtomicInteger;

/*
 Atomic operations means this operation cannot be split - it has atomicity 

 Examples: Transactions in database 

 Either the whole thing will execute or none will - no partial 

 If you have an integer called x, and made it x = 5 - atomic 
 If you later tried to print(x) - atomic 
 x * 2 <-- not atomic because here you are getting value of x and also multiplying by 2 - 2 operations 
 x++ <--- not atomic - 3 operations, get value x, add 1 to it, and write it to memory


 If an operation is atomic then OFTEN the operation is thread safe - no locks no reentrant locks no volatile ... none needed  

 compareAndSet() -- update only if thread safe -- if no other thread changed that value while executing -- if the value is the same as the value in memory then it will set otherwise it returns false
 
 CAS -- check the value before you write it -- if matches what is expected then write it as a single atomic operation
 */
public class AtomicInteger {
	private static AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			new Thread(() -> {
				count.incrementAndGet();
				count.decrementAndGet();
			}).start();
		}

		// Wait a bit for all threads to finish (simplified)
		try { Thread.sleep(1000); } catch (InterruptedException e) {}

		System.out.println("Final count: " + count.get());
	}
}
