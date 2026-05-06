package src.main.java.javase.异常;

public class Demo09Exception {
    public static void main(String[] args) {
        int result = method();
        System.out.println(result);
    }

    public static int method() {
       
         String s = null;
            System.out.println(s.length());//空指针异常
            return 2;
      
        finally {
            System.out.println("我必须滴执行");
        }

}
}