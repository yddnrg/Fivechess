package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/*
 * 大厅界面
 */
public class GameFrame extends JFrame
{
	static int screenwidth=Toolkit.getDefaultToolkit().getScreenSize().width;
	static int screenheight=Toolkit.getDefaultToolkit().getScreenSize().height;
	private int degree=0;
	
	JDialog jd=new JDialog();
	JFrame ill_jd=new JFrame();
	private GameFrame gameframe=this;
	
	private JButton locButton = new JButton("本地双人");    
	private JButton netButton = new JButton("联网对战"); 
	private JButton pcButton = new JButton("人机对战"); 
	private JButton offButton = new JButton("退出");
	
	private JButton easyButton = new JButton("简单");    
	private JButton normalButton = new JButton("普通"); 
	private JButton hardButton = new JButton("困难"); 
	
	
	//背景设置
	private JPanel contentPane = new JPanel()
	{
		protected void paintComponent(Graphics g) 
		{
/*here change*/		Image image = new ImageIcon("resource/imag/home.png").getImage();
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	    }
	 };
	 
	public GameFrame() 
	{
		this.setTitle("五子棋");
/*here change*/	    this.setSize(1000, 562);
/*here change*/		this.setLocation((screenwidth-1000)/2, (screenheight-562)/2);
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    this.setIconImage(new ImageIcon("resource/imag/logo.png").getImage());
	    contentPane.setLayout(null);

	    locButton.setBounds((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.5), this.getWidth() / 6, this.getHeight() / 14);
	    netButton.setBounds((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.6), this.getWidth() / 6, this.getHeight() / 14);
	    netButton.setFocusPainted(false);
	    pcButton.setBounds((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.7), this.getWidth() / 6, this.getHeight() / 14);
	    offButton .setBounds((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.8), this.getWidth() / 6, this.getHeight() / 14);
	    
	    contentPane.add(locButton);
	    contentPane.add(netButton);
	    contentPane.add(pcButton);
	    contentPane.add(offButton);
	    
	    addAction();//为按钮添加监听事件
	    
	    this.add(contentPane);
	    this.setVisible(true);
	}
	private void addAction() 
	{

		locButton.addActionListener(new ActionListener() 
	    {
	      @Override
	      public void actionPerformed(ActionEvent e) 
	      {
	        to_Room(0);
	      }
	    });
	    offButton.addActionListener(new ActionListener() 
	    {
	      @Override
	      public void actionPerformed(ActionEvent e) 
	      {
	        gameframe.dispose();
	      }
	    });
	    netButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
//	    	  to_onlineRoom();
	    	  Show_uncomplete();
	      }
	    });

	    pcButton.addActionListener(new ActionListener() 
	    {
	      @Override
	      public void actionPerformed(ActionEvent e) 
	      {
	    	choose_degree();
	      }
	    });
	    
	    //不同难度设置
	    easyButton.addActionListener(new ActionListener() 
	    {
	    	@Override
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		degree=1;
	    		jd.setVisible(false);
	    		to_Room(degree);
	    	}
	    });
	    normalButton.addActionListener(new ActionListener() 
	    {
	    	@Override
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		degree=2;
	    		jd.setVisible(false);
	    		to_Room(degree);
	    	}
	    });
	    hardButton.addActionListener(new ActionListener() 
	    {
	    	@Override
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		degree=3;
	    		jd.setVisible(false);
	    		to_Room(degree);
	    	}
	    });
	    
	}


//	public void to_onlineRoom() {
//		// TODO Auto-generated method stub
//		new online_Room(this);
//		this.setVisible(false);
//	}

	protected void Show_uncomplete() 
	{
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this,"很抱歉，该功能还未开发完全，将在后续版本中更新！");
	}
	public void  to_Room(int flag)
	{
		new Room(this,flag);
		this.setVisible(false);
	}
	
	public void choose_degree()
	{
		jd.setSize(200, 300);
		jd.setLocation((screenwidth-200)/2, (screenheight-300)/2);
		jd.setLayout(null);
		
		easyButton.setBounds((int) (jd.getWidth() * 0.12), (int) (jd.getHeight() * 0.1), (int) (jd.getWidth() * 0.7), jd.getHeight() / 6);
		normalButton.setBounds((int) (jd.getWidth() * 0.12), (int) (jd.getHeight() * 0.35), (int) (jd.getWidth() * 0.7), jd.getHeight() / 6);
		hardButton.setBounds((int) (jd.getWidth() * 0.12), (int) (jd.getHeight() * 0.6), (int) (jd.getWidth() * 0.7), jd.getHeight() / 6);
		
		jd.add(easyButton);
		jd.add(normalButton);
		jd.add(hardButton);
		
		jd.setVisible(true);
		return;
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
	}
}
