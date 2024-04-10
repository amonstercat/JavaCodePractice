package com.example.codepractice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketLimiter {
    private final int maxTokens; // 桶的最大容量  
    private final double tokensPerSecond; // 每秒添加的令牌数  
    private final AtomicInteger tokens; // 当前桶中的令牌数  
    private final ScheduledExecutorService scheduler; // 用于定时添加令牌的调度器  

    public TokenBucketLimiter(int maxTokens, double tokensPerSecond) {
        this.maxTokens = maxTokens;
        this.tokensPerSecond = tokensPerSecond;
        this.tokens = new AtomicInteger((int) Math.ceil(maxTokens)); // 初始化桶满  
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        // 立即添加一次令牌，并每秒添加一次  
        this.scheduler.scheduleAtFixedRate(
                () -> tokens.addAndGet((int) Math.ceil(tokensPerSecond)), 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    /*
     * 尝试消费一个令牌，如果成功则返回true，否则返回false。
     */
    public synchronized boolean tryConsume() {
        if (tokens.get() > 0) {
            // 如果桶中有令牌，则消费一个并返回true  
            tokens.decrementAndGet();
            return true;
        } else {
            // 桶中没有令牌，返回false  
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建一个令牌桶，最大容量为10个令牌，每秒添加5个令牌  
        TokenBucketLimiter limiter = new TokenBucketLimiter(10, 5.0);

        // 模拟请求到达并尝试获取令牌  
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                if (limiter.tryConsume()) {
                    System.out.println("Thread " + Thread.currentThread().getId() + " got a token and processed the request.");
                } else {
                    System.out.println("Thread " + Thread.currentThread().getId() + " didn't get a token and the request was throttled.");
                }
            }).start();

            // 稍作等待以模拟请求的到达间隔  
            Thread.sleep(200);
        }

        // 等待一段时间让令牌桶有机会添加令牌，并处理剩余的请求  
        Thread.sleep(2000);

        // 停止令牌桶的令牌添加任务  
        limiter.stop();
    }
}