package Lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyCondition {
    static ReentrantLock lock= new ReentrantLock();
    static boolean hasWine = false;
    static boolean hasMoney = false;
    public static void main(String[] args) throws InterruptedException {

        Condition condition = lock.newCondition();
        Condition condition1 = lock.newCondition();

        new Thread(()->{
            lock.lock();
            try {
                System.out.println("有酒没？"+ hasWine);
                while (!hasWine){
                    try {
                        System.out.println("没酒，歇会");
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread()+"可以开始干活了");
            }finally {
                lock.unlock();
            }

        },"小张").start();

        new Thread(()->{
            lock.lock();
            try {
                System.out.println("有钱没？"+hasMoney);
                while (!hasMoney){
                    try {
                        System.out.println("没钱，歇会");
                        condition1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread()+"可以开始干活了");
            }finally {
                lock.unlock();
            }

        },"小李").start();

        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                hasWine=true;
                condition.signal();
            }finally {
                lock.unlock();
            }

        }).start();

        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                hasMoney=true;
                condition1.signal();
            }finally {
                lock.unlock();
            }

        }).start();

    }
}
