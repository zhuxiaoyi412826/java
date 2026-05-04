package javase.io;

import java.io.File;

public class Demo04 {
    public static void main(String[] args) {
        // 1. 创建File对象（代表文件路径）
        File file = new File("D:/1/java/io/hello.txt");

        // 2. 创建File对象（代表文件夹路径）
        File dir1 = new File("D:/javaTest");          // 单级文件夹
        File dir2 = new File("D:/javaTest/a/b/c");    // 多级文件夹


        // ========== 1. 判断是否存在 ==========
        if (!dir1.exists()) {
            // 创建单级文件夹
            dir1.mkdir();
            System.out.println("单级文件夹创建成功");
        }

        if (!dir2.exists()) {
            // 创建多级文件夹 推荐用mkdirs()
            dir2.mkdirs();
            System.out.println("多级文件夹创建成功");
        }


        // ========== 2. 创建空文件 ==========
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("文件创建成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // ========== 3. 常用信息获取 ==========
        System.out.println("文件名：" + file.getName());
        System.out.println("绝对路径：" + file.getAbsolutePath());
        System.out.println("文件大小(字节)：" + file.length());

        // 判断是文件还是文件夹
        System.out.println("是否是文件：" + file.isFile());
        System.out.println("是否是文件夹：" + dir1.isDirectory());


        // ========== 4. 遍历文件夹下所有文件 ==========
        File parentDir = new File("C:\\Program Files (x86)");
        File[] files = parentDir.listFiles();
        System.out.println("\n===== 遍历文件夹内容 =====");
        if (files != null) {
            for (File f : files) {
                System.out.println(f.getName() + "  " + (f.isFile() ? "文件" : "文件夹"));
            }
        }


        // ========== 5. 删除文件 ==========
        // file.delete();  // 删除文件
        // dir1.delete(); // 只能删除空文件夹
    }
}
