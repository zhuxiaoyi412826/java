package javase.多线程;

public class MyRunnable implements Runnable {

    
    //     //定义100张票
    // int ticket = 100;

    // @Override
    // public void run() {
    //     while(true){
    //         if (ticket>0){
    //             System.out.println(Thread.currentThread().getName()+"买了第"+ticket+"张票");
    //             ticket--;
    //         }
    //     }
    // }

    //定义100张票
    // int ticket = 100;

    // //任意new一个对象
    // Object obj = new Object();

    // @Override
    // public void run() {
    //     while(true){
    //         try {
    //             Thread.sleep(100L);
    //         } catch (InterruptedException e) {
    //             throw new RuntimeException(e);
    //         }
    //         synchronized (obj){
    //             if (ticket>0){
    //                 System.out.println(Thread.currentThread().getName()+"买了第"+ticket+"张票");
    //                 ticket--;
    //             }
    //         }

    //     }
    // }



    //定义100张票
    int ticket = 100;

    @Override
    public void run() {
        while(ticket>0){
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        //    method01();
            method02();
        }
    }

//    public synchronized void method01(){
//         if (ticket>0){
//             System.out.println(Thread.currentThread().getName()+"买了第"+ticket+"张票");
//             ticket--;
//         }
//     }


   public void method02(){
       synchronized(this){
           System.out.println(this+"..........");
           while (ticket>0){
               System.out.println(Thread.currentThread().getName()+"买了第"+ticket+"张票");
               ticket--;
           }
           return;
       }

   }

    
}
