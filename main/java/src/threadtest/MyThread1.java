package threadtest;

public class MyThread1 extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println(Thread.currentThread()+"extends Thread1111");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
