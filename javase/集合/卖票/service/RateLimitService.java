package src.main.java.javase.集合.卖票.service;


import src.main.java.javase.集合.卖票.constant.TicketConstants;
import src.main.java.javase.集合.卖票.exception.BreakException;
import src.main.java.javase.集合.卖票.exception.LimitException;
import src.main.java.javase.集合.卖票.util.LogUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流 + 熔断机制
 * 1. 限流：限制同时并发请求数
 * 2. 熔断：失败次数达到阈值直接拒绝请求
 */
public class RateLimitService {
    // 当前并发请求数
    private final AtomicInteger currentRequest = new AtomicInteger(0);
    // 失败请求计数
    private final AtomicInteger failCount = new AtomicInteger(0);
    // 熔断开关
    private volatile boolean isBreak = false;

    /**
     * 尝试获取请求许可
     */
    public synchronized boolean tryPass() {
        // 已熔断直接拒绝
        if (isBreak) {
            LogUtil.breakDown("系统已触发熔断，暂时拒绝所有抢票请求");
            throw new BreakException("系统熔断，暂时无法抢票");
        }

        // 限流判断
        int now = currentRequest.get();
        if (now >= TicketConstants.MAX_RATE_LIMIT) {
            LogUtil.limit("当前并发人数已达限流阈值：" + TicketConstants.MAX_RATE_LIMIT + "，拒绝本次抢票");
            throw new LimitException("当前抢票人数过多，请稍后再试");
        }

        currentRequest.incrementAndGet();
        return true;
    }

    /**
     * 请求结束释放计数
     */
    public void release(boolean isSuccess) {
        currentRequest.decrementAndGet();
        if (!isSuccess) {
            int fail = failCount.incrementAndGet();
            // 失败达到阈值触发熔断
            if (fail >= TicketConstants.BREAK_THRESHOLD) {
                isBreak = true;
                LogUtil.breakDown("失败请求累计达" + fail + "次，触发服务熔断");
                // 开启定时恢复熔断
                new Thread(this::recoverBreak).start();
            }
        } else {
            // 成功一次清空失败计数
            failCount.set(0);
        }
    }

    /**
     * 熔断恢复
     */
    private void recoverBreak() {
        try {
            Thread.sleep(TicketConstants.BREAK_SLEEP_TIME);
            isBreak = false;
            failCount.set(0);
            LogUtil.info("熔断时间结束，系统恢复正常抢票");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getCurrentRequest() {
        return currentRequest.get();
    }

    public boolean isBreak() {
        return isBreak;
    }
}
