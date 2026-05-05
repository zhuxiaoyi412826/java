package javase.多线程;

public class demo03 {
     public static void main(String[] args) {
        MyThread1 t1 = new MyThread1();
        t1.setName("金莲");

        // MyThread1 t2 = new MyThread1();
        // t2.setName("阿庆");
        MyThread2 t2 = new MyThread2();
        t2.setName("阿庆");

        /*
           获取两个线程的优先级
           MIN_PRIORITY = 1 最小优先级 1
           NORM_PRIORITY = 5 默认优先级 5
           MAX_PRIORITY = 10 最大优先级 10
         */
      

        //设置优先级
        // t1.setPriority(10);
        // t2.setPriority(1);
        //   System.out.println(t1.getPriority());
        // System.out.println(t2.getPriority());

        // t2.setDaemon(true);//设置守护线程
        // t2.setDaemon(true);
        t1.start();
        t2.start();

    
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+"执行了......"+i);
        }
        
    }

     public static class MyThread1 extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+"执行了......"+i);
        }
    }
}

public static class MyThread2 extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+"执行了..."+i);
        }
    }
}

}
