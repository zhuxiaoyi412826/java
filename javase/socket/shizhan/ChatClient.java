package javase.socket.shizhan;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * 聊天客户端
 */
public class ChatClient {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("===== 连接到聊天室 =====");

            // 创建读取服务器消息的线程
            new Thread(new ServerMessageReader(socket)).start();

            // 发送消息
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            String message;
            while (true) {
                message = userInput.readLine();
                if (message == null || "/exit".equalsIgnoreCase(message)) {
                    out.println("/exit");
                    break;
                }
                out.println(message);
            }

            System.out.println("已退出聊天室");
        } catch (ConnectException e) {
            System.out.println("无法连接到服务器，请确保服务器已启动");
        } catch (IOException e) {
            System.out.println("连接异常: " + e.getMessage());
        }
    }

    /**
     * 读取服务器消息的线程
     */
    static class ServerMessageReader implements Runnable {
        private Socket socket;

        public ServerMessageReader(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                // 服务器断开连接
            }
        }
    }
}
