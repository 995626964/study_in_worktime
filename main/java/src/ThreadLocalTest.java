import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalTest {
//    private static ThreadLocal<Map<String,Integer>> threadLocal = new ThreadLocal();
//    public static void main(String[] args) {
//        new Thread(()->{
//            HashMap map = new HashMap();
//            map.put("age1",20);
//            threadLocal.set(map);
//            threadLocal.remove();
//        },"线程1").start();
//
//        new Thread(()->{
//            HashMap map = new HashMap();
//            map.put("age2",25);
//            HashMap map2 = new HashMap();
//            map2.put("age4",26);
//            threadLocal.set(map);
//            threadLocal.set(map2);
//            System.out.println(threadLocal.get().get("age4"));
//            threadLocal.remove();
//        },"线程2").start();
//    }
private static final int THREAD_LOOP_SIZE = 500;
    private static final int MOCK_DIB_DATA_LOOP_SIZE = 10000;

    private static ThreadLocal<List<User>> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_LOOP_SIZE);
        for (int i = 0; i < THREAD_LOOP_SIZE; i++) {
            executorService.execute(() -> {
                threadLocal.set(new ThreadLocalTest().addBigList());
                Thread t = Thread.currentThread();
                System.out.println(Thread.currentThread().getName());
                threadLocal.remove(); //模拟每次使用完之后都要调用remove()掉，记住是在当前线程内部调用
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    private List<User> addBigList() {
        List<User> params = new ArrayList<>(MOCK_DIB_DATA_LOOP_SIZE);
        for (int i = 0; i < MOCK_DIB_DATA_LOOP_SIZE; i++) {
            params.add(new User("xuliugen", "password" + i, "男", i));
        }
        return params;
    }

    class User {
        private String userName;
        private String password;
        private String sex;
        private int age;
        public User(String userName, String password, String sex, int age) {
            this.userName = userName;
            this.password = password;
            this.sex = sex;
            this.age = age;
        }
    }
}
