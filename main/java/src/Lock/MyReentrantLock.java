package Lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MyReentrantLock {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        //3.规定时间内没获取到锁，打断自己等待状态
//        Thread thread2=new Thread(()->{
//            try {
//                System.out.println("等待获取锁");
//                if(lock.tryLock(2000, TimeUnit.MILLISECONDS)){
//                    System.out.println("获取到了锁");
//                    lock.unlock();
//                }else {
//                    System.out.println("没有获取锁");
//                }
//            }catch (InterruptedException e) {
//                System.out.println("被打断了");
//                e.printStackTrace();
//            }
//
//        });
//
//        System.out.println("主线程获取锁");
//        lock.lock();
//        thread2.start();
//        Thread.sleep(1000);
//        thread2.interrupt();



//2.可被打断的获取锁
//        Thread t1 = new Thread(() -> {
//            try {
//                System.out.println("等待获取锁");
//                lock.lockInterruptibly();
//
//            } catch (InterruptedException e) {
//                System.out.println("没有获取锁，返回");
//                e.printStackTrace();
//                return;
//            }
//            try {
//                System.out.println("获取到了锁");
//            } finally {
//                lock.unlock();
//            }
//
//        }, "张三");
//
//        lock.lock();
//        try {
//            t1.start();
//            try {
//                Thread.sleep(1000);
//                System.out.println("打断他");
//                t1.interrupt();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            lock.unlock();
//        }

// 1.可重入特性
//        lock.lock();
//        try {
//            System.out.println("main");
//            a1();
//        }finally {
//            lock.unlock();
//        }
    }

    public static void a1() {
        lock.lock();
        try {
            System.out.println("a1");
            b1();
        } finally {
            lock.unlock();
        }
    }

    public static void b1() {
        lock.lock();
        try {
            System.out.println("b1");
        } finally {
            lock.unlock();
        }
    }

}
