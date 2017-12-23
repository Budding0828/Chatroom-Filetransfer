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
		
		//显示图片
		ImageIcon imageIcon = new ImageIcon(path);
		
		//设置大小
		setTitle("显示图片");
		setSize(300,400);
		
		//添加容器
		JPanel showjpanel = new JPanel();
		showjpanel.setLayout(new FlowLayout());
		
		//设置文本提示
		TextField showtext = new TextField();
		showtext.setText("传输的文件类型为图片");
		showtext.setEditable(false);
		
		//设置点击按钮
		JButton showbutton = new JButton("点击显示传输图片");
		
		showbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				//设置显示的label
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
