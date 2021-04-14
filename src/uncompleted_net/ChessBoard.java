package uncompleted_net;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.*;

import model.basic_op;
import model.myRule;
import util.AudioPlayer;
import util.HumanThread;
import util.RobotThread;
import view.Room;

public class ChessBoard extends JPanel
{
	
	private ChessBoard chessBoard=this;
	private static myRule rule=new basic_op();
	private Room room;
	private online_room ol_room;
	
	private AudioPlayer audioPlayer=new AudioPlayer("resource/audio/down.wav");
	private AudioPlayer audioStopPlayer=new AudioPlayer("resource/audio/stop.wav");
	
	private Executor pool = Executors.newFixedThreadPool(2); // 2个线程容量的线程池
	public static boolean lock=false;
	
	public static int chess_cnt=0;
	private int row; // 鼠标点击的坐标
	private int line; // 鼠标点击的坐标
	private int color_chess;
	public static int color_human=0;
	public static int color_robot=0;
	private int limit;
	private final int chess_black=1,chess_white=2;
	public static int mode=0;
	private int[][] mark = new int[15][15];
	private boolean clickAble;
	private boolean isInvited;
	
	public myRule getRule(){
		return rule;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int flag) {
		this.limit=flag;
	}
	
	
	
	
	public ChessBoard(online_room ol_Room,int color,boolean isinvited)
	{
		this.addMouseListener(new MouseHandler());
		rule.Reset();
		this.ol_room=ol_Room;
		color_human=color;
		color_robot=color_human==chess_black?chess_white:chess_black;
        this.isInvited = isinvited;
        if(isInvited)
            clickAble = false;
        else
            clickAble = true;
	}
	
	
	public void setName(String name) 
	{
        super.setName(name);
        String[] pos = name.split(",");
        System.out.println("panel setName: x->"+pos[0]);
        System.out.println("y->"+pos[1]);

        //收到敌人的下棋步子
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        rule.downchess(x, y, color_robot);
        //然后我就可点，显示该我下棋
        clickAble = true;
        repaint();
    }
	public class MouseHandler extends MouseAdapter 
	{
	    public void mousePressed(MouseEvent event) 
	    {
	    	synchronized (chessBoard) 
	    	{
	    		if(clickAble)
	    		{
	    			int x = event.getX();
		    		int y = event.getY();
		    		System.out.println(x);
		    		System.out.println(y);
		    		if (x > 30 && x < 535 && y > 30 && y < 535) 
		    		{
		    			row = (x - 21) / 34;
		    			line = (y - 21) / 34;
		    			if (rule.downchess(row, line, color_human)) 
		    			{
		    				chess_cnt++;
		    				System.out.println(chess_cnt);
		    				mark[row][line] = 1;
		    				clickAble=false;
		    				sendStepInfo(row,line);
		    				repaint();
		    				
		    				audioPlayer.run();
		    				if(chess_cnt==225)
		    					ol_room.a_draw();
		    				else if(rule.is_win(row, line))
		    				{
		    					if(mode==0)
		    						ol_room.win(color_chess);
		    					else
		    						ol_room.win();
		    				}else
		    					chessBoard.notifyAll();
		    			} else
		    			{
		    				audioStopPlayer.run();
		    				JOptionPane.showMessageDialog(ol_room, "该位置已有棋子");
		    			}
		    		} else
		    			JOptionPane.showMessageDialog(ol_room,"请将棋子放进棋盘内");
	    		}
	      }
	    }
	}
	@Override
	public void paintComponent(Graphics g) 
	{
		g.drawImage(new ImageIcon("resource/imag/pan.png").getImage(), -8, -8,565, 565, this);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.black);
	    char ch = 'A';
	    g.setFont(new Font("宋体", Font.BOLD, 12));

	    for (int i = 0; i < 15; i++) {
	      for (int j = 0; j < 15; j++) {
	        if (basic_op.table_now[i][j] != 0) {
	          int m, n;
	          m = i * 34 + 35;
	          n = j * 34 + 35;
	          Ellipse2D ellipse = new Ellipse2D.Double();
	          Ellipse2D ellipse2D = new Ellipse2D.Double();
	          ellipse.setFrameFromCenter(m, n, m + 14, n + 14);
	          ellipse2D.setFrameFromCenter(m + 1, n - 1, m + 15, n + 13);
	          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	              RenderingHints.VALUE_ANTIALIAS_ON);
	          g2.setComposite(AlphaComposite.getInstance(
	              AlphaComposite.SRC_OVER, 0.5f));

	          // 白子
	          GradientPaint gp1 = new GradientPaint(
	              (float) ellipse.getMinX(),
	              (float) ellipse.getMinY(),
	              new Color(174, 173, 171),
	              (float) ellipse.getMaxX(),
	              (float) ellipse.getMaxY(), new Color(116, 115, 114));
	          GradientPaint gp3 = new GradientPaint(
	              (float) ellipse.getMinX(),
	              (float) ellipse.getMinY(), Color.white,
	              (float) ellipse.getMaxX(),
	              (float) ellipse.getMaxY(), Color.gray);
	          // 黑子
	          GradientPaint gp2 = new GradientPaint(
	              (float) ellipse.getMinX() - 1,
	              (float) ellipse.getMinY() - 1, Color.white,
	              (float) ellipse.getCenterX() - 1,
	              (float) ellipse.getCenterY() - 1, Color.gray);
	          GradientPaint gp4 = new GradientPaint(
	              (float) ellipse.getMinX() - 1,
	              (float) ellipse.getMinY() - 1, Color.white,
	              (float) ellipse.getCenterX() - 1,
	              (float) ellipse.getCenterY() - 1, Color.black);
	          // 黑子
	          // System.out.println("m=" + m + "n=" + n);
	          if (basic_op.table_now[i][j] == chess_black) {
	            // System.out.println("棋桌在"+m+","+n+"处画了一个黑子");
	            g2.setPaint(gp2);
	            g2.fill(ellipse);
	            g2.setPaint(gp4);

	          } else if (basic_op.table_now[i][j] == chess_white) {// 白子
	            // System.out.println("棋桌在"+m+","+n+"处画了一个白子");
	            g2.setPaint(gp1);
	            g2.fill(ellipse);
	            g2.setPaint(gp3);

	          } else {
	            System.out.println("定位不准确，未获得棋子颜色");
	          }
	          g2.setComposite(AlphaComposite.getInstance(
	              AlphaComposite.SRC_OVER, 1));
	          g2.fill(ellipse2D);
	          if (mark[i][j] == 1) {
	            g2.setColor(Color.red);
	            g2.drawLine(m - 3, n, m + 3, n);
	            g2.drawLine(m, n - 3, m, n + 3);
	            mark[i][j] = 0;
	          }
	        }
	      }
	    }
	  }
	public void sendStepInfo(int x, int y)
	{
		// TODO Auto-generated method stub
		ol_room.getLan().xiaQi(x, y);
	}
	public void back() 
	{
		int flag=rule.delete(limit, 1);
		if(flag==-1)
			ol_room.no_regret_times();
		else if(flag==0)
			ol_room.no_chess();
		else if(color_human==chess_white&&chess_cnt==1)
			ol_room.no_chess();
		else 
		{
			if(color_human!=0&&color_chess!=color_human)	
			{
				rule.delete(limit, 1);
				chess_cnt--;
			}
			limit--;
			chess_cnt--;
		}
		repaint();
		System.out.println(chess_cnt);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
