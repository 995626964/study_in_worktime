import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicTest {
    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static int clientTotal = 5000;
    public static int threadTotal = 200;

    public static void main(String[] args) throws Exception {
        ExecutorService executors = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        long start = System.currentTimeMillis();
        for (int i = 0; i <clientTotal ; i++) {
                executors.execute(()->{
                    try {
                        semaphore.acquire();
                        System.out.println(semaphore.availablePermits());
                        Thread.sleep(200);
                        add();
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                });
        }
        countDownLatch.await();
        executors.shutdown();
        long end = System.currentTimeMillis();
        System.out.println(atomicInteger+"用时:"+(end-start));
    }
    public static void add(){
        atomicInteger.getAndIncrement();
    }
// 请求总数
//public static int clientTotal = 5000;
//    // 同时并发执行的线程数
//    public static int threadTotal = 200;
//
//    public static AtomicInteger count = new AtomicInteger(0);
//
//    public static void main(String[] args) throws Exception {
//        ExecutorService executorService = Executors.newCachedThreadPool();//获取线程池
//        final Semaphore semaphore = new Semaphore(threadTotal);//定义信号量
//        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
//        for (int i = 0; i < clientTotal ; i++) {
//            executorService.execute(() -> {
//                try {
//                    semaphore.acquire();
//                    add();
//                    semaphore.release();
//                } catch (Exception e) {
//                }
//                System.out.println(count.get());
//                countDownLatch.countDown();
//            });
//        }
//        countDownLatch.await();
//        executorService.shutdown();
//        System.out.println(count.get());
//    }
//
//    private static void add() {
//        count.getAndIncrement();
//    }

}
