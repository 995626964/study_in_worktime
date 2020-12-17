package reflect;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

public class ReflectTest {

    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("reflect.MyClass");

        System.out.println("******************获取所有公有构造方法******************");
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor1 : constructors) {
            System.out.println(constructor1);
        }

        System.out.println("******************获取所有构造方法（公有，私有，受保护，默认）******************");
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }

        System.out.println("******************获取公有，无参的构造方法******************");
        Constructor constructor = clazz.getConstructor();
        System.out.println(constructor);

        System.out.println("******************获取私有的构造方法，并调用******************");
        Constructor declaredConstructor = clazz.getDeclaredConstructor(int.class);
        System.out.println(declaredConstructor);
        // 暴力访问。（忽略访问修饰符）
        declaredConstructor.setAccessible(true);
        declaredConstructor.newInstance(20);

        System.out.println("******************获取所有公有的字段******************");
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            System.out.println(field);
        }

        System.out.println("******************获取所有的字段（公有，私有，受保护）******************");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
        }
        System.out.println("******************获取公有字段，并调用******************");
        Object obj = constructor.newInstance(); // 相当于MyClass myClass=new MyClass();
        Field name = clazz.getField("name");// myclass.name("李四")
        name.set(obj, "李四");
        MyClass myClass = (MyClass) obj;
        System.out.println(myClass.name);

        System.out.println("******************获取私有字段，并调用******************");
        Field tel = clazz.getDeclaredField("tel");
        tel.setAccessible(true);
        tel.set(obj, "12321213645");
        System.out.println(myClass);

        System.out.println("***************获取所有的”公有“方法(包括父类的，也包括Object类的)*******************");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method);
        }

        System.out.println("***************获取所有的方法(包括私有的，不包括从父类继承的)*******************");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod);
        }

        System.out.println("***************获取公有的say()方法，并调用*******************");
        Method say = clazz.getMethod("say");
        say.invoke(obj);

        System.out.println("***************获取私有的say()方法，并调用*******************");
        Method say4 = clazz.getDeclaredMethod("say4",int.class);
        say4.setAccessible(true);
        say4.invoke(obj,20);

        System.out.println("***************反射执行Father中的main方法*******************");
        Class father = Class.forName("reflect.Father");
        Object fatherObj=father.newInstance();
        Method main = father.getMethod("main", String[].class);

        // JDK1.5把整个数组作为一个参数，而JDK1.4会把一个数组中的每一个元素作为一个参数，由于高版本需要兼容低版本，这个数组会被打散成若干个单独的参数，就会导致异常。
        // 解决办法：将参数转换成一个不是数组的参数，让程序无法进行拆散，这样传入的就是一个参数了。
        main.invoke(fatherObj,(Object)new String[]{"1","2","3"});

        // 我们利用反射和配置文件，可以使：应用程序更新时，对源码无需进行任何修改。我们只需要将新类发送给客户端，并修改配置文件即可
        System.out.println("***************通过反射运行配置文件内容*******************");
        Class<?> className = Class.forName(getValue("className"));
        Object o = className.newInstance();
        Method method=className.getDeclaredMethod(getValue("method"));
        method.invoke(o);

        // 反射方法的其他使用--通过反射越过泛型检查
        System.out.println("***************通过反射越过泛型检查*******************");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("lisi");
        arrayList.add("zhangsan");
        //arrayList.add(200);

        Class listClass=arrayList.getClass();
        Method add = listClass.getMethod("add",Object.class);
        add.invoke(arrayList,200);
        for (Object o1 : arrayList) {
            System.out.println(o1);
        }
    }

    public static String getValue(String key) throws IOException {
        //获取配置文件的对象
        Properties pro = new Properties();
        FileReader in = new FileReader("src/reflect/application.properties");
        pro.load(in);
        in.close();
        return pro.getProperty(key);
    }

}
