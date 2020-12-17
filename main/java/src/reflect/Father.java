package reflect;

public class Father {
    protected void father() {

    }

    public Father() {

    }

    protected void say() {
        System.out.println("father say");
    }

    public static void main(String[] args) {
        System.out.println("main of father started" +
                args[0]);
    }
}