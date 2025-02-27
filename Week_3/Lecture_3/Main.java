public class Main {
    public static void main(String[] args) {
        System.out.println("Main Start");

        // --- First way 
        // Thread plt1 = new PrintLoopTask();
        // plt1.start(); 

        // if you do not have to use the object plt1 again after start then use the below approach 
        // new PrintLoopTask().start(); only if you do not need the object pointer after calling the start
        

        // --- Second way 

        // create the class PrintLoopTask on the fly using Anonymous class 
        // Thread t1 = new Thread(){ // hey java create a subclass and make it extend Thread - dont care about the name
        //     @Override
        //     public void run() {
        //         for (int i = 0; i <= 5; i++) {
        //             System.out.println("Number : "+i);
        //         }
        //     }
        // };
        // t1.start();

        // which to use? Rule: If i have a task and i want to push that task into another thread but it never repeats so im only going to create that task once and im not going to do it again
        // The first approach is good if you want to launch that same thread again - repeats again 
        // Anonymous class is not good for repetition because i will have to redeclare it again if i use it again
        

        // Interface Runnable implementation
        // new Thread(new PrintLoopTask()).start(); 
        
        // Create Runnable Anonymous class
        Runnable r = new Runnable() { // java creates a class and makes it implements interface Runnable 
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Number: "+i);
                }
            }
        };

        // shorter 
        new Thread(new Runnable() { // first thing java will do is create an object of Runnable with data type Anonymous class that implements Runnable, once class is constructed,
                                    // it will create an object and give it as an argument to the constructor of Thread, and once the Thread object is created then the start() is going to be called
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Number: "+i);
                }
            }
        }).start();


        // Modern style - Java 15 + 
        // Lambda allows you to create anonymous classes with a few symbols 

        Runnable object = () -> { // hey java we need an anonymous class, the data type is runnable, and the code for the method that does not take any argument is inside the below scope 
            for (int i = 0; i < 10; i++) {
                System.out.println("Number "+i);
            }
        };

        new Thread(object).start();

        // better way :

        // it knows how to override the method run because there is only one method that has no argument - if there are 2 methods then it will not work
        // how does the lambda operation know this is Runnable?? How many constructors in the Thread class takes one object? 2, and how many of them has one method? Runnable type 
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Number "+i);
            }
        }).start();




        // when shall i use inheritence and when shall i use interface?
        /*
            It depends on programming style - for Yad 98% of the time uses interface 
                1. Hates inheritence  
                2. Testability - the PrintLoopTask if it uses interface then its only one method - but the inheritence introduces all the inherited methods with it 
         */



        System.out.println("Main End"); // side affect of multi threading: when you have 2 independant execution paths - you will have different inconsistent results 
        /*
            Main Start
            Main End             side affect: main end runs before the loop
            Number: 1
            Number: 2
            Number: 3
            Number: 4
         */
    }
}
