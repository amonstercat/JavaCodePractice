package com.example.codepractice;

import java.util.concurrent.*;

public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //FutureTask中的任务可以是Callable类型，代表一个可返回结果的任务
        FutureTask<String> futureTask=new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName()+"正在运行");
                Thread.sleep(3000);
                return "Success";
            }
        });

        new Thread(futureTask).start(); //启动单个线程执行任务
        String result=futureTask.get();// 同时又实现了Future，可以获取异步结果（会阻塞3秒）
        System.out.println(result);

    }
}
