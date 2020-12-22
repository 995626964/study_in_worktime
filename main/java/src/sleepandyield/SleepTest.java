package sleepandyield;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepTest {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.debug("被唤醒了{}",Thread.currentThread().getState());
                e.printStackTrace();
            }
        });
        thread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("状态{}",thread.getState());

        thread.interrupt();
    }
}
