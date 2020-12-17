import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;


public class solveABA {
    private  static AtomicInteger atomicInteger = new AtomicInteger(1);
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(10,1);

    public static void main(String[] args) {
        // ABA问题
        new Thread(()->{
            atomicInteger.compareAndSet(1,2);
            atomicInteger.compareAndSet(2,1);
        }).start();

        new Thread(()->{

            System.out.println(atomicInteger.compareAndSet(1, 3));
        }).start();


        // 解决ABA问题，原本的CAS算法是直接通过值来判断是否能修改。现在加了版本号后，通过版本号来判断是否能够修改。 注意：两者虽然都能修改成功，但第一种是错误的
        new Thread(()->{
            int stamp=atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+ "第一次版本号:"+stamp);
            atomicStampedReference.compareAndSet(10, 11, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName()+ "第二次版本号:"+atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(11, 10, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName()+ "第三次版本号:"+atomicStampedReference.getStamp());
        },"线程1").start();

        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int stamp=atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+ "第一次版本:"+stamp);
            System.out.println(atomicStampedReference.compareAndSet(10, 20, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
        },"线程2").start();
    }
}
