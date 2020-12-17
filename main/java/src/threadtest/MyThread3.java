package threadtest;

import java.util.concurrent.Callable;

public class MyThread3 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread()+"implements callable");
        return 6;
    }
}
