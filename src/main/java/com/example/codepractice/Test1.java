import java.util.HashMap;
import java.util.Map;

public class Test1 {
    public static int findMysteriousGem(int[] arr) {
        // 使用HashMap来存储整数与其出现次数的映射关系
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // 统计整数的出现次数
        for (int num : arr) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int maxMysteriousGem = -1; // 默认情况下没有神秘宝石
        for (int num : frequencyMap.keySet()) {
            int frequency = frequencyMap.get(num);
            if (frequency == num && num > maxMysteriousGem) {
                maxMysteriousGem = num;
            }
        }

        return maxMysteriousGem;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 2, 3, 3, 3};
        System.out.println(findMysteriousGem(arr1)); // 输出：3

        int[] arr2 = {2, 2, 3, 4};
        System.out.println(findMysteriousGem(arr2)); // 输出：2
    }
}
