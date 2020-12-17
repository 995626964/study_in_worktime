package reflect;

public class MyClass extends Father {
    public String name;
    protected int age;
    char sex;
    private String tel;

    MyClass(char s) {

    }

    public MyClass(String name) {
        System.out.println("姓名" + name);
    }

    private MyClass(int age) {
        System.out.println("年龄" + age);
    }

    public MyClass() {

    }

    public void say() {
        System.out.println("公有的无参方法");
    }

    protected void say2(String s){
        System.out.println("受保护的，有参方法");
    }

    protected void say3(){
        System.out.println("受保护的，无参方法");
    }

    private String say4(int age){
        System.out.println("私有的，有参数和返回值的方法");
        return String.valueOf(age);
    }

    @Override
    public String toString() {
        return name+age+sex+tel;
    }



}