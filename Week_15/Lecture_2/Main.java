package Week_15.Lecture_2;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/*
  VarHandle RECAP 

  How can we use objects of VarHandle to implement Volatile
 */
public class Main {
    static int length = 9;
    static int arr[][] = new int[length][10_000_000];
    
    private int data;
    private static final VarHandle DATA_VARHANDLE; 

    // a static block, a constructor that will be called only once 
    static {
        try {
            // first argument - what is the class that this variable exists in
            // what is the name of the variable you want to attach this varhandle to
            // what is the type of that variable
            DATA_VARHANDLE = MethodHandles.lookup().findVarHandle(Main.class, "data", int.class);
            
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void setData(int value) {
        // this.data = value;
        // DATA_VARHANDLE.set(this, value); // this will do the same as the above one, first arg is to indicate which object and 2nd arg is for which value -- varhandle already attached to data so no need to tell it
        // this is a plain set -- the compiler can reorder and remove the write 


        // DATA_VARHANDLE.setOpaque(this, value); // make sure write will be performed - no code elimination BUT reordering is allowed
        // this garuantees that this write will actually happen but it can be reordered

        // DATA_VARHANDLE.setRelease(this, value); // a release fence (STORE STORE and LOAD STORE) fences will be added to this write -- all the write will be pushed to memory then moving
        
        DATA_VARHANDLE.setVolatile(this, value); // 3 fencces, release fences (STORE STORE and LOAD STORE) and STORE LOAD 
        // THIS will make this write operation volatile, you can change the behavior to other sets in different parts of code (like outside of loop ...etc)


        DATA_VARHANDLE.get(this, value);
        DATA_VARHANDLE.getAcquire(this, value);
        DATA_VARHANDLE.getVolatile(this, value);

        DATA_VARHANDLE.compareAndExchange(arr);
        DATA_VARHANDLE.compareAndExchangeAcquire(arr);
        DATA_VARHANDLE.compareAndExchangeRelease(arr);

        // atomic classes ALL are using varhandle to achieve their operations - it is handling all the atomic nature in Java 
        /*
         VARHANDLE is extremely useful when you want to create a lock free where you dont need locks yet you need thread safety because it gives you fences
          and will give you volatile behavior at any given time and the compare and exhcnage operations (atomic behavior)

          HOMEWORK:

          write this pseudocode using first with: VARHANDLE (attaching 2 varhandles, what kind of set and get you need to recreate the behavior) and ATOMIC classes

          ALL THE write operations should be removed with set and get methods 


            public void lock(int t) {
            int other = 1-t;
            flag[t]=1;
            while (flag[other] == 1) {
                    if(turn == other) {
                        flag[t]=0;
                        while(turn == other);
                        flag[t] =1;    
                    
                    }
                }
            }
         */
    }
    public static void main(String[] args) {

        int i,j=0;
        for (i = 0; i < length; i++) {
            for (j = 0; j < arr[i].length; j++) {
                arr[i][j] = 2*i; // the reordering of this write and the j++ will not happen with storestore 
                // VarHandle.storeStoreFence(); // free of charge in x86 HARDWARE -- but not in JVM and Compiler <--- application will be a bit slower because restricting reordering in JVM and Compiler 
                
            }
        }

        System.out.println(i);


    }
}
