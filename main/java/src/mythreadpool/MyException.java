package mythreadpool;

public class MyException extends Throwable {
    @Override
    public String getMessage() {
        return "任务太多了，拒绝!抛出异常";
    }
}
