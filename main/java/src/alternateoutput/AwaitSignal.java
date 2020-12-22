package alternateoutput;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AwaitSignal extends ReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(3);
        Condition a=awaitSignal.newCondition();
        Condition b=awaitSignal.newCondition();
        Condition c=awaitSignal.newCondition();
        new Thread(()->{
            awaitSignal.print("a",a,b);
        }).start();
        new Thread(()->{
            awaitSignal.print("b",b,c);
        }).start();
        new Thread(()->{
            awaitSignal.print("c",c,a);
        }).start();

        Thread.sleep(1000);
        awaitSignal.lock();
        try {
            log.info("唤醒a");
            a.signal();
        }finally {
            awaitSignal.unlock();
        }
    }

    private int loopNum;

    public AwaitSignal(int loopNum){
        this.loopNum=loopNum;
    }

    public void print(String str,Condition condition,Condition nextCondition){
        for (int i = 0; i <loopNum ; i++) {
            lock();
            try {
                log.info("{}被阻塞了",condition);
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("线程{}正在打印{}",Thread.currentThread(),str);
                nextCondition.signal();
            }finally {
                unlock();
            }

        }
    }
}
