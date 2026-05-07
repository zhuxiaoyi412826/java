package src.main.java.javase.集合.卖票.service;


import src.main.java.javase.集合.卖票.constant.TicketConstants;
import src.main.java.javase.集合.卖票.entity.Ticket;
import src.main.java.javase.集合.卖票.util.LogUtil;

/**
 * 票池动态管理
 * 自动监控剩余票数，低于阈值自动加票，高于上限不追加
 */
public class TicketPoolService {
    private final Ticket ticket;

    public TicketPoolService(Ticket ticket) {
        this.ticket = ticket;
        // 启动后台监控线程 动态维护票池
        startPoolMonitor();
    }

    /**
     * 后台监控线程 动态增减票
     */
    private void startPoolMonitor() {
        new Thread(() -> {
            while (true) {
                int remain = ticket.getRemainTicket();
                // 低于最小保底，自动补票
                if (remain < TicketConstants.TICKET_POOL_MIN) {
                    int addNum = TicketConstants.TICKET_POOL_MAX - remain;
                    ticket.addTicket(addNum);
                    LogUtil.info("票池余量不足，自动补充票数：" + addNum + "，当前剩余：" + ticket.getRemainTicket());
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "票池动态监控线程").start();
    }

    /**
     * 手动增加票数 管理员后台调用
     */
    public synchronized void manualAddTicket(int num) {
        int now = ticket.getRemainTicket();
        if (now + num > TicketConstants.TICKET_POOL_MAX) {
            LogUtil.info("超出票池最大容量，无法追加，当前剩余：" + now);
            return;
        }
        ticket.addTicket(num);
        LogUtil.info("管理员手动追加票数：" + num + "，当前剩余票数：" + ticket.getRemainTicket());
    }

    public int getRemainTicket() {
        return ticket.getRemainTicket();
    }
}
