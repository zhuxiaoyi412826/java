package src.main.java.javase.集合.卖票.constant;


/**
 * 系统全局常量
 */
public class TicketConstants {
    // 初始总票数
    public static final int INIT_TICKET_NUM = 100;
    // 票池最大容量
    public static final int TICKET_POOL_MAX = 300;
    // 票池最小保底票数
    public static final int TICKET_POOL_MIN = 20;

    // 线程池核心大小
    public static final int CORE_POOL_SIZE = 20;
    // 最大并发限流
    public static final int MAX_RATE_LIMIT = 50;
    // 熔断触发阈值：失败请求达到30触发熔断
    public static final int BREAK_THRESHOLD = 30;
    // 熔断休眠时间 ms
    public static final long BREAK_SLEEP_TIME = 3000;

    // 排队队列最大等待人数
    public static final int QUEUE_MAX_WAIT = 200;
}