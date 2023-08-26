package com.example.codepractice;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

//使用信号量实现线程交替打印 "A" 、"B"字符
public class SemaphoreTest {

  private  static    Semaphore semaphoreOdd = new Semaphore(1);
  private  static    Semaphore semaphoreEven = new Semaphore(0);

    public static void main(String[] args) {

        new  Thread(
                ()->{
                    try {
                        print("A",semaphoreOdd,semaphoreEven);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        ,"线程1").start();

        new  Thread(
                ()->{
                    try {
                        print("B",semaphoreEven,semaphoreOdd);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                ,"线程2").start();
    }

    static  void print(String mes,Semaphore currentSemaphore, Semaphore nextSemaphore) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            currentSemaphore.acquire();//当前信号量  -1
            System.out.println( Thread.currentThread().getName()+"线程打印"+mes+"第"+i+"次");
            nextSemaphore.release(); //下一信号量+1
        }
    }
}



//使用monitor监视器实现交替打印
class  SynchronizedTest{

  static  final   Object  monitor=new Object();
  static  final AtomicInteger count=new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(
                ()->{
                    while (count.get()<=100)
                        synchronized (monitor) {
                            System.out.println("线程1" + ":" + count);
                            count.incrementAndGet();
                            monitor.notify();
                            //此处判断，是为了打印完了100个数字后，程序能够正常结束，
                            // 否则程序将一直等待下去，耗费系统资源。
                            if (count.get() <= 100) {
                                try {
                                    monitor.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                }
        ,"Thread1").start();


        new Thread(
                ()->{
                    while (count.get()<=100)
                        synchronized (monitor) {
                            System.out.println("线程2" + ":" + count);
                            count.incrementAndGet();
                            monitor.notify();
                            if (count.get() <= 100) {
                                try {
                                    monitor.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                }
                ,"Thread2").start();
    }
}

//使用Semaphore三个线程交替打印A、B、C

class  SemaphoreTest2{

 private static  final  Semaphore semaphoreA =new Semaphore(1);
 private static  final  Semaphore semaphoreB=new Semaphore(0);
 private static  final  Semaphore semaphoreC=new Semaphore(0);

    public static void main(String[] args) {

        new Thread(
                ()->{
                    print("A",semaphoreA,semaphoreB);
                }
        ,"ThreadA").start();

        new Thread(
                ()->{
                    print("B",semaphoreB,semaphoreC);
                }
                ,"ThreadB").start();

        new Thread(
                ()->{
                    print("C",semaphoreC,semaphoreA);
                }
                ,"ThreadC").start();
    }

   public static  void print(String message,Semaphore cur,Semaphore next)
   {

       for (int i = 0; i < 100; i++) {
           try {
               cur.acquire(1); //
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           System.out.println(Thread.currentThread().getName()+"打印"+message+"第"+i+"次");
           next.release(); //为下一个等待该信号量的线程开绿灯
       }
   }
}


class  SemaphoreTest3{

 private  static  final  Semaphore semaphore=new Semaphore(0);
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()+"启动");
        semaphore.release(2);
        new Thread(()->
        {
            try {
                Thread.sleep(2000);
                semaphore.acquire(1);
                System.out.println(Thread.currentThread().getName()+"获取到信号量");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Thread1").start();

        new Thread(()->
        {
            try {
                Thread.sleep(2000);
                semaphore.acquire(1);
                System.out.println(Thread.currentThread().getName()+"获取到信号量");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Thread2").start();
    }
}