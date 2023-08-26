package com.example.codepractice;

import java.util.concurrent.*;

public class FutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*
         Executors 是一个工厂类，用于创建不同类型的线程池
         ExecutorService 是线程池的抽象。它定义了提交任务、管理任务、获取任务执行结果等操作的方法
         使用 Executors 创建 ExecutorService 的实例，然后通过 ExecutorService 来管理线程池的任务执行
         创建出来的 ExecutorService 的实例是ThreadPoolExecutor类型，其父类实现了ExecutorService接口的
         故无论哪种方式创建线程池，最终都要回到ExecutorService 上来
         A extends B，B implements C  ，A是C的实现
         */
        ExecutorService executorService= Executors.newCachedThreadPool();

        Task task=new Task();
        
        //提交任务类型为callable时，可以在未来获取结果
        Future<Integer> res = executorService.submit(task);
        System.out.println("正在执行任务");

        System.out.println("task运行结果为"+res.get()); //这一步get会阻塞当前线程

        executorService.shutdown(); //关闭线程池资源

    }
}

class Task implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        int result=0;
        for (int i = 0; i < 100; i++) {
            for (int i1 = 0; i1 < i; i1++) {
                result+=i1;
            }
        }
        return  result;
    }
}
