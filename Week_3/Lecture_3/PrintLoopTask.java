
// Whenever i have the word Task with the class name then i know this class will have its own thread 
public class PrintLoopTask implements Runnable { // problem: imagine if PrintLoopTask already inherits from another class - you cannot inherit from 2 classes 
                                                 // To solve the problem - use the interface Runnable

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) { 
            System.out.println("Number: "+i);
        }
    }
}
