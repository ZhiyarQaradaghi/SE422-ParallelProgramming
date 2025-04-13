package playground.week8_review.locks.explicitLocks;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        
        Integer arr[] = new Integer[750];
        ReentrantLock locks[] = new ReentrantLock[arr.length];

        ReentrantLock l1, l2;

        l1 = locks[0];
        l1.lock();

        for(int i=0; i < arr.length-1; i++){
        
        	l1 = locks[i];
        	l2 = locks[i+1];
        
        	l2.lock();
        	Integer e1 = arr[i];
        	Integer e2 = arr[i+1];
        	System.out.println(e1*e2);
        	l1.unlock();
        
        	// synchronized(e1){
        	//	synchronized(e2){
        	//		System.out.println(e1*e2);
        	//	}
        	// }
        
        }

        // l2.unlock();

    }
}
