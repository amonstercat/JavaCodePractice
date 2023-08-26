package com.example.codepractice;


public class SnowFlake{

    private long workerId;
    private long datacenterId;
    private long sequence;

    public SnowFlake(long workerId, long datacenterId, long sequence){
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",maxDatacenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    private long twepoch = 1288834974657L; //起始时间戳,用于用当前时间戳减去这个时间戳，算出偏移量

    private long workerIdBits = 5L; //worker占的位数
    private long datacenterIdBits = 5L; //datacenter占的位数


    /*
    00000000 00000000 00000000 00000001 //原码：1的二进制
    11111111 11111111 11111111 11111110 //取反码：1的二进制的反码
    11111111 11111111 11111111 11111111 //加1：-1的二进制表示（补码）

    -1 的二进制表示 先左移 5位，得到结果a;
    再让 -1 异或 a;
     */
    private long maxWorkerId = -1L ^ (-1L << workerIdBits); //利用位运算计算出5位能表示的最大正整数是31
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);//同理
    private long sequenceBits = 12L;//同一机器同一时间截（毫秒)的并发数所占位数


    //偏移量
    private long workerIdShift = sequenceBits; //12
    private long datacenterIdShift = sequenceBits + workerIdBits; //17
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; //22
    private long sequenceMask = -1L ^ (-1L << sequenceBits);  //4095    为了防止溢出

    private long lastTimestamp = -1L; //对比的上一次时间戳，起始值为最小值

    public long getWorkerId(){
        return workerId;
    }

    public long getDatacenterId(){
        return datacenterId;
    }

    public long getTimestamp(){
        return System.currentTimeMillis();
    }


    // 生成下一个唯一ID
    public synchronized long nextId() {
        long timestamp = timeGen();//获取当前时间戳

        //当前时间戳<上一次获取到的时间戳 —— 发生了时钟回拨
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        //如果两次生成id是在同一毫秒内
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask; //并发数+1
            if (sequence == 0) {                  //4095+1=4096
                timestamp = tilNextMillis(lastTimestamp); //更新时间戳 进入到下一毫秒
            }
        }
        //如果当前时间戳不同，说明已经进入了下一毫秒，序列号重新归零。
        else
        {
            sequence = 0;
        }

        lastTimestamp = timestamp;  //将当前时间戳记录为上一次的时间戳，以备下一次生成ID时使用。

        // 将各个部分组合成最终的ID ——先将每部分左移至对应部分，然后各部分通过或运算得到最终结果
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }


    // 在当前时间戳基础上等待，直到下一个毫秒
    private long tilNextMillis(long lastTimestamp) {

        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }



    //返回当前时间戳
    private long timeGen(){
        return System.currentTimeMillis();
    }


    //---------------测试---------------
    public static void main(String[] args) {
        SnowFlake worker = new SnowFlake(1,1,1);
        for (int i = 0; i < 30; i++) {
            System.out.println(worker.nextId()+"第"+i+"次生成");
        }
    }
}