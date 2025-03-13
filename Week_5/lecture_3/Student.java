package Week_5.lecture_3;

import java.util.concurrent.locks.ReentrantLock;


public class Student {
    private int id;
    private float gpa;

    // avoid this because you are avoiding all the advantages of primitive types - big performance hit
    // private Integer id;
    // private Float gpa;

    // reentrant lock is a lock that can be acquired multiple times by the same thread without causing a deadlock  
    // if a thread has acquired a lock and it tries to acquire it again, it will be able to do so without blocking itself 
    // this is private because i want to encapsulate the lock, i don't want the others to have access to the lock
    private final ReentrantLock idLock, gpaLock;

    // this is better because i can override and better for testing
    public Student() {
        // 1st method
        idLock = new ReentrantLock();
        gpaLock = new ReentrantLock();

        // 2nd method
        idLock2 = new Object();
        gpaLock2 = new Object();
    }

    // now 2 threads can access the id and gpa at the same time
    // public synchronized  int getId() {
    //     try {
    //         idLock.lock();
    //         return id;
    //     } finally {
    //         idLock.unlock();
    //     }
    // }

    
    // public synchronized  float getGpa() {
        //     try {
            //         gpaLock.lock();
            //         return gpa;
            //     } finally {
                //         gpaLock.unlock();
                //     }
                // }

    private Object idLock2, gpaLock2;
                
    // if i have a thread calling the gpa, the synchronize will deal with the gpaLock object to lock and unlock 
    // what you should NOT do! Putting synchronized as signature of the method 
    /*
     1. Locking the entire method
     2. No choice what to lock, meaning will always be this object
     3. Exposing too much informaton to the other classes - Student class should be responsible for locking and unlocking - why are you giving the details of what kind of locks you are using to the other classes
     */
    public int getId() {
        synchronized (idLock2) {
            return id;
        }
    }

    public float getGpa() {
        synchronized (gpaLock2) {
            return gpa;
        }
    }


    public void setId(int id) {
        synchronized (idLock2) {
            this.id = id;
            
        }
    }

    public void setGpa(float gpa) {
        synchronized (gpaLock2) {
            this.gpa = gpa;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", gpa=" + gpa +
                '}';
    }
}

// if the student is thread safe, will it make its data structure thread safe? The data structure is not thread safe, maybe i will have a thread that is trying to scan the array but in the middle of the scan another thread will come in and change a few objects at the same time
// if i have 2 threads and they manipulate the id then they should block each other out, but if one is using id and the other is using gpa then they should not block each other out
class Students {
    Student[] students = new Student[100];

}

class Main {
    public static void main(String[] args) {
        Student student = new Student();
        System.out.println(student.getId());
    }
}