package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import model.ChessBoard;
import util.AudioPlayer;


public class Room extends JFrame 
{
	public boolean gameStart;
	public boolean backGame;
	private GameFrame GF;
	private final int chess_black=1,chess_white=2;
	public int Limit=setLimit();
	public int Choose_color=set_color();;
	JPanel Gamer=new JPanel();
	private ChessBoard table;
	
	public Room(GameFrame GF,int mode) //mode为0是本地对战，mode为1，2，3分别代表不同难度 
	{
	    this.GF = GF;
	    init(mode);
	}
	public void init(int mode)
	{
		
		this.setTitle("五子棋");
	    this.setSize(1000, 800);
	    this.setLocation((GF.screenwidth-1000)/2, (GF.screenheight-800)/2);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    getContentPane().setLayout(null);
	    setVisible(true);
	    
	    if(mode==0)
	    	table=new ChessBoard(this);
	    else 
	    	
	    	table=new ChessBoard(this,mode);
			
	    
	    JPanel background = new JPanel() 
	    {
	        protected void paintComponent(Graphics g) 
	        {
	          Image image = new ImageIcon("resource/imag/room.png").getImage();
	          g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	        }
	      };
	      background.setBounds(0, 0, 1000, 800);
	      getContentPane().add(background);
	      background.setLayout(null);
	      background.setOpaque(false);

	      table.setBounds(215, 100, 545, 545);
	      background.add(table);

	      JPanel UIPanel = new JPanel();
	      UIPanel.setBounds(173, 670, 515, 33);
	      background.add(UIPanel);
	      UIPanel.setLayout(null);
	      UIPanel.setOpaque(false);
	      
	      
	      JButton Restar_btn = new JButton("重新开始");
	      Restar_btn.addMouseListener(new MouseAdapter() 
	      {
	        @Override
	        public void mouseClicked(MouseEvent e) 
	        {
	          resetGame();
	        }
	      });
	      Restar_btn.setBounds(150, 5, 80, 23);
	      UIPanel.add(Restar_btn);
	      
	      
	      JButton Exit_btn = new JButton("退出");
	      Exit_btn.setBounds(416, 5, 73, 23);
	      Exit_btn.addMouseListener(new MouseAdapter() 
	      {
	        @Override
	        public void mouseClicked(MouseEvent e) 
	        {
	          table.getRule().Reset();
	          to_out();
	        }
	      });
	      UIPanel.add(Exit_btn);

	      JButton Regret_btn = new JButton("悔棋");
	      Regret_btn.setBounds(240, 5, 78, 23);
	      Regret_btn.addMouseListener(new MouseAdapter() 
	      {
	        @Override
	        public void mouseClicked(MouseEvent e) 
	        {
	            String[] options = {"确认悔棋", "取消悔棋"};
	            int res = JOptionPane.showOptionDialog(null, "确认悔棋吗，你还有"+table.getLimit()+"次悔棋机会", "悔棋?",
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION,
	                new ImageIcon("resource/imag/back.png"), options, options[0]);
	            if (res ==0) 
	                table.back();

	        }
	      });
	      UIPanel.add(Regret_btn);

	      JButton givein_btn = new JButton("认输");
	      givein_btn.addActionListener(new ActionListener() 
	      {
	        @Override
	        public void actionPerformed(ActionEvent e) 
	        {
	            String[] options = {"确定认输", "继续本局"};
	            int res = JOptionPane.showOptionDialog(null, "确定认输吗？", "这样真的好吗?",
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION,
	                new ImageIcon("resource/imag/touxiang.png"), options, options[0]);
	            if (res == 0) 
	              deafeat();
	          
	        }
	      });
	      givein_btn.setBounds(328, 5, 78, 23);
	      UIPanel.add(givein_btn);
	  }
	private void resetGame() 
	{
	    gameStart = false;
	    table.getRule().Reset();
	    repaint(); 
	    ChessBoard.chess_cnt = 0;
	    table.setLimit(this.Limit);
	    if(ChessBoard.color_human==chess_white)
	    {
	    	ChessBoard.lock=true;
	    	table.robot_for_black_reset();;
	    }
	    	
	    	
	    	
	    
	}
	public void deafeat() 
	{
		new Thread(new AudioPlayer("resource/audio/loser.wav")).start();
	    JOptionPane.showMessageDialog(this,
	        "胜败乃兵家常事，壮士请重新来过", "你输了！", JOptionPane.ERROR_MESSAGE, new ImageIcon("resource/imag/loser.png"));
	    resetGame();
	}
	public void win()
	{
		new Thread(new AudioPlayer("resource/audio/winner.wav")).start();
		JOptionPane.showMessageDialog(this,
		     "大侠，在下甘拜下风！！", "你赢了！", JOptionPane.ERROR_MESSAGE, new ImageIcon("resource/imag/winner.png"));
		resetGame();
	}
	public void win(int color_win)
	{
		String color_String="黑棋";
		if(color_win==chess_white)
			color_String="白棋";
		new Thread(new AudioPlayer("resource/audio/winner.wav")).start();
		JOptionPane.showMessageDialog(this,
		    color_String+"赢了！" , "在下甘拜下风！！", JOptionPane.ERROR_MESSAGE, new ImageIcon("resource/imag/winner.png"));
		resetGame();
	}
	public void a_draw()
	{
		new Thread(new AudioPlayer("resource/audio/winner.wav")).start();
		JOptionPane.showMessageDialog(this,
		     "平局", "势均力敌！", JOptionPane.ERROR_MESSAGE, new ImageIcon("resource/imag/winner.png"));
		resetGame();
	}
	public void to_out()
	{
		GF.setVisible(true);
		this.dispose();
	}
	public void no_regret_times() 
	{
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this, "你的次数已用尽");
	}
	public void no_chess() 
	{
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this, "无棋可悔");
	}
	public int setLimit() 
	{
		// TODO Auto-generated method stub
		String Lim=JOptionPane.showInputDialog("请输入最大悔棋次数（0~30）");
		return Integer.parseInt(Lim);
	}
	public int set_color() 
	{
		// TODO Auto-generated method stub
		String[] options = {"白棋", "黑棋"};
        int res = JOptionPane.showOptionDialog(this, "黑子先走，选择你的棋子颜色", "选择",
            JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION,new ImageIcon("resource/imag/winner.png"),options, options[0]);
		if(res==0)
			return chess_white;
		return chess_black;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
