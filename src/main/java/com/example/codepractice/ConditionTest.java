package com.example.codepractice;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// 使用 Condition 实现生产者—消费者模型
public class ConditionTest {
    //Condition依赖外部Lock对象
    private  static  final ReentrantLock lock=new ReentrantLock();
    private  static  final Condition A=lock.newCondition();
    private  static  final Condition B=lock.newCondition();
    private  static  final Condition C=lock.newCondition();
    private  static AtomicInteger state=new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(
                ()->{
                    print("A",0,A,B);
                }
        ,"Thread A").start();

        new Thread(
                ()->{
                    print("B",1,B,C);
                }
                ,"Thread B").start();

        new Thread(
                ()->{
                    print("C",2,C,A);
                }
                ,"Thread C").start();
    }

    static  void print(String msg,int  target,Condition cur,Condition next)
    {
        for (int i = 0; i < 100; i++) {
            try {
                lock.lock();
                while (state.get() % 3 != target) {
                    cur.await();
                }
                next.signalAll(); //唤醒在下一个condition队列中的所有线程，让该线程加入竞争锁的同步队列
                System.out.println(Thread.currentThread().getName() + "线程打印" + msg + "第" + i + "次");
                state.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}




//使用Condition实现生产者/消费者模型
//实现一个简易的BlockingQueue
class  MyBlockingQueueTest{

    private  volatile  int maxSize;
    private   final Queue<Object> queue;
    private  static  final  ReentrantLock lock=new ReentrantLock();

    private  Condition notEmpty=lock.newCondition();
    private  Condition notFull=lock.newCondition();
   public MyBlockingQueueTest(int size)
    {
        this.maxSize=size;
        queue=new LinkedList();
    }

    public  void  put(Object obj) throws InterruptedException {
       lock.lock();
       try {
           if(queue.size()==maxSize) //队列到达最大容量—— Full
           {
               System.out.println("队列已满，无法再放入元素");
               notFull.await(); //生产者不能接着put元素了

           }
           queue.add(obj);
           notEmpty.signalAll(); //唤醒等待的消费者
       }
       finally {
           lock.unlock();
       }
    }

    public  Object  get() throws  InterruptedException
    {
        lock.lock();
        try {
            if(queue.size()==0)  //队列中没有元素
            {
                notEmpty.await();// 消费者无法取了,阻塞式等待

            }
            Object item=queue.remove();
            notFull.signalAll() ;//唤醒生产者，可以继续往队列中添加元素
            return  item;
        }finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueueTest myBlockingQueueTest = new MyBlockingQueueTest(20);
        ExecutorService producerPool = Executors.newFixedThreadPool(5);
        ExecutorService consumerPool = Executors.newFixedThreadPool(5);
        producerPool.execute(
                // 这部分代码将由线程池中的一个线程异步执行
                ()->{
                    for (int i = 0; i < 20; i++) {
                        try {
                            myBlockingQueueTest.put(i);
                            System.out.println("Producer :"+i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        consumerPool.execute(
                ()->{
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < 20; i++) {
                        try {
                            Object result = myBlockingQueueTest.get();
                            System.out.println("Consumer"+result);
                        }catch (Exception e)
                        {
                            throw  new RuntimeException(e);
                        }
                    }
                }
        );

        producerPool.shutdown();
        consumerPool.shutdown();
    }
}