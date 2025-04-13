package playground.week8_review;

public class Main {
    public static void main(String...args) {
        new Thread(() -> {
            System.err.println("Hello from SE422");
        }).start(); // this is a thread created using lambda expression which is a shortcut for creating an instance of a class that implements the Runnable interface.


        

    }    
}
