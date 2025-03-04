package Week_4.Lecture_2;

public class Task1 implements Runnable {

    public Info obj;
    public Task1(Info arg) {
        this.obj = arg;
    }

    int id;
    public Task1(int arg) {
        this.id = arg;
    }

    @Override
    public void run() {
        System.out.println("Task1: "+ "hello");

        while (true) { 
            // System.out.println("Task1 SID: "+obj.sid);
            System.out.println("Task1 ID: "+id);
            try {
                Thread.sleep(500); // put the current thread to sleeping mode 
            } catch (Exception ex) {
    
            }
        }
    }
}
