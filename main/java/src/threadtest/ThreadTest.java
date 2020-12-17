package threadtest;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    private  static Object resource1 = new Object();
    private  static Object resource2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        //由于循环等待条件导致了死锁，thread1永远得不到resource2，thread2永远得不到resource1。解决办法是按某一顺序请求资源。
        new Thread(()->{
            synchronized (resource1) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (resource2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        },"线程1").start();

        new Thread(()->{
            synchronized (resource2) {
                System.out.println(Thread.currentThread() + "get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource1");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        },"线程2").start();

        MyThread1 myThread1 = new MyThread1();
        MyThread2 myThread2 = new MyThread2();
        MyThread3 myThread3 = new MyThread3();

        myThread1.start();
        myThread1.join(3000);
        //myThread1.interrupt();
        Thread thread2 = new Thread(myThread2);
        thread2.start();
        FutureTask<Integer> future = new FutureTask(myThread3);
        Thread thread3 = new Thread(future);
        thread3.run();
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 4; i++) {
            executorService.execute(myThread1);
        }

        executorService.shutdown();
    }
}
