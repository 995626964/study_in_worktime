package alternateoutput;

import java.util.concurrent.locks.LockSupport;

public class ParkUnpark {

    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {
        ParkUnpark parkUnpark = new ParkUnpark();
        t1 = new Thread(()->{
            parkUnpark.print("a",t2);
        });
        t2 = new Thread(()->{
            parkUnpark.print("b",t3);
        });
        t3 = new Thread(()->{
            parkUnpark.print("c",t1);
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);

    }

    public void print(String str,Thread next){
        for (int i = 0; i <3 ; i++) {
            try {
                //保证unpark()先执行
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }

    }
}
