package src.main.java.javase.集合.卖票.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void info(String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] [系统] " + msg);
    }

    public static void success(String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] [成功] " + msg);
    }

    public static void fail(String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] [失败] " + msg);
    }

    public static void limit(String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] [限流] " + msg);
    }

    public static void breakDown(String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] [熔断] " + msg);
    }
}