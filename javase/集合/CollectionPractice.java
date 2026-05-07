package src.main.java.javase.集合;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class CollectionPractice {
    
    private static Random random = new Random();
    
    // 中文姓氏
    private static String[] surnames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", 
                                        "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨",
                                        "朱", "秦", "尤", "许", "何", "吕", "施", "张",
                                        "孔", "曹", "严", "华", "金", "魏", "陶", "姜"};
    
    // 中文名字（单字）
    private static String[] names = {"伟", "强", "勇", "军", "磊", "涛", "斌", "鹏",
                                     "杰", "峰", "敏", "静", "丽", "芳", "燕", "红",
                                     "玲", "娜", "婷", "雪", "梅", "琴", "兰", "英",
                                     "明", "亮", "光", "辉", "华", "宇", "翔", "宇"};

    // 生成随机中文姓名（2-3个字）
    private static String generateChineseName() {
        String surname = surnames[random.nextInt(surnames.length)];
        // 50%概率生成2字名，50%概率生成3字名
        if (random.nextBoolean()) {
            return surname + names[random.nextInt(names.length)];
        } else {
            return surname + names[random.nextInt(names.length)] + names[random.nextInt(names.length)];
        }
    }

    public static void main(String[] args) {
        // 1. ArrayList 存储学生 - 随机生成50个学生
        List<Student> stuList = new ArrayList<>();
        int studentCount = 50;
        for (int i = 1; i <= studentCount; i++) {
            String name = generateChineseName();
            int age = random.nextInt(4) + 16; // 16-19岁
             double score = Math.round((random.nextDouble() * 40 + 60) * 2) / 2.0; // 60-100分，保留一位小数
            stuList.add(new Student(i, name, age, score));
        }
        // 添加一个重复id的学生用于去重测试
        String duplicateName = generateChineseName();
        stuList.add(new Student(1, duplicateName, random.nextInt(4) + 16, 
                               Math.round((random.nextDouble() * 40 + 60) * 10) / 10.0));

        // 2. 遍历所有学生
        System.out.println("===== 原始学生列表 =====");
        for (Student s : stuList) {
            System.out.println(s);
        }

        // 3. 按成绩降序排序
        System.out.println("\n===== 按成绩降序 =====");
        stuList.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));
        stuList.forEach(System.out::println);

        // 4. 筛选年龄大于18的学生
        System.out.println("\n===== 年龄大于18的学生 =====");
        for (Student s : stuList) {
            if (s.getAge() > 18) {
                System.out.println(s);
            }
        }

        // 5. HashSet 去重
        System.out.println("\n===== HashSet 去重后 =====");
        Set<Student> stuSet = new HashSet<>(stuList);
        stuSet.forEach(System.out::println);

        // 6. HashMap 存储 id->学生
        System.out.println("\n===== HashMap 键值对 =====");
        Map<Integer, Student> stuMap = new HashMap<>();
        for (Student s : stuSet) {
            stuMap.put(s.getId(), s);
        }
        // 遍历Map
        for (Map.Entry<Integer, Student> entry : stuMap.entrySet()) {
            System.out.println("key:" + entry.getKey() + "  value:" + entry.getValue());
        }

        // 7. Queue 队列排队输出
        System.out.println("\n===== Queue 学生排队 =====");
        Queue<Student> queue = new LinkedList<>(stuSet);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }

        // 8. TreeSet 按年龄自然排序
        System.out.println("\n===== TreeSet 按年龄升序 =====");
        Set<Student> treeSet = new TreeSet<>(stuSet);
        treeSet.forEach(System.out::println);
    }
}
