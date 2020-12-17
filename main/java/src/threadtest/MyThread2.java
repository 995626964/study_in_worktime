package threadtest;

public class MyThread2 implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread()+"implements runnable");
    }
}
