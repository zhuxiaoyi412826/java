package javase.socket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UdpReceive {
    public static void main(String[] args) throws IOException {
        //1.创建UDP接收端Socket对象
        DatagramSocket ds = new DatagramSocket(12345);
        //2.创建数据并接收
        byte[] bytes = new byte[1024];
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
        ds.receive(dp);
        //3.解析数据 - 使用UTF-8解码
        String msg = new String(dp.getData(), 0, dp.getLength(), StandardCharsets.UTF_8);
        System.out.println("接收的内容：" + msg);
        //4.关闭资源
        ds.close();
    }
}