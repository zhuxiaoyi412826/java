package javase.多线程;

import java.util.ArrayList;
import java.util.List;

public class PA1 {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        // 生产者线程
        new Thread(() -> {
            int i = 1;
            while (true) {
                try {
                    warehouse.produce(i++);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "生产者").start();

        // 消费者线程
        new Thread(() -> {
            while (true) {
                try {
                    warehouse.consume();
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "消费者").start();
    }
}
class Warehouse {
    // 存放产品
    private final List<Integer> list = new ArrayList<>();
    // 仓库最大容量
    private static final int MAX_SIZE = 5;

    // 生产
    public synchronized void produce(int num) throws InterruptedException {
        // 仓库满了，生产者等待
        while (list.size() == MAX_SIZE) {
            wait();
        }
        list.add(num);
        System.out.println("生产者生产：" + num + " 仓库库存：" + list.size());
        // 唤醒消费者
        notifyAll();
    }

    // 消费
    public synchronized void consume() throws InterruptedException {
        // 仓库空了，消费者等待
        while (list.isEmpty()) {
            wait();
        }
        Integer num = list.remove(0);
        System.out.println("消费者消费：" + num + " 仓库库存：" + list.size());
        // 唤醒生产者
        notifyAll();
    }
}