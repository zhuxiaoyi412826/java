package javase.多线程;

public class demo04 {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();

        /*
           Thread(Runnable target)
         */
        Thread t1 = new Thread(myRunnable);
        //调用Thread中的start方法,开启线程

    
      

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+"...执行了"+i);
        }
          t1.start();
    }
}
