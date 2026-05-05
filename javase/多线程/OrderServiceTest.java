package javase.多线程;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 多线程综合案例 + 自带测试入口
 * 知识点：线程池、阻塞队列、生产者消费者、多线程协作、线程中断
 */
public class OrderServiceTest {

    // 阻塞队列缓冲区：容量10
    private static final ArrayBlockingQueue<Integer> orderQueue = new ArrayBlockingQueue<>(10);
    // 订单自增编号
    private static int orderNo = 1;

    // 生产者：生成订单
    static class Producer implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Integer order = orderNo++;
                    orderQueue.put(order);
                    System.out.println("生产者[" + Thread.currentThread().getName()
                            + "] 生成订单：" + order + " 队列库存：" + orderQueue.size());
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println("生产者线程终止");
                    break;
                }
            }
        }
    }

    // 消费者：处理订单
    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Integer order = orderQueue.take();
                    System.out.println("消费者[" + Thread.currentThread().getName()
                            + "] 处理订单：" + order + " 队列剩余：" + orderQueue.size());
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("消费者线程终止");
                    break;
                }
            }
        }
    }

    // 测试方法：main 就是测试入口
    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== 多线程生产者消费者测试开始 =====");

        // 创建固定大小线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);

        // 提交2个生产者任务
        pool.execute(new Producer());
        pool.execute(new Producer());

        // 提交3个消费者任务
        pool.execute(new Consumer());
        pool.execute(new Consumer());
        pool.execute(new Consumer());

        // 测试运行5秒，然后关闭所有线程
        TimeUnit.SECONDS.sleep(5);

        // 中断所有线程、停止任务
        pool.shutdownNow();
        System.out.println("===== 测试结束 =====");
    }
}
