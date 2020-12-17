package mythreadpool;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread());
        MyThreadPool myThreadPool = new MyThreadPool(2,5,TimeUnit.SECONDS,new MyBlockingQueue(1),((r, myThreadPool1) -> {
            if(!myThreadPool1.isShutDown()){
                // 1.哪个线程调用的这个方法就让哪个线程去执行这个被拒绝的任务
                // System.out.println("拒绝策略生效"+Thread.currentThread()+"正在运行..."+r);
                // r.run();

                // 2.放弃这个任务
                // System.out.println("放弃这个任务，不执行");

                // 3.抛异常
                // 如果抛出的是RuntimeException并且没有捕获它，那么后面的程序不会执行了
                try {
                    throw new MyException();
                } catch (MyException e) {
                    e.printStackTrace();
                }

                // 4.放弃工作队列中最旧的，也就是最先加入工作队列的任务，再把这个新任务添加进去。由于不知道怎么停止这个旧任务(runnable)，只能将这个task变为null，达到停止任务的效果。 自己写的版本,结果正确，逻辑不一定正确。
//                Runnable oldestTask=myThreadPool1.workQueue.peek().getTask();
//                System.out.println("放弃了之前最早加入工作队列的"+oldestTask);
//                myThreadPool1.workQueue.peek().setTask(r);

            }

        }));
        for (int i = 0; i <10 ; i++) {
            int j = i;
            myThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(j);
                }
            });
        }
    }

    private Integer coreSize;
    private long timeout;
    private TimeUnit timeUnit;
    private MyBlockingQueue myBlockingQueue;
    private RejectedExecutionHandler rejectedExecutionHandler;
    private BlockingQueue<Worker> workQueue = new LinkedBlockingDeque<>();

    public MyThreadPool(Integer coreSize, long timeout, TimeUnit timeUnit, MyBlockingQueue myBlockingQueue, mythreadpool.RejectedExecutionHandler rejectedExecutionHandler) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.myBlockingQueue=myBlockingQueue;
        this.rejectedExecutionHandler=rejectedExecutionHandler;
    }

    public void execute(Runnable task){
        synchronized (workQueue){
            if(workQueue.size()<coreSize){
                Worker worker= new Worker(task);
                System.out.println("生成核心线程"+worker);
                System.out.println("加入运行列表"+task);
                workQueue.offer(worker);
                worker.start();
            }else{
                if(!myBlockingQueue.offer(task,2,TimeUnit.SECONDS)){
                    rejectedExecutionHandler.rejectedExecution(task,this);
                }
            }
        }
    }

    public Boolean isShutDown(){
        // 判断线程池是否被关闭,目前直接返回false
        return false;
    }


    public class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        public Runnable getTask() {
            return task;
        }

        public void setTask(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (task != null || (task = myBlockingQueue.take()) != null) {
                try {
                    System.out.println(Thread.currentThread()+"正在运行..."+task);
                    task.run();
                    System.out.println(Thread.currentThread()+"运行完了"+task);
                } finally {
                    task = null;
                }
            }
            synchronized (workQueue){
                System.out.println("销毁task"+task);
                workQueue.remove(this);
            }
        }

    }

    static class MyBlockingQueue {
        private Integer size;
        private ArrayDeque arrayDeque;
        private ReentrantLock lock = new ReentrantLock();
        private Condition offerCondition = lock.newCondition();
        private Condition takeCondition = lock.newCondition();

        public MyBlockingQueue(Integer queueSize) {
            this.size = queueSize;
            this.arrayDeque = new ArrayDeque();
        }

        public boolean offer(Runnable runnable, long timeout, TimeUnit timeUnit) {
            lock.lock();
            try {
                long nanos = timeUnit.toNanos(timeout);
                while (arrayDeque.size() == size) {
                    try {
                        if (nanos <= 0) {
                            return false;
                        }
                        System.out.println("等待加入等待队列" + runnable);
                        nanos = offerCondition.awaitNanos(nanos);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("加入等待队列" + runnable);
                arrayDeque.addLast(runnable);
                takeCondition.signal();
                return true;
            } finally {
                lock.unlock();
            }
        }

        public Runnable take() {
            lock.lock();
            try {
                while (arrayDeque.size() == 0) {
                    try {
                        System.out.println("等待获取一个runnable");
                        takeCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Runnable runnable = (Runnable) arrayDeque.removeFirst();
                System.out.println("获取到一个runnable" + runnable);
                offerCondition.signal();
                return runnable;
            } finally {
                lock.unlock();
            }
        }
    }
}
