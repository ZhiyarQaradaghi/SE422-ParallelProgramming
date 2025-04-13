package playground.week8_review.locks.explicitLocks;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// ReentrantReadWriteLock, Deadlock and Starvation
// ReentrantReadWriteLock is a special type of lock that allows multiple threads to read a resource concurrently
// but only one thread to write to it at a time. It has two locks: a read lock and a write lock.
// The read lock can be held by multiple threads at the same time, while the write lock can only be held by one thread.
// This can lead to a situation called "starvation" where a thread that wants to write to the resource is unable to do so
// because there are too many threads holding the read lock. This can happen if the read lock is held for a long time
// or if there are too many threads trying to read the resource at the same time.
// In this example, we will create a class called Student that has a read and write lock for the id field.
// We will create two tasks that will try to read and write to the id field at the same time.
// The first task will read the id field and set it to -1 if it is 0.
// The second task will read the id field and set it to 2 if it is 0.
// This will create a situation where the first task is trying to read the id field while the second task is trying to write to it.
// This can lead to a situation called "starvation" where the first task is unable to read the id field
// because the second task is holding the write lock.
// This can happen if the read lock is held for a long time or if there are too many threads trying to read the resource at the same time.
public class LockStarvation {
    public static void main(String[] args) {

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores);

        Student s1 = new Student();

        Runnable task1 = (()->{
            System.out.println("Task1 Student ID: "+s1.getId());
			System.out.println("Setting ID to 1");
			s1.setId(1);
			System.out.println("Task1 Student ID: "+s1.getId());
        });

		Runnable task2 = (()->{
			System.out.println("Task2 Student ID: "+s1.getId());
			System.out.println("Setting ID to 2");
			s1.setId(2);
			System.out.println("Task2 Student ID: "+s1.getId());
		});

        pool.execute(task1);
		pool.execute(task2);
    }    
}

// The readlock needs to be unlocked before the write lock can be acquired
// but the writeLock doesnt need to be unlocked before the read lock can be acquired
// The read lock can be held by multiple threads at the same time, while the write lock can only be held by one thread.
// This can lead to a situation called "starvation" where a thread that wants to write to the resource is unable to do so
// because there are too many threads holding the read lock. This can happen if the read lock is held for a long time
// or if there are too many threads trying to read the resource at the same time.
class Student{
	private int id;

	private final ReentrantReadWriteLock idLock;
	// private final ReentrantLock idLock, gpaLock;
	// private Object idLock = new Object();
	// private Object gpaLock = new Object();

	public Student(){
		idLock = new ReentrantReadWriteLock();
		// gpaLock = new ReentrantLock(true);
	}


	public int getId(){
		// synchronized(idLock){
		//	return id;
		// }

		try{
			idLock.readLock().lock();
			if(id==0){
			idLock.readLock().unlock();
			idLock.writeLock().lock();
				id = -1;
			idLock.writeLock().unlock();
			idLock.readLock().lock();
			}
			return id;
		}finally{
			idLock.readLock().unlock();
			// idLock.writeLock().unlock();
		}
	}


	public void setId(int arg){
		try{
			idLock.writeLock().lock();
			id = arg;
		}finally{
			// idLock.readLock().lock();
			idLock.writeLock().unlock();
		}
	}

}
