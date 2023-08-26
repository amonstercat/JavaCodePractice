package com.example.codepractice;

import java.util.Arrays;

public class Test3 {
    public static int maxCatScore(int[] treasureValue) {
        int n = treasureValue.length;
        int[][] dp = new int[n][n];

        // 初始化dp数组
        for (int i = 0; i < n; i++) {
            dp[i][i] = treasureValue[i];
        }

        // 填充dp数组
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                int total = 0;
                for (int k = i; k <= j; k++) {
                    total += treasureValue[k];
                }
                dp[i][j] = Math.max(total - dp[i + 1][j], total - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1];
    }

    public static void main(String[] args) {
        int[] treasure1 = {6, 2, 3, 4, 5, 5};
        System.out.println(maxCatScore(treasure1)); // 输出：18

        int[] treasure2 = {7, 7, 7, 7, 7, 7, 7};
        System.out.println(maxCatScore(treasure2)); // 输出：28
    }
}
