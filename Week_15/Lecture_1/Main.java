package Week_15.Lecture_1;

import java.lang.invoke.VarHandle;

/*
 Volatile -- adds 5 fences

 How to introduce fences without using volatile?
 Using VarHandle --- VarHandle allows you to use fences manually - when to put that fence in which situation after which operation ... etc 

 VarHandle is part of java.lang.invoke -- the MOST volatile class in the java API -- changed alot in the last 10 years

 Make sure when looking at the documentation, it needs to be JDK 21+ because it changed alot in previous versions

 How can I use VarHandle without objects, just static methods
 */
public class Main {
    static int length = 10;
    static int[][] arr = new int[length][10_000_000];
    public static void main(String[] args) {

        int i = 0;
        int a,b,c;
        for (i = 0; i < length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                VarHandle.releaseFence(); // make sure the previous iteration is written to main memory then move on
                arr[i][j] = j*2;
                VarHandle.releaseFence(); // [STORE STORE] [LOAD STORE] -- with every iteration of this loop, i will make sure that the write is done before moving to the next iteration 
                // - if another thread is looking at this array it will be able to see this write because im pushing it to main memory with the fence
                VarHandle.storeStoreFence(); // just [STORE STORE] -- only interested in reordering of the write -- and in this code I only have write operation arr[i][j] = j*2;  so this is the best option
                
            }
        }
        // if you have a release fence somewhere in your code, and you have another thread trying to acquire the data then ensure you have an acquire fence somewhere
        // if you have a release --- make sure you also have an acquire somewhere else when they want to get that data from the main memory

        a = 10;
        System.out.println(a);
        VarHandle.acquireFence(); // Make sure you finish the print a (LOAD) then move, there is no way this LOAD a will be pushed after the writes
        VarHandle.loadLoadFence();

        // on x86 the acquire and release fences are free, the hardware will not reorder but remember that the compiler or the JVM MAY reorder it so we still want them

        VarHandle.fullFence(); // all 4 fences -- NO REORDERING WHATSOEVER across this fence -- stronger than volatile 


        // STORE LOAD fence is MISSING -- YOU NEED TO CREATE AN OBJECT OF VARHANDLE to use it -- NO STATICALLY available (its a part of the fullFence though it is extremely expensive)

        /*
         Critical thinking

         Can I create volatile behavior using these static methods only -- NO, you need a store load
         */
        b = 20;
        c = 30;
    }
    
}
