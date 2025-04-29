package Week_12.Lecture_1;

/*
  Java Textbook Chapter 5 - Immutables
  
  What does it mean for an object to be immutable?
  Properties and Limitations of Immutable Objects

  Atomic Classes - lock free yet thread safe
  Immutable Objects is another way to achieve lock free yet thread safe

  An object is immutable when we cannot change its properties after it has been created. No mutations can be performed on the object. Read only.

  You have an object that you cannot change, if that object is also pointing at other objects then those cannot be changed either.


  Advantages of Immutable Objects:

  If you shared that object with multiple threads: t1, t2, t3 - they can only read the object, they cannot change it. -- Thread safe and lock free since there are no writes operations.

  -- Space Complexitiy increases - more memory is used to store the object since we cannot change it. 
  -- Modifications are expensive - if we want to change the object we have to create a new object and copy the properties of the old object to the new object.
  -- This is too expensive for large objects - if the object is small then it is not a problem.
  -- Code complexity, we have to write more code to create the new object and copy the properties of the old object to the new object.


 */
public class ImmutableObjects {

    public static void main(String[] args) {
        Student s = new Student(1, 3.5);

        s = s.setGpa(3.2).setId(5);


        






        // before making the object immutable: 
        // // thread 1 will change the id to 100
        // s.id = 100;

        // // thread 2 will change the id to 200
        // s.id = 200; // mutable object, we can change the properties of the object
    }
}


class Student {
    private final int id;
    private final double gpa;

    public Student(int id, double gpa) {
        this.id = id;
        this.gpa = gpa;
    }



    public int getId() {
        return id;
    }

    public double getGpa() {
        return gpa;
    }

    public Student setGpa(double gpa) {
        return new Student(this.id, gpa); // create a new object with the new gpa
        // this.gpa = gpa; // cannot change the gpa since it is immutable
    }

    public Student setId(int id) {
        return new Student(id, this.gpa); // create a new object with the new id
        // this.id = id; // cannot change the id since it is immutable
    }
}
