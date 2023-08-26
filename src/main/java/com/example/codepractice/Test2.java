package com.example.codepractice;
import java.util.Arrays;

public class Test2 {

    public static int maxEvenSum(int[] cards, int cnt) {
        Arrays.sort(cards); // 排序
        int res = 0;
        for(int i=0; i<cnt; i++) {
            res += cards[cards.length-1-i];
            if(res % 2 == 1) { // 总和为奇数
                res -= cards[cards.length-1-i];
                res += cards[cards.length-2-i];
                i--;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] cards = {5,4,3,2,1,4};
        int cnt = 3;
        System.out.println(maxEvenSum(cards, cnt));
    }

}
