package com.example.codepractice;

//懒汉
public class SingeltonTest {

    private  static  final  SingeltonTest  singelton=new SingeltonTest();
    private  SingeltonTest(){
        //构造函数私有化
    }
    public  static  SingeltonTest getInstance()
    {
        return  singelton;
    }
}

class SingeltonTest2{
    private  static  volatile  SingeltonTest2 singelton;

    private  SingeltonTest2(){
    }
    public  static  SingeltonTest2 getInstance(){
        if (singelton==null)
        {
            synchronized (SingeltonTest2.class)
            {
                if(singelton==null)
                {
                    singelton=new SingeltonTest2();
                }
            }
        }
        return  singelton;
    }
}


//静态内部类
 class SingletonTest3 {
    // 静态内部类
    private static class SingletonInstance {
        private static final SingletonTest3 instance = new SingletonTest3();
    }
    // 获取实例（单例对象）
    public static SingletonTest3 getInstance() {
        return SingletonInstance.instance;
    }
    private SingletonTest3() {
    }
    // 类方法
    public void sayHi() {
        System.out.println("Hi,Java.");
    }
}