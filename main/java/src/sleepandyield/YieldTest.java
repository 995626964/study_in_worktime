package sleepandyield;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YieldTest {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            int count = 0;
            for (;;) {
                Thread.yield();
                System.out.println("----->"+count++);
            }
        },"t1");

        Thread t2 = new Thread(()->{
            int count= 0;
            for(;;){
                System.out.println("       ----->"+count++);
            }
        },"t2");

        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t2.start();
    }
}
