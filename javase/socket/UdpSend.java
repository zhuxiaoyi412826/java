package javase.socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UdpSend {
    public static void main(String[] args) throws IOException {
        //1.创建UDP发送端Socket对象
        DatagramSocket ds = new DatagramSocket();
        //2.创建数据并打包 - 使用BufferedReader处理中文输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println("请输入要发送的内容：");
        String s = br.readLine(); // 获取用户输入的字符串
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, address, 12345);
        //3.发送数据
        ds.send(dp);
        System.out.println("已发送：" + s);
        //4.关闭资源
        br.close();
        ds.close();
    }
}