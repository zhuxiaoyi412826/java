package javase.socket.shizhan;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 聊天服务器 - 支持多客户端连接和消息转发
 */
public class ChatServer {
    private static final int PORT = 8888;
    private static Set<ClientHandler> clients = new HashSet<>();
    private static Map<String, ClientHandler> userMap = new HashMap<>();
    private static Set<String> onlineUsers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("===== 聊天服务器已启动 =====");
            System.out.println("服务器端口: " + PORT);
            System.out.println("等待客户端连接...\n");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败: " + e.getMessage());
        }
    }

    /**
     * 广播消息给所有在线用户
     */
    public static synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * 发送私聊消息
     */
    public static synchronized boolean sendPrivateMessage(String fromUser, String toUser, String message) {
        ClientHandler targetClient = userMap.get(toUser);
        if (targetClient != null) {
            targetClient.sendMessage("[私聊][" + fromUser + "]: " + message);
            return true;
        }
        return false;
    }

    /**
     * 获取在线用户列表
     */
    public static synchronized String getOnlineUsers() {
        return String.join(", ", onlineUsers);
    }

    /**
     * 添加在线用户
     */
    public static synchronized boolean addUser(String username, ClientHandler handler) {
        if (userMap.containsKey(username)) {
            return false; // 用户名已存在
        }
        userMap.put(username, handler);
        onlineUsers.add(username);
        clients.add(handler);
        broadcast("【系统】" + username + " 进入聊天室");
        broadcast("【在线用户】" + getOnlineUsers());
        return true;
    }

    /**
     * 移除在线用户
     */
    public static synchronized void removeUser(String username, ClientHandler handler) {
        userMap.remove(username);
        onlineUsers.remove(username);
        clients.remove(handler);
        broadcast("【系统】" + username + " 离开聊天室");
        broadcast("【在线用户】" + getOnlineUsers());
    }

    /**
     * 客户端处理器
     */
    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                // 登录流程
                out.println("===== 欢迎来到聊天室 =====");
                out.println("请输入您的昵称:");
                
                while (true) {
                    username = in.readLine();
                    if (username == null || username.trim().isEmpty()) {
                        out.println("昵称不能为空，请重新输入:");
                        continue;
                    }
                    username = username.trim();
                    if (addUser(username, this)) {
                        out.println("【系统】登录成功！当前在线用户: " + getOnlineUsers());
                        out.println("【系统】输入 '@用户名 消息' 发送私聊");
                        out.println("【系统】输入 '/list' 查看在线用户");
                        out.println("【系统】输入 '/exit' 退出聊天室");
                        break;
                    } else {
                        out.println("【系统】昵称已被占用，请重新输入:");
                    }
                }

                // 消息处理循环
                String message;
                while ((message = in.readLine()) != null) {
                    if ("/exit".equalsIgnoreCase(message)) {
                        break;
                    } else if ("/list".equalsIgnoreCase(message)) {
                        out.println("【在线用户】" + getOnlineUsers());
                    } else if (message.startsWith("@")) {
                        // 私聊消息
                        int spaceIndex = message.indexOf(" ");
                        if (spaceIndex > 0) {
                            String targetUser = message.substring(1, spaceIndex);
                            String privateMsg = message.substring(spaceIndex + 1);
                            if (!sendPrivateMessage(username, targetUser, privateMsg)) {
                                out.println("【系统】用户 '" + targetUser + "' 不在线");
                            }
                        } else {
                            out.println("【系统】私聊格式错误，请输入: @用户名 消息");
                        }
                    } else {
                        // 群聊消息
                        broadcast("[" + username + "]: " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("客户端连接异常: " + e.getMessage());
            } finally {
                close();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        private void close() {
            if (username != null) {
                removeUser(username, this);
            }
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
