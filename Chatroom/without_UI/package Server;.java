package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Server extends ServerSocket {

    private static final int SERVER_PORT = 2017;

    private static List user_list = new ArrayList();// 登录用户集合
    private static List<ServerThread> thread_list = new ArrayList<ServerThread>();// 服务器已启用线程集合
    private static LinkedList message_list = new LinkedList();// 存放消息队列
 
    
    //服务器类
    public Server() throws IOException {
        super(SERVER_PORT);// 创建ServerSocket       
        System.out.println("服务器启动");
        try {
            while (true) {// 监听客户端请求，启用一个线程处理
                Socket socket = accept();
                new ServerThread(socket);
            }
        } catch (Exception e) {
        } finally {
            close();
        }
    }
    
    //广播消息
    public void broadcast() {
       while (message_list.size() > 0) { 
          String message = (String) message_list.getFirst();
          for (ServerThread thread : thread_list) {
              thread.out.println(message);
           }
          message_list.removeFirst();
        }
     }

    //服务器线程类   
    class ServerThread extends Thread {
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ServerThread(Socket s) throws IOException {
            client = s;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("成功连上聊天室,请输入你的名字：");
            start();
        }

        @Override
        public void run() {
            try {
                int flag = 0;
                String line = in.readLine();
                while (true) {               	
                    // 查看在线用户列表
                    if ("showuser".equals(line)) {
                        out.println(this.listOnlineUsers());
                    }
                    if("bye".equals(line)){
                        out.println("bye");
                    break;
                    }
                    // 第一次进入，保存名字
                    if (flag == 0) {
                    	flag++;
                        name = line;
                        user_list.add(name);
                        thread_list.add(this);
                        out.println(name + "你好,可以开始聊天了...");
                        message_list.addLast("Client<" + name + ">进入聊天室...");
                    } else {
                    	message_list.addLast("Client<" + name + "> say : " + line);
                    }
                    broadcast();  
                    line = in.readLine();
                    broadcast();                
                }
                
                out.println("byeClient");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {// 用户退出聊天室
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                thread_list.remove(this);
                user_list.remove(name);
                message_list.addLast("Client<" + name + ">退出了聊天室");
            }
        }

        // 统计在线用户列表
        private String listOnlineUsers() {
            String s = "--- 在线用户列表 ---\r\n";
            for (int i = 0; i < user_list.size(); i++) {
                s += "[" + user_list.get(i) + "]\r\n";
            }
            s += "--------------------";
            return s;
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();// 启动服务端
    }
}
