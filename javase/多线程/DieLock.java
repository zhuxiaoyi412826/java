package javase.多线程;

public class DieLock implements Runnable {
 private boolean flag;

    public DieLock(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag){
            synchronized (lockA.lockA){
                System.out.println("if...lockA");
                synchronized (lockB.lockB){
                    System.out.println("if...lockB");
                }
            }
        }else{
            synchronized (lockB.lockB){
                System.out.println("else...lockB");
                synchronized (lockA.lockA){
                    System.out.println("else...lockA");
                }
            }
        }
    }
    
}
