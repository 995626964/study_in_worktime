package alternateoutput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitNotify {

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1,3);
        new Thread(()->{
            for (int i = 0; i <waitNotify.loopNum ; i++) {
                waitNotify.print("a",1,2);
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i <waitNotify.loopNum ; i++) {
                waitNotify.print("b",2,3);
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i <waitNotify.loopNum ; i++) {
                waitNotify.print("c",3,1);
            }
        }).start();
    }

    private int flag;
    private int loopNum;

    public WaitNotify(int flag,int loopNum){
        this.flag=flag;
        this.loopNum=loopNum;
    }

    public void print(String str,int waitFlag, int nextFlag){
        synchronized (this){
            while (waitFlag!=this.flag){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("线程{}正在打印{}",Thread.currentThread(),str);
            this.flag=nextFlag;
            notifyAll();
        }

    }

}
