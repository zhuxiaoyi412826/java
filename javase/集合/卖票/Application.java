package src.main.java.javase.集合.卖票;

import src.main.java.javase.集合.卖票.service.GrabService;

/**
 * 抢票系统入口
 */
public class Application {
    public static void main(String[] args) {
        GrabService grabService = new GrabService();

        // 模拟200人同时高并发抢票
        grabService.startGrab(200);

        // 管理员后台手动追加票数
        grabService.adminAddTicket(50);
    }
}
