package Server;

import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import FileType.fileType;
import ShowImage.ShowImage;

public class Server{
	public static final int PORT = 5000;	//�˿�������1024���ϣ�������
	private ServerSocket sersock;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	JFrame frame = new JFrame("������");
	JButton receive = new JButton("�����ļ���������");
	JButton start = new JButton("��ʼ���ӿͻ���");
	JButton send = new JButton("�����ļ����ͻ���(ѡ���ļ�)");
	JButton upload = new JButton("��ʼ�����ļ�");
	JButton download = new JButton("��ʼ�����ļ�");
	
	TextField text = new TextField();
	JTextArea state = new JTextArea();
	JTextArea serverText = new JTextArea();
	JTextArea pathText = new JTextArea();
	JTextArea stateText = new JTextArea();
	
	String path = new String();
	FileInputStream fileInputStream;
	FileOutputStream fileOutputStream;
	
	public Server(){
		
		//��������
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(5, 2));
		//��� 
		serverText.setText("����������");
		serverText.setEditable(false);
		jPanel.add(serverText);
		jPanel.add(start);
		
		jPanel.add(receive);
		jPanel.add(download);
		
		jPanel.add(send);
		jPanel.add(upload);
		
		pathText.setText("�ļ�·����");
		pathText.setEditable(false);
		jPanel.add(pathText);
		jPanel.add(text);
		
		stateText.setText("״̬ ��");
		stateText.setEditable(false);
		jPanel.add(stateText);
		jPanel.add(state);
						
		frame.add(jPanel);
		frame.setBounds(200,200,500,300);
		
		//���ÿɼ���
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//��Ӽ��������ڿ�ʼ���ӿͻ���
		start.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					startConnect();
				} catch (IOException e1) {
					state.setText("���ӿͻ���ʧ��");
					e1.printStackTrace();
				}
			}
		});
		
		//��Ӽ��������ڴ�������ͨ���������ļ�����������
		receive.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					in = new DataInputStream(socket.getInputStream());
					state.setText("����ͨ�������ɹ�");
				} catch (IOException e1) {
					state.setText("����ͨ������ʧ��");
					e1.printStackTrace();
				}
			}
		});
		
		//��Ӽ��������ڿ�ʼ���شӿͻ��˴������ļ������浽��Ӧ��ַ����ʼ���أ�
		download.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//path = in.readUTF();
					
					String fileName = downloadFunction();
					state.setText("���سɹ����ļ�·��Ϊ D:\\");
					path = "D:\\" + fileName;
					File file = new File(path);
					//System.out.println("���سɹ����ļ�·��Ϊ D:\\");
					
					//��ͼƬ
					if(fileType.isImage(file)) {
						new ShowImage(path);
					}
				} catch (IOException e1) {
					state.setText("����ʧ��");
					System.out.println("����ʧ��");
					e1.printStackTrace();
				}finally {
					//��֤ͨ���ر�
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
			
		//��Ӽ���������ѡ���ļ��������ļ����ͻ��ˣ�
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
		
		//��Ӽ��������ڴ�������ͨ���������ļ�����ʼ�����ļ���
		upload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					out = new DataOutputStream(socket.getOutputStream());
					state.setText("�ϴ�ͨ�������ɹ�");
					
					File file = new File(path);						
					fileInputStream = new FileInputStream(file);
					
					//�ļ������ļ�����
					out.writeUTF(file.getName());
					out.flush();
					
					//out.writeLong(file.length());
					//out.flush();
					
					byte[] sendBytes = new byte[1024];
					int length = 0;
					while((length = fileInputStream.read(sendBytes, 0, sendBytes.length)) > 0) {
						out.write(sendBytes,0,length);
						out.flush();	
					}
					state.setText("�ϴ��ɹ�");
					
				} catch (IOException e1) {
					state.setText("�ϴ�ͨ������ʧ��");
					e1.printStackTrace();
				}
				finally {
					//ȷ��ͨ�����ر�
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
	
	
	//���������ļ�
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
	
	//�������ӿͻ���
	public void startConnect() throws IOException {				
		try {
			if(sersock == null)	{
				sersock = new ServerSocket(PORT);
			}
			System.out.println("�������ɹ�����");
			state.setText("�������ɹ�����, ���ڵȴ�����...");
			while(true) {
				socket = sersock.accept();
				if(socket.isConnected()) {
					System.out.println("�����ӿͻ���");
					state.setText("�����ӿͻ���");
					break;
				}
			}
			
		}
		catch (IOException e) {
			System.err.println("���������������쳣");
			state.setText("���������������쳣");
		}
		
		
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
