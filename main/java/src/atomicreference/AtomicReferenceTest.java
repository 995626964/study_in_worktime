package atomicreference;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    public static void main(String[] args) {
        User user = new User();
        user.setName("张三");
        user.setAge(20);
        user.setNumber("13281");
        System.out.println(user);
        AtomicReference<User> atomicReference = new AtomicReference<>(user);

        new Thread(()->{
            User user2 = new User();
            user2.setName("李四");
            user2.setAge(18);
            user2.setNumber("13281");
            // user的name属性已经变化却还能够compareAndSet成功，证明确实是比较的对象的引用。
            user.setName("xxx");
            System.out.println(user2);
            System.out.println(atomicReference.compareAndSet(user, user2));
        },"线程1").start();

        new Runnable(){
            @Override
            public void run() {
                User user3 = new User();
                user3.setName("王五");
                user3.setAge(17);
                System.out.println(atomicReference.compareAndSet(user,user3));
            }
        }.run();
    }
}
