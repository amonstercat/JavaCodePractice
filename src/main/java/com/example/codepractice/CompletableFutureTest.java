package com.example.codepractice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;


/*
旅游平台问题
 */



//线程池实现
class ThreadPoolImpl{

    private  static ExecutorService pool= Executors.newFixedThreadPool(3);
    public static void main(String[] args) throws InterruptedException {
        System.out.println(getPrices());
        pool.shutdown();
    }

    public static   Set<Integer> getPrices() throws InterruptedException {
        Set<Integer> prices= Collections.synchronizedSet(new HashSet<Integer>());
        pool.submit(new Task(prices));
        pool.submit(new Task(prices));
        pool.submit(new Task(prices));
        Thread.sleep(3000);
        return  prices;
    }

    static  class  Task implements  Runnable{

        Set<Integer> prices;
        public  Task( Set<Integer> prices)
        {
            this.prices=prices;
        }

        @Override
        public void run() {
            int price=0;
            try {
                Thread.sleep((long) (Math.random() * 40));
                price= (int) (Math.random()*4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            prices.add(price);
        }
    }
}

public class CompletableFutureTest {

}
