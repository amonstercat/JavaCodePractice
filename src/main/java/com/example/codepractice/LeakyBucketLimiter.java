package com.example.codepractice;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
/*
漏桶是一个很形象的比喻，外部请求就像是水一样不断注入水桶中，而水桶已经设置好了最大出水速率，
漏桶会以这个速率匀速放行请求，而当水超过桶的最大容量后则被丢弃。
漏桶算法能够像消息队列中的消费者一样控制消息被消费的速率
 */
public class LeakyBucketLimiter {

    //桶的容量
    private int capacity;

    //桶中现存水量
    private AtomicLong water=new AtomicLong(0);

    //开始漏水的时间戳
    private  long leakTimeStamp=System.currentTimeMillis();

    //水流出的速率，即限制每秒允许通过的请求数
    private int leakRate;


    public  LeakyBucketLimiter (int capacity,int leakRate)
    {
        this.capacity=capacity;
        this.leakRate=leakRate;
    }

    /*
    根据桶中水量及漏水速率来判断是否允许请求通过
     * true 代表放行，请求可已通过
     * false 代表限制，不让请求通过
     */
    public  synchronized  boolean tryAcquire()
    {
        //桶中没有水(请求)，将请求放入桶中，并直接放行，
        if(water.get()==0)
        {
            leakTimeStamp=System.currentTimeMillis();
            water.set(1);
            return  true;
        }
        //计算桶中剩余水量
        water.set(water.get()-(System.currentTimeMillis()-leakTimeStamp)/1000*leakRate);

        water.set(Math.max(0, water.get()));
        leakTimeStamp+=System.currentTimeMillis()-leakTimeStamp;
        if(water.get()<capacity)
        {
            return  true;
        }else {
            return  false;
        }
    }

    public static void main(String[] args) {

        //桶最多容纳100个请求，桶漏出(请求执行)速率为20
        LeakyBucketLimiter leakyBucketLimiter=new LeakyBucketLimiter(100,20);

        //创建一个固定线程数量的线程池
        var executorService = Executors.newFixedThreadPool(10);
        int threads=3;
        CountDownLatch countDownLatch=new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            executorService.submit(
                    ()->{

                    }
            );
        }
    }
}
