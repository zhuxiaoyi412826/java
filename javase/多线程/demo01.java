package javase.多线程;

public class demo01 {
    public static void main(String[] args) {
        //创建线程对象
        MyThread t1 = new MyThread();
        //调用start方法,开启线程,jvm自动调用run方法
        t1.start();
        try {
            t1.join();//等待t1线程执行完毕
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("main线程..........执行了"+i);
        }
    }
    public static class MyThread extends Thread{
        //重写run方法
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("MyThread...执行了"+i);
        }
    }

    }
}