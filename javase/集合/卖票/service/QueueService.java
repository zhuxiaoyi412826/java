package src.main.java.javase.集合.卖票.service;


import src.main.java.javase.集合.卖票.constant.TicketConstants;
import src.main.java.javase.集合.卖票.entity.User;
import src.main.java.javase.集合.卖票.util.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 抢票排队机制：阻塞队列实现排队等候
 */
public class QueueService {
    // 阻塞队列 排队等候抢票的用户
    private final BlockingQueue<User> waitQueue;

    public QueueService() {
        waitQueue = new ArrayBlockingQueue<>(TicketConstants.QUEUE_MAX_WAIT);
    }

    /**
     * 用户进入排队
     */
    public boolean enQueue(User user) {
        boolean offer = waitQueue.offer(user);
        if (!offer) {
            LogUtil.limit("排队队列已满，当前人数超出最大等待限制，无法加入排队：" + user.getUserName());
        } else {
            LogUtil.info("用户 " + user.getUserName() + " 进入抢票排队队列，当前排队人数：" + waitQueue.size());
        }
        return offer;
    }

    /**
     * 取出排队用户
     */
    public User deQueue() {
        return waitQueue.poll();
    }

    /**
     * 获取当前排队人数
     */
    public int getWaitCount() {
        return waitQueue.size();
    }
}