import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // 论文数量
        scanner.nextLine(); // 消耗掉换行符

        // 使用 TreeMap 来按字典序存储作者及其加分总数
        TreeMap<String, Integer> authorScores = new TreeMap<>();

        // 逐个处理每篇论文
        for (int i = 0; i < n; i++) {
            int numAuthors = scanner.nextInt(); // 作者数量
            scanner.nextLine(); // 消耗掉换行符

            // 逐个处理每个作者
            for (int j = 0; j < numAuthors; j++) {
                String author = scanner.next(); // 作者名字
                int score = 3 - j; // 计算加分
                // 更新作者的加分总数
                authorScores.put(author, authorScores.getOrDefault(author, 0) + score);
            }
        }

        // 输出结果
        for (Map.Entry<String, Integer> entry : authorScores.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
