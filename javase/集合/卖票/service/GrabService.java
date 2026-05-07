package src.main.java.javase.集合.卖票.service;

import src.main.java.javase.集合.卖票.constant.TicketConstants;
import src.main.java.javase.集合.卖票.entity.Ticket;
import src.main.java.javase.集合.卖票.entity.User;
import src.main.java.javase.集合.卖票.task.GrabTask;
import src.main.java.javase.集合.卖票.util.LogUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GrabService {
    private final Ticket ticket;
    private final TicketPoolService ticketPoolService;
    private final RateLimitService rateLimitService;
    private final QueueService queueService;
    private final AdminService adminService;
    private final ExecutorService threadPool;

    public GrabService() {
        // 初始化车票
        this.ticket = new Ticket(TicketConstants.INIT_TICKET_NUM);
        // 票池动态管理
        this.ticketPoolService = new TicketPoolService(ticket);
        // 限流熔断
        this.rateLimitService = new RateLimitService();
        // 排队队列
        this.queueService = new QueueService();
        // 管理员后台
        this.adminService = new AdminService();
        // 线程池
        this.threadPool = Executors.newFixedThreadPool(TicketConstants.CORE_POOL_SIZE);
    }

    /**
     * 开始并发抢票
     */
    public void startGrab(int userNum) {
        LogUtil.info("========== 高并发抢票系统启动 ==========");
        LogUtil.info("初始总票数：" + ticket.getRemainTicket());
        LogUtil.info("参与抢票人数：" + userNum);
        LogUtil.info("最大并发限流：" + TicketConstants.MAX_RATE_LIMIT);
        LogUtil.info("=======================================");

        CountDownLatch latch = new CountDownLatch(userNum);

        // 批量提交抢票任务
        for (int i = 1; i <= userNum; i++) {
            User user = new User("UID" + i, "抢票用户" + i);
            threadPool.execute(new GrabTask(user, ticket, rateLimitService, queueService, adminService, latch));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        threadPool.shutdown();
        LogUtil.info("所有抢票请求处理完毕");

        // 管理员后台展示数据
        showAdminPanel();
    }

    /**
     * 管理员后台面板
     */
    private void showAdminPanel() {
        System.out.println("\n");
        adminService.showStatistics();
        adminService.showSuccessRecord();
        // adminService.showAllRecord(); // 可放开查看全部记录
    }

    /**
     * 管理员手动加票
     */
    public void adminAddTicket(int num) {
        ticketPoolService.manualAddTicket(num);
    }
}