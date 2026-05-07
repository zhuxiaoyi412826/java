package src.main.java.javase.集合.卖票.test;

import src.main.java.javase.集合.卖票.entity.User;
import src.main.java.javase.集合.卖票.service.AdminService;
import src.main.java.javase.集合.卖票.service.GrabService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 企业级高并发抢票测试
 * 功能：100个用户 完全同时抢票
 */
public class ConcurrencyTest {

    public static void main(String[] args) throws InterruptedException {
        int threadNum = 100;

        GrabService grabService = new GrabService();
        AdminService adminService = new AdminService();

        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(threadNum);

        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= threadNum; i++) {
            userList.add(new User("UID" + i, "测试用户" + i));
        }

        // 提交 100 个抢票任务
        for (User user : userList) {
            pool.submit(() -> {
                try {
                    latch.await(); // 同时等待
                    grabService.startGrab(1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish.countDown();
                }
            });
        }

        System.out.println("【1秒后 100 线程同时抢票】");
        Thread.sleep(1000);

        // 瞬间同时触发
        latch.countDown();
        finish.await();

        pool.shutdown();

        System.out.println("\n====== 测试结束 ======");
        adminService.showStatistics();
    }
}
