package Week_12.Lecture_2;

import java.util.concurrent.atomic.AtomicReference;

/* Ref: Java Chap 5 - immutables 
 -- How immutables interact with pointers 
 -- How immutables interact with threads 

    We will have a custom class or a data structure that is mutable in between the threads and the immutable object



    HOMEWORK Question:

    if I have another property in Student class

    private final Scanner scanner;

    and initialized it in the constructor
    public Student(int id, double gpa) {
        this.id = id;
        this.gpa = gpa;
        this.scanner = new Scanner(System.in);
    }

    is this scanner object immutable or mutable? Scanner is mutable so the Student object becomes mutable.

    if you do it with String class, then its immutable 
 */
public class ImmutableObjects_Part2 {
    public static void main(String[] args) {
        Student s = new Student(100, 3.5);
        /*
          s---> object
         */
        Student old = s; // Pass by value - copying the memory address of the object not the reference of the pointer 
        /*
          s------> object (both pointing to the same object)
               |
          old---
         */

        // s.setId(100);
        /*
          old---> object

          s------> object (id 100 but diffrent memory address - new object)
               
         */

        // if(s == old) { // whether s and old are pointing to the same object
        //     System.out.println("s and old are the same object");
        // } else {
        //     System.out.println("s and old are different objects");
        // }




        // Thread t1 = new Thread1(s);
        // /*
        //  obj ---> same object s and old are pointing at 

        //     s---> object (all pointing to the same object)
        //         |
        //     old---
        //         |
        //     t1 obj---
        //  */
        // Thread t2 = new Thread2(s);

        //  /*
        //  obj ---> same object s and old are pointing at 

        //     s---> object (all pointing to the same object)
        //         |
        //     old---
        //         |
        //     t1 obj---
        //         |
        //     t2 obj---
        //  */

        // DB.ptr = s; 
        DB.ptr.set(s);
        Thread t1 = new Thread1();
        Thread t2 = new Thread2();

        t1.start();
        t2.start();
        
        

    }
}

class Thread1 extends Thread {
    Student obj;

    public Thread1(Student arg) {
        this.obj = arg;
    }

    public Thread1() {
        this.obj = DB.ptr.get(); // get the object from the database
    }

    @Override
    public void run() {
        // obj = obj.setGpa(obj.getGpa() + 1); // object t1 will point at a new object with new gpa -- the change will not propagate to the old object and other threads 
        // obj = DB.ptr;
        // obj.getGpa();
        // DB.ptr = obj = obj.setGpa(obj.getGpa() + 1); // now evey other thread that are using the DB class will see this new object;
        // // why not DB.ptr = obj.setGpa(obj.getGpa() + 1); // because if we want to use it again it will have the old values



        // hypothetical situation:
        Student old;
        old = obj = DB.ptr.get(); // 2 local pointers pointing at the latest object from system level

        obj = obj.setGpa(3.8);

        // if(DB.ptr == old) {  // make sure that the ptr is still the same and hasnt been changed by another thread at system level
        //     DB.ptr = obj;  // this is not thread safe because these 2 lines need to be in one operation, because maybe the if condition is true but right after that line a thread will change the ptr 
        //     // solution: CAS -- AtomicReference class -- offers CAS operations for object pointers 
        // }

        // solution:
        DB.ptr.compareAndSet(old, obj); // compare the old value with the new value and if they are the same then set the new value to the ptr -- this is atomic operation and thread safe
    }
}

class Thread2 extends Thread {
    Student obj;

    public Thread2() {
        this.obj = DB.ptr.get(); // get the object from the database
    }
    public Thread2(Student arg) {
        this.obj = arg;
    }



    @Override
    public void run() {
        
    }
}

class DB {
    // We dont need this anymore because we need CAS-- public static volatile Student ptr; // volatile to force every thread to read the value from the main memory and not from the cache when the address of ptr is changed 
    public static AtomicReference<Student> ptr = new AtomicReference<>(); 
    /*
     ptr points to an object of AtomicReference class, inside the AtomicReference class there is a pointer to the student Object

     ptr ---> object of AtomicReference class (pointer) ---> object of Student class
     */
}

class Student {
    private final int id;
    private final double gpa;
    private final Department dept;

    public Student(int id, double gpa) {
        this.id = id;
        this.gpa = gpa;
        this.dept = null; // default value
    }

    public Student(int id, double gpa, Department dept) {
        this.id = id;
        this.gpa = gpa;
        this.dept = dept;
    }

    public int getId() {
        return id;
    }

    public double getGpa() {
        return gpa;
    }

    public Department getDept() {
        return dept;
    }

    public Student setGpa(double gpa) {
        return new Student(this.id, gpa, this.dept); 
    }

    public Student setId(int id) {
        return new Student(id, this.gpa, this.dept); 
    }

    public Student setDept(Department dept) {
        return new Student(this.id, this.gpa, dept); 
    }

    

}


class Department {
    int deptId;
    String deptName;
    public Department(int deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }
}