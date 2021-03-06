package Client;

import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import FileType.fileType;
import ShowImage.ShowImage;

public class Client {
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	public static final String IPADDRESS = "localhost";	//服务器地址
	
	JFrame frame = new JFrame("用户端");
	JButton receive = new JButton("下载文件到用户端");
	JButton start = new JButton("开始连接服务端");
	JButton send = new JButton("发送文件到服务端(选择文件)");
	JButton upload = new JButton("开始发送文件");
	JButton download = new JButton("开始下载文件");
	
	TextField text = new TextField();
	JTextArea state = new JTextArea();
	JTextArea serverText = new JTextArea();
	JTextArea pathText = new JTextArea();
	JTextArea stateText = new JTextArea();
	
	String path = new String();
	FileInputStream fileInputStream;
	FileOutputStream fileOutputStream;
	
	public Client(){		
		//布局
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(5, 2));
		//添加 
		serverText.setText("用户端界面");
		serverText.setEditable(false);
		jPanel.add(serverText);
		jPanel.add(start);
		
		jPanel.add(receive);
		jPanel.add(download);
		
		jPanel.add(send);
		jPanel.add(upload);
		
		pathText.setText("文件路径：");
		pathText.setEditable(false);
		jPanel.add(pathText);
		jPanel.add(text);
		
		stateText.setText("状态 ：");
		stateText.setEditable(false);
		jPanel.add(stateText);
		jPanel.add(state);
						
		frame.add(jPanel);
		frame.setBounds(200,200,500,300);
		
		//设置可见性
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//添加监听，用于开始连接服务器
		start.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					startConnect();
				} catch (IOException e1) {
					state.setText("连接服务器失败");
					e1.printStackTrace();
				}
			}
		});
		
		//添加监听，用于创建下载通道（下载文件到客户端）
		receive.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					in = new DataInputStream(socket.getInputStream());
					state.setText("下载通道创建成功");
				} catch (IOException e1) {
					state.setText("下载通道创建失败");
					e1.printStackTrace();
				}
			}
		});
		
		//添加监听，用于开始下载从服务器传来的文件并保存到相应地址（开始下载）
		download.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//path = in.readUTF();
										
					String fileName = downloadFunction();
					state.setText("下载成功，文件路径为 D:\\");
					path = "D:\\" + fileName;
					File file = new File(path);
					//System.out.println("下载成功，文件路径为 D:\\");
					
					//是图片
					if(fileType.isImage(file)) {
						new ShowImage(path);
					}
					
				} catch (IOException e1) {
					state.setText("下载失败");
					System.out.println("下载失败");
					e1.printStackTrace();
				}finally {
					//保证通道关闭
					try {
						in.close();
					} catch (IOException e1) {						
						e1.printStackTrace();
					}
					
					try {
						fileOutputStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		
		});
		
		//添加监听，用于选择文件（发送文件到服务器）
		send.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechoose = new JFileChooser();
				if(filechoose.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					path = filechoose.getSelectedFile().getAbsolutePath();
					text.setText(path);
				}
						
			}
		});
		
		//添加监听，用于创建发送通道并发送文件（开始发送文件）
		upload.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					out = new DataOutputStream(socket.getOutputStream());
					state.setText("上传通道创建成功");
					out.writeUTF(path);
					
					File file = new File(path);
					
					fileInputStream = new FileInputStream(file);
							
					//文件名和文件长度
					out.writeUTF(file.getName());
					out.flush();
							
//					out.writeLong(file.length());
//					out.flush();
							
					byte[] sendBytes = new byte[1024];
					int length = 0;
					while((length = fileInputStream.read(sendBytes, 0, sendBytes.length)) > 0) {
						out.write(sendBytes,0,length);
						out.flush();	
					}
					state.setText("上传成功");
							
				} catch (IOException e1) {
					state.setText("上传通道创建失败");
					e1.printStackTrace();
				}
				finally {
					//确保通道均关闭
					if(fileInputStream != null)
						try {
							fileInputStream.close();
						} catch (IOException e1) {			
							e1.printStackTrace();
						}
					if(out != null)
						try {
							out.close();
						} catch (IOException e1) {	
							e1.printStackTrace();
						}
							
				}
						
						
			}
		});
	
	}
	
	//用于下载文件
	public String downloadFunction()throws IOException{
		String fileName = in.readUTF();
		
		fileOutputStream = new FileOutputStream(new File("d:/" + fileName));
		byte[] sendBytes = new byte[1024];
		while(true) {
			int read = 0;
			read = in.read(sendBytes); 		
			if(read == -1)	break;
			fileOutputStream.write(sendBytes, 0, read);
			fileOutputStream.flush();
		}
		return fileName;
	}
	
	//用于连接服务端
	public void startConnect() throws IOException {
		
		try {
			socket = new Socket(IPADDRESS,5000);
			System.out.println("客户端成功启动");
			state.setText("客户端成功启动, 连接成功！");
			
		}
		catch (IOException e) {
			System.err.println("客户端启动出现异常");
			state.setText("客户端启动出现异常");
		}
		
		
	}
	
	
	public static void  main(String[] args) throws IOException {
		new Client();
		
	}
	
}
