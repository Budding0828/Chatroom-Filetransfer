package ShowImage;


import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowImage extends JFrame{
	public ShowImage(String path) {
		
		//��ʾͼƬ
		ImageIcon imageIcon = new ImageIcon(path);
		
		//���ô�С
		setTitle("��ʾͼƬ");
		setSize(300,400);
		
		//�������
		JPanel showjpanel = new JPanel();
		showjpanel.setLayout(new FlowLayout());
		
		//�����ı���ʾ
		TextField showtext = new TextField();
		showtext.setText("������ļ�����ΪͼƬ");
		showtext.setEditable(false);
		
		//���õ����ť
		JButton showbutton = new JButton("�����ʾ����ͼƬ");
		
		showbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				//������ʾ��label
				JLabel showlabel = new JLabel();
				showlabel.setIcon(imageIcon);
				
				showjpanel.add(showlabel);
				setSize(imageIcon.getIconHeight()+20, imageIcon.getIconWidth()+30);
				repaint();
			}
		});
		

		
		
		showjpanel.add(showtext);
		showjpanel.add(showbutton);

		add(showjpanel);		
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
}
