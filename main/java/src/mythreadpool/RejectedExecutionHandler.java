package mythreadpool;

public interface RejectedExecutionHandler {
    public void rejectedExecution(Runnable r,MyThreadPool myThreadPool);
}
