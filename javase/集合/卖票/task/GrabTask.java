package src.main.java.javase.集合.卖票.task;


import src.main.java.javase.集合.卖票.entity.Ticket;
import src.main.java.javase.集合.卖票.entity.User;
import src.main.java.javase.集合.卖票.exception.BreakException;
import src.main.java.javase.集合.卖票.exception.LimitException;
import src.main.java.javase.集合.卖票.service.AdminService;
import src.main.java.javase.集合.卖票.service.QueueService;
import src.main.java.javase.集合.卖票.service.RateLimitService;
import src.main.java.javase.集合.卖票.util.LogUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 抢票线程任务
 * 整合：限流、熔断、排队、抢票、记录日志
 */
public class GrabTask implements Runnable {
    private final User user;
    private final Ticket ticket;
    private final RateLimitService rateLimit;
    private final QueueService queueService;
    private final AdminService adminService;
    private final CountDownLatch latch;

    public GrabTask(User user, Ticket ticket, RateLimitService rateLimit,
                    QueueService queueService, AdminService adminService, CountDownLatch latch) {
        this.user = user;
        this.ticket = ticket;
        this.rateLimit = rateLimit;
        this.queueService = queueService;
        this.adminService = adminService;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            // 1. 先进入排队队列
            boolean inQueue = queueService.enQueue(user);
            if (!inQueue) {
                LogUtil.fail("用户 " + user.getUserName() + " 排队队列已满，抢票失败");
                adminService.addRecord(user);
                return;
            }

            // 2. 限流熔断校验
            try {
                rateLimit.tryPass();
            } catch (LimitException | BreakException e) {
                LogUtil.fail("用户 " + user.getUserName() + " " + e.getMessage());
                adminService.addRecord(user);
                return;
            }

            // 模拟网络耗时
            Thread.sleep((long) (Math.random() * 300));

            // 3. 执行抢票
            boolean grabResult = ticket.grabTicket();
            user.setSuccess(grabResult);

            if (grabResult) {
                LogUtil.success("用户 " + user.getUserName() + " 抢票成功");
            } else {
                LogUtil.fail("用户 " + user.getUserName() + " 抢票失败，余票不足");
            }

            // 4. 保存后台记录
            adminService.addRecord(user);

            // 5. 释放限流计数
            rateLimit.release(grabResult);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }
    }
}