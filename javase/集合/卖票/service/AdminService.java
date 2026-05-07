package src.main.java.javase.集合.卖票.service;

import src.main.java.javase.集合.卖票.entity.GrabRecord;
import src.main.java.javase.集合.卖票.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员后台：查看抢票记录、统计数据
 */
public class AdminService {
    // 全局抢票记录集合
    private final List<GrabRecord> recordList = new ArrayList<>();

    /**
     * 新增抢票记录
     */
    public void addRecord(User user) {
        String res = user.isSuccess() ? "抢票成功" : "抢票失败";
        recordList.add(new GrabRecord(user, res));
    }

    /**
     * 查看全部抢票记录
     */
    public void showAllRecord() {
        System.out.println("========== 管理员后台-全部抢票记录 ==========");
        for (GrabRecord record : recordList) {
            System.out.println(record);
        }
        System.out.println("============================================");
    }

    /**
     * 只查看成功记录
     */
    public void showSuccessRecord() {
        System.out.println("========== 管理员后台-抢票成功记录 ==========");
        List<GrabRecord> success = recordList.stream()
                .filter(r -> r.getResult().equals("抢票成功"))
                .collect(Collectors.toList());
        success.forEach(System.out::println);
        System.out.println("成功总人数：" + success.size());
        System.out.println("============================================");
    }

    /**
     * 统计概况
     */
    public void showStatistics() {
        long success = recordList.stream().filter(r -> r.getResult().equals("抢票成功")).count();
        long fail = recordList.size() - success;
        System.out.println("========== 管理员后台-数据统计 ==========");
        System.out.println("总参与人数：" + recordList.size());
        System.out.println("成功人数：" + success);
        System.out.println("失败人数：" + fail);
        System.out.println("当前排队人数：" + QueueService.class.getDeclaredMethods().length);
        System.out.println("========================================");
    }
}