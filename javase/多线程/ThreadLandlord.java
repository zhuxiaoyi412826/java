package javase.多线程;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * 多线程版斗地主
 * 3个线程模拟3个玩家，线程安全洗牌、发牌、出牌
 */
public class ThreadLandlord {
    // 线程安全的牌堆（所有玩家共享）
    private static final Vector<String> cardPool = new Vector<>();
    // 3个玩家的手牌 + 底牌
    private static final Vector<String> player1 = new Vector<>();
    private static final Vector<String> player2 = new Vector<>();
    private static final Vector<String> player3 = new Vector<>();
    private static final Vector<String> bottomCards = new Vector<>();

    // 初始化扑克牌（54张：花色+数字+大小王）
    static {
        String[] colors = {"♠", "♥", "♣", "♦"};
        String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
        
        // 组装普通牌
        for (String color : colors) {
            for (String num : numbers) {
                cardPool.add(color + num);
            }
        }
        // 添加大小王
        cardPool.add("小王");
        cardPool.add("大王");
    }

    // 洗牌（线程安全）
    private static void shuffleCards() {
        System.out.println("===== 系统开始洗牌 =====");
        Collections.shuffle(cardPool);
    }

    // 发牌（轮流发牌，最后留3张底牌，synchronized保证线程安全）
    private static synchronized void dealCards() {
        System.out.println("===== 系统开始发牌 =====");
        for (int i = 0; i < cardPool.size(); i++) {
            String card = cardPool.get(i);
            // 最后3张作为底牌
            if (i >= cardPool.size() - 3) {
                bottomCards.add(card);
            } else if (i % 3 == 0) {
                player1.add(card);
            } else if (i % 3 == 1) {
                player2.add(card);
            } else {
                player3.add(card);
            }
        }
    }

    // 玩家线程类（实现出牌逻辑）
    static class Player extends Thread {
        private final String name;
        private final Vector<String> handCards;

        public Player(String name, Vector<String> handCards) {
            this.name = name;
            this.handCards = handCards;
        }

        @Override
        public void run() {
            // 1. 理牌（排序）
            sortCards();
            System.out.println(name + " 理牌完成：" + handCards);

            // 2. 轮流出牌（线程安全，模拟依次出牌）
            playCards();
        }

        // 理牌：简单排序（按斗地主牌力大小）
        private void sortCards() {
            List<String> order = List.of("3","4","5","6","7","8","9","10","J","Q","K","A","2","小王","大王");
            handCards.sort((a, b) -> {
                int indexA = order.indexOf(a.replaceAll("[♠♥♣♦]", ""));
                int indexB = order.indexOf(b.replaceAll("[♠♥♣♦]", ""));
                return Integer.compare(indexA, indexB);
            });
        }

        // 出牌（synchronized 保证同一时间只有一个玩家出牌）
        private synchronized void playCards() {
            System.out.println("\n>>>>> " + name + " 开始出牌 <<<<<");
            // 模拟依次打出所有手牌
            while (!handCards.isEmpty()) {
                String outCard = handCards.remove(0);
                System.out.println(name + " 打出：" + outCard);
                try {
                    // 模拟出牌间隔，更真实
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name + " 手牌已出完！");
        }
    }

    public static void main(String[] args) {
        // 1. 洗牌
        shuffleCards();

        // 2. 发牌
        dealCards();
        System.out.println("底牌：" + bottomCards);

        // 3. 创建3个玩家线程
        Player p1 = new Player("玩家1", player1);
        Player p2 = new Player("玩家2", player2);
        Player p3 = new Player("玩家3", player3);

        // 4. 启动线程（开始游戏）
        System.out.println("\n===== 斗地主游戏开始 =====");
        p1.start();
        p2.start();
        p3.start();
    }
}
