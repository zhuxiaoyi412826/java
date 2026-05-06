package src.main.java.javase.异常;

import java.io.FileNotFoundException;

public class Demo06Exception {
    public static void main(String[] args) {
        String s ="a.txt";
        try {
             add(s);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
              System.out.println("我要执行了");
        }finally {
            System.out.println("我必须滴执行");
        }
      
      
    }
    public static void add(String s) throws FileNotFoundException {
        System.out.println("添加功能");
        if (!s.endsWith(".txt")) {
            //故意创建异常
            throw new FileNotFoundException("文件找不到");
        }
        System.out.println("我要执行了");
    }

}
