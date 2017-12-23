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

    private static final int SERVER_PORT = 4000;

    private static List user_list = new ArrayList();// ��¼�û�����
    private static List<ServerThread> thread_list = new ArrayList<ServerThread>();// �������������̼߳���
    private static LinkedList message_list = new LinkedList();// �����Ϣ����
 
    
    //��������
    public Server() throws IOException {
        super(SERVER_PORT);// ����ServerSocket       
        System.out.println("����������");
        try {
            while (true) {// �����ͻ�����������һ���̴߳���
                Socket socket = accept();
                new ServerThread(socket);
            }
        } catch (Exception e) {
        } finally {
            close();
        }
    }
    
    //�㲥��Ϣ
    public void broadcast() {
       while (message_list.size() > 0) { 
          String message = (String) message_list.getFirst();      
          for (ServerThread thread : thread_list) {
              thread.out.println(message);           
           }
          message_list.removeFirst();
        }
     }

    //�������߳���   
    public class ServerThread extends Thread {
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ServerThread(Socket s) throws IOException {
        	System.out.println("�����߳�");
            client = s;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            thread_list.add(this);
            out.println(name + "���,���Կ�ʼ������..." + "\n");
        
            start();
        }

        @Override
        public void run() {
            try {  	
                while (true) {
                	broadcast();
                	String line = in.readLine(); 
                    message_list.addLast(line);
                    broadcast();               
                }
            }catch (Exception e) {
				// TODO: handle exception
			}finally {
				thread_list.remove(this);
                user_list.remove(name);
               // message_list.addLast("Client<" + name + ">�˳���������");
                broadcast();
			}
        }

        // ͳ�������û��б�
        private String listOnlineUsers() {
            String s = "--- �����û��б� ---\r\n";
            for (int i = 0; i < user_list.size(); i++) {
                s += "[" + user_list.get(i) + "]\r\n";
            }
            s += "--------------------";
            return s;
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();// ���������
    }
}
