package com.example.codepractice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatchTest {

    private  static  final CountDownLatch count=new CountDownLatch(3);
    public static void main(String[] args) throws InterruptedException {

        System.out.println("主线程开始执行");

        new Thread(
                ()->{
                    count.countDown(); //共享模式下的释放
                    System.out.println(Thread.currentThread().getName()+"加载资源1");
                    System.out.println("还有"+count.getCount()+"个资源未加载完成");
                }
                ,"Thread1").start();

        new Thread(
                ()->{
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    count.countDown(); //共享模式下的释放
                    System.out.println(Thread.currentThread().getName()+"加载资源2");
                    System.out.println("还有"+count.getCount()+"个资源未加载完成");
                }
                ,"Thread2").start();
        new Thread(
                ()->{
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    count.countDown(); //共享模式下的释放
                    System.out.println(Thread.currentThread().getName()+"加载资源3");
                    System.out.println("还有"+count.getCount()+"个资源未加载完成");
                }
                ,"Thread3").start();


        count.await();
        System.out.println("前置资源加载完毕,主线程继续执行");
    }
}
