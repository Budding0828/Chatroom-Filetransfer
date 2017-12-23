package UI;
//交互界面
import Server.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ClientUI {
	//相关控件声明
	private JFrame frame = new JFrame("聊天室_客户端");
	private JTextArea textArea = new JTextArea();
	private JTextField textField = new JTextField();
	private JTextField txt_port = new JTextField("5000");
	private JTextField txt_hostIP = new JTextField("127.0.0.1");
	private JTextField txt_name = new JTextField();
	private JButton btn_start = new JButton("连接");
	private JButton btn_stop = new JButton("断开");
	private JButton btn_send = new JButton("发送");
	private JPanel northPanel;
	private JPanel southPanel;
	private JScrollPane rightScroll;
	private JScrollPane leftScroll ;
	private JSplitPane centerSplit ;
	private String userName;
		
	
	private JList<String> userlist ;
	
	//通讯声明
	public Socket client;
	private PrintWriter out;
	private BufferedReader in;
	
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 4000;
	private boolean isConnected = false;
	
	public ClientUI(String userName) {
		textArea.setEditable(false);
		textArea.setForeground(Color.BLUE);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JPanel textAreaScroll = new JPanel();
		textAreaScroll.add(new JScrollPane(textArea));
		
		//设置用户名
		this.userName = userName;
		txt_name.setText(userName);
		userlist = new JList<>();
		
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 7));
		northPanel.add(new JLabel("端口"));
		northPanel.add(txt_port);
		txt_port.setEditable(false);
		
		northPanel.add(new JLabel("服务器IP"));
		northPanel.add(txt_hostIP);
		txt_hostIP.setEditable(false);
		
		northPanel.add(new JLabel("姓名"));
		northPanel.add(txt_name);
		txt_name.setEditable(false);
		
		northPanel.add(btn_start);
		northPanel.add(btn_stop);
		northPanel.setBorder(new TitledBorder("连接信息"));

		rightScroll = new JScrollPane(textArea);
		rightScroll.setBorder(new TitledBorder("消息显示区"));
		leftScroll = new JScrollPane(userlist);
		leftScroll.setBorder(new TitledBorder("在线用户"));
		
		southPanel = new JPanel(new BorderLayout());
		southPanel.add(textField, "Center");
		southPanel.add(btn_send, "East");
		southPanel.setBorder(new TitledBorder("写消息"));
		
		//设置排版
		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
		centerSplit.setDividerLocation(100);
		
		frame.setLayout(new BorderLayout());;
		frame.add(northPanel, "North");
		frame.add(centerSplit, "Center");
		frame.add(southPanel, "South");
		frame.setSize(600, 400);
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		textArea.append("--- 欢迎来到聊天室 --- " + "\n");

		
		//以下开始添加监听器
		
		//连接按钮
		btn_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//启动连接
				startConnect();
				isConnected = true;
			}
		});
		
		//退出按钮
		btn_stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				out.println(userName + "离开了聊天室" + "\n");
				isConnected = false;
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				out.close();
				try {
					in.close();
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//发送消息
		btn_send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = textField.getText();
				out.println("Client<" + userName + "> say :" + input);	
				textField.setText("");		
			}
		});
	}
	
	//接受消息部分
	class readLineThread extends Thread{
		private BufferedReader buff;
		public readLineThread() {
			try {
				buff = new BufferedReader(new InputStreamReader(client.getInputStream()));
				start();
			}catch (Exception e) {
			}
		}
	
		@Override
        public void run() {
            try {
                while(true){
                    String result = buff.readLine();
                    if(!isConnected){
                        break;
                    }else{
                        textArea.append(result + '\n');
                    }
                }
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
            }
        } 

	}

	//开始连接
	public void startConnect() {
		try {
			client = new Socket(SERVER_IP, SERVER_PORT);
			out = new PrintWriter(client.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
			textArea.append("连接成功" + '\n');
			out.println("Client<" + userName + "> 进入了聊天室" + '\n');
		
		} catch (Exception e) {
			//System.out.println("连接失败");
			textArea.append("连接失败\n");
		}
		
		//开始监听发送来的消息
		readLineThread reLineThread = new readLineThread();

	}
	

	public static void main(String[] args) throws IOException {
		
		JFrame nameJrame = new JFrame("注册");
		JPanel nameJpanel = new JPanel();
		nameJpanel.setLayout(new GridLayout(2, 1));
		
		nameJrame.setBounds(500, 300, 300, 150);
		JTextField inputName = new JTextField();
		inputName.setBorder(new TitledBorder("请输入你的名字："));
		JButton btn_name = new JButton("确定");
		
		nameJpanel.add(inputName);
		nameJpanel.add(btn_name);
		nameJrame.add(nameJpanel);
		nameJrame.setVisible(true);
		nameJrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btn_name.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = inputName.getText();
				nameJrame.setVisible(false);
				new ClientUI(userName);		
			}
		});
		
		
	}

}



