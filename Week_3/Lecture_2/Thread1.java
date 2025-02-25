package Week_3.Lecture_2;

public class Thread1 extends Thread{

    @Override
    public void run() {

        try {
            while (true) { 
                System.out.println("Loop 1");
                Thread.sleep(100);
            }

        } catch (Exception ex ) {

        }
    }
    
}
