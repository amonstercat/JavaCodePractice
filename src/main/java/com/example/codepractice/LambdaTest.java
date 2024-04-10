package com.example.codepractice;

import java.util.concurrent.ScheduledExecutorService;

@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class LambdaTest {
    public static void main(String[] args) {
        // 使用 lambda 表达式创建 MathOperation 接口的实例

        /*
        如果 Lambda 表达式的主体只有一条表达式，
        那么该表达式的值就是 Lambda 表达式的返回值。
        *下列四条语句创建了四个MathOperation的实例
            */
        MathOperation add = ((a, b) -> {
           int res= a + b;
           return  res;
        });
        MathOperation subtract = (a, b) -> a - b;
        MathOperation multiply = (a, b) -> a * b;
        MathOperation divide = (a, b) -> a / b;

        int num1 = 10;
        int num2 = 5;

        // 调用 MathOperation 接口的实现方法
        System.out.println("Addition: " + add.operate(num1, num2));
        System.out.println("Subtraction: " + subtract.operate(num1, num2));
        System.out.println("Multiplication: " + multiply.operate(num1, num2));
        System.out.println("Division: " + divide.operate(num1, num2));
    }
}
