package javase.io;

import java.io.File;
import java.io.IOException;

public class demo1 {
    public static void main(String[] args) throws IOException{
    //    file01();
    // file02();
        //   file03();
        //  file04();
        // 遍历文件夹下所有.jpg文件
         // 1.创建File对象,指定要遍历的文件夹路径
        // File file = new File("C:\\Users\\DELL\\Desktop\\笔记\\数据结构算法\\img\\数据结构");
        // method(file);
 File file = new File("F:\\");
        method2(file);
   

//     System.out.println("------------------");
//    String path2 ="E:"+File.separator+"java"+File.separator+"io";
//              System.out.println(path2);
    }
    // private static void file01() {
    //     String pathSeparator =File.pathSeparator;
    //     System.out.println("pathSeparator:"+pathSeparator);
    //     String separator =File.separator;
    //     System.out.println("separator:"+separator);
    // }
    //  private static void file02() {
    //     //String getAbsolutePath() -> 获取File的绝对路径->带盘符的路径
    //     File file1 = new File("1.txt");
    //     System.out.println("file1.getAbsolutePath() = " + file1.getAbsolutePath());
    //     //String getPath() ->获取的是封装路径->new File对象的时候写的啥路径,获取的就是啥路径
    //     File file2 = new File("io\\1.txt");
    //     System.out.println("file2.getPath() = " + file2.getPath());
    //     //String getName()  -> 获取的是文件或者文件夹名称
    //     File file3 = new File("E:\\Idea\\io\\1.txt");
    //     System.out.println("file3.getName() = " + file3.getName());
    //     //long length() -> 获取的是文件的长度 -> 文件的字节数  ->文件夹没有长度
    //     File file4 = new File("1.txt");
    //     System.out.println("file4.length() = " + file4.length());
    // }
    // private static void file03() throws IOException {
    //     /*boolean createNewFile()  -> 创建文件
    //     如果要创建的文件之前有,创建失败,返回false
    //     如果要创建的文件之前没有,创建成功,返回true*/
    //     File file1 = new File("D:\\1\\java\\2.txt");
    //     System.out.println("file1.createNewFile() = " + file1.createNewFile()); 
    //         File file11 = new File("D:\\1\\java\\1.txt");
    //     System.out.println("file11.createNewFile() = " + file11.createNewFile());

    //     /*boolean mkdirs() -> 创建文件夹(目录)既可以创建多级文件夹,还可以创建单级文件夹
    //     如果要创建的文件夹之前有,创建失败,返回false
    //     如果要创建的文件夹之前没有,创建成功,返回true*/
    //     File file2 = new File("D:\\1\\java\\案例");
    //     System.out.println("file2.mkdirs() = " + file2.mkdirs());
    //     File file22 = new File("D:\\1\\java\\案例1");
    //     System.out.println("file22.mkdirs() = " + file22.mkdirs());
    // }

    //   private static void file04() {
    //     File file = new File("D:\\1\\java\\javase\\io");
    //     //String[] list() -> 遍历指定的文件夹,返回的是String数组
    //     String[] list = file.list();
    //     for (String s : list) {
    //         System.out.println(s);
    //     }
    //     //File[] listFiles()-> 遍历指定的文件夹,返回的是File数组 ->这个推荐使用
    //     System.out.println("==============");
    //     File[] files = file.listFiles();
    //     for (File file1 : files) {
    //         System.out.println(file1);
    //     }


    // private static void method(File file) {
    //    // 2.调用listFiles(),遍历文件夹,返回File数组
    //     File[] files = file.listFiles();
    //     // 3.遍历File数组,在遍历的过程中判断,如果是文件,获取文件名,判断是否以.jpg结尾的 如果是,输出
    //     for (File file1 : files) {
    //         if (file1.isFile()){
    //             String name = file1.getName();
    //             if (name.endsWith(".png")){
    //                 System.out.println(name);
    //             }
    //         }else {
    //    // 4.否则证明是文件夹,继续调用listFiles,遍历文件夹,然后重复 2 3 4步骤 -> 递归
    //            method(file1);
    //         }
    //     }


    // }

// 拿不到目录列表（没权限 / 系统目录）→ 直接跳过，不遍历。
     public static void method2(File file) {
        // 1. 不是目录直接跳过
        if (!file.isDirectory()) {
            return;
        }

        // 2. 关键：先判 null！！
        File[] files = file.listFiles();
        if (files == null) {
            // 权限不足/系统目录，直接跳过不遍历
            return;
        }

        // 3. 现在可以安全遍历
        for (File f : files) {
            if (f.isFile()) {
                // 判断图片后缀
                String name = f.getName().toLowerCase();
                if (name.endsWith(".jpg") || name.endsWith(".jpeg")
                    || name.endsWith(".png") || name.endsWith(".gif")
                    || name.endsWith(".bmp")) {
                    System.out.println(f.getAbsolutePath());
                }
            } else {
                // 是文件夹，继续递归
                method2(f);
            }
        }

}
      }