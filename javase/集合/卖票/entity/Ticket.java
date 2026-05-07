package src.main.java.javase.集合.卖票.entity;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 车票实体 + 原子票数控制
 */
public class Ticket {
    // 总票数
    private final int totalTicket;
    // 剩余票数 原子类保证并发安全
    private final AtomicInteger remainTicket;

    public Ticket(int totalTicket) {
        this.totalTicket = totalTicket;
        this.remainTicket = new AtomicInteger(totalTicket);
    }

    /**
     * 抢票 CAS 无锁
     */
    public boolean grabTicket() {
        while (true) {
            int current = remainTicket.get();
            if (current <= 0) {
                return false;
            }
            // CAS 减1
            if (remainTicket.compareAndSet(current, current - 1)) {
                return true;
            }
        }
    }

    /**
     * 动态增加票数
     */
    public synchronized void addTicket(int num) {
        remainTicket.addAndGet(num);
    }

    public int getRemainTicket() {
        return remainTicket.get();
    }

    public int getTotalTicket() {
        return totalTicket;
    }
}