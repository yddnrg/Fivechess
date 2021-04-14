package model;

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

import util.AudioPlayer;
import util.HumanThread;
import util.RobotThread;
import view.Room;

public class ChessBoard extends JPanel
{
	
	private ChessBoard chessBoard=this;
	private static myRule rule=new basic_op();
	private Room room;
	
	private AudioPlayer audioPlayer=new AudioPlayer("resource/audio/down.wav");
	private AudioPlayer audioStopPlayer=new AudioPlayer("resource/audio/stop.wav");
	
	private Executor pool = Executors.newFixedThreadPool(2); // 2个线程容量的线程池
	private RobotThread robotThread = new RobotThread(this, rule); // 机器人线程
	private HumanThread humanThread = new HumanThread(this, rule); // 人类线程
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
	public int level;
	

	public myRule getRule(){
		return rule;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int flag) {
		this.limit=flag;
	}
	public ChessBoard(Room room) //本地模式
	{
		// TODO Auto-generated constructor stub
		super(null);
		this.room=room;
		chess_cnt=0;
		limit=room.Limit;
		rule.Reset();
		
		this.setBounds(0, 0, 515, 515);
	    pool.execute(humanThread); // 开启人类线程
	}
	public ChessBoard(Room room,int degree) //人机模式
	{
		// TODO Auto-generated constructor stub
		super(null);
		this.room=room;
		limit=room.Limit;
		color_human=room.Choose_color;
		color_robot=color_human==chess_black?chess_white:chess_black;
		chess_cnt=0;
		robotThread.degree=degree;
		level=degree;
		rule.Reset();
		
		this.setBounds(0, 0, 515, 515);
		pool.execute(humanThread); // 开启人类线程
		pool.execute(robotThread);
	}
	
	public class MouseHandler extends MouseAdapter 
	{
	    public void mousePressed(MouseEvent event) 
	    {
	    	synchronized (chessBoard) 
	    	{
	    			int x = event.getX();
		    		int y = event.getY();
		    		System.out.println(x);
		    		System.out.println(y);
		    		
		    		if (x > 30 && x < 535 && y > 30 && y < 535) 
		    		{
		    			int color_tmp=0;
		    			row = (x - 21) / 34;
		    			line = (y - 21) / 34;
		    			color_chess=chess_cnt%2+1;
		    			if(color_human!=0)
		    				color_tmp=color_human;
		    			else 
							color_tmp=color_chess;
		    			if (rule.downchess(row, line, color_tmp)) 
		    			{

		    				chess_cnt++;
		    				System.out.println(chess_cnt);
		    				mark[row][line] = 1;
		    				lock = true;
		    				repaint();
		    				audioPlayer.run();
		    				if(chess_cnt==225)
		    					room.a_draw();
		    				else if(rule.is_win(row, line))
		    				{
		    					if(mode==0)
		    						room.win(color_chess);
		    					else
		    						room.win();
		    					chessBoard.notifyAll();
		    				}else
		    					chessBoard.notifyAll();
		    			} else
		    			{
		    				audioStopPlayer.run();
		    				JOptionPane.showMessageDialog(room, "该位置已有棋子");
		    			}
		    		} else
		    			JOptionPane.showMessageDialog(room,"请将棋子放进棋盘内");

		    		
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
	public void back() 
	{
		int flag=rule.delete(limit, 1);
		if(flag==-1)
			room.no_regret_times();
		else if(flag==0)
			room.no_chess();
		else if(color_human==chess_white&&chess_cnt==1)
			room.no_chess();
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
	public synchronized void robotChess(int degree) 
	{
		// TODO Auto-generated method stub
		System.out.println("机器线程开启");
	    synchronized (chessBoard) 
	    {
	    	while (true) 
	    	{
	    		if(color_human==chess_black)
	    		{
	    			if (!lock) 
		    		{
		    		  try {
		    			  wait();
		    		  } catch (Exception e) {
		    			  e.printStackTrace();
		    		  }
		    		} 
		    		else
		    		{
		    			try {
		    				Thread.sleep(500);
		    			} catch (Exception e) {
		    				e.printStackTrace();
		    			}
		    			int[] XY = rule.pc_ai(row, line,degree,color_robot);
		    			mark[XY[0]][XY[1]] = 1;
		    			color_chess=chess_cnt%2+1;
		    			rule.downchess(XY[0], XY[1], color_robot);
		    			repaint();
		    			chess_cnt++;
		    			lock = false;
		    			audioPlayer.run();
		    			if(chess_cnt==225)
		    				room.a_draw();
		    			else if(rule.is_win(XY[0],XY[1]))
		    				room.deafeat();
		    			else
		    				chessBoard.notifyAll();
		    		}
	    		}
	    		else 
	    		{
	    			try {
	    				Thread.sleep(500);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
					int[] XY = rule.pc_ai(row, line,degree,color_robot);
	    			mark[XY[0]][XY[1]] = 1;
	    			color_chess=chess_cnt%2+1;
	    			rule.downchess(XY[0], XY[1], color_robot);
	    			repaint();
	    			chess_cnt++;
	    			lock = false;
	    			audioPlayer.run();
	    			if(chess_cnt==225)
	    				room.a_draw();
	    			else if(rule.is_win(XY[0],XY[1]))
	    			{
	    				room.deafeat();
	    				lock=true;
	    			}
	    			else
	    				chessBoard.notifyAll();
	    			if (!lock) 
		    		{
		    		  try {
		    			  wait();
		    		  } catch (Exception e) {
		    			  e.printStackTrace();
		    		  }
		    		} 
	    		}		
	    	}
	    }
	}
	public void robot_for_black_reset() 
	{
		// TODO Auto-generated method stub
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[] XY = rule.pc_ai(row, line,level,color_robot);
		mark[XY[0]][XY[1]] = 1;
		color_chess=chess_cnt%2+1;
		rule.downchess(XY[0], XY[1], color_robot);
		repaint();
		chess_cnt++;
		lock = false;
		audioPlayer.run();
		chessBoard.notifyAll();
		if (!lock) 
		{
		  try {
			  wait();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		}
	}
}
