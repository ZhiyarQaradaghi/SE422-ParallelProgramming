package Week_4.Lecture_2;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello from Main");

        final Info i = new Info();
        i.sid = 10000;

        System.out.println("Main SID: "+i.sid);

        // Thread t1 = new Thread(new Task1(i));
        // t1.start();

        // int id = 2004;
        // Thread t2 = new Thread(new Task1(id));
        // t2.start();



        // you cannot have constructors in anonymous classes 
        // Thread t3 = new Thread() { // the scope of the anonymous class is shared with its creator - this anonymous class can see everything the Main can see 
        //     @Override // limitation - if you later on change the value of the pointer - this wont be allowed - trying to acceess I but the pointer I is changing 
        //     public void run() {
        //         while (true) { 
        //             System.out.println("Anoynmous class Thread: "+ i.sid);
                    
        //         }
        //     }
        // };
        // t3.start();

        Thread t4 = new Thread(() -> {
            System.out.println("Lambda Class: "+i.sid);
        });
        t4.start();

        Thread.sleep(5000);
        // id = 500;
        System.out.println("Bye from Main");
    }    
}