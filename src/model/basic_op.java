package model;

import java.sql.Struct;

import view.Room;


public class basic_op implements myRule
{	
	private int i, j, cnt;
	private final int chess_white=2,chess_black=1;
	public static int[][] table_now = new int[15][15];
	public static int[][] table_stack = new int[260][2];
	private int stack_cnt=-1;
	normal_AI ai=new normal_AI();
	
	@Override
	public boolean downchess(int x, int y, int type) 
	{
		// TODO Auto-generated method stub
		if (table_now[x][y] != 0) 
			return false;
		else
		{
			stack_cnt++;
			table_stack[stack_cnt][0]=x;
			table_stack[stack_cnt][1]=y;
			if (type == chess_white)
				table_now[x][y]=chess_white;
			else 
				table_now[x][y]=chess_black;
		}
		return true;
	}
	
	@Override
	public int delete(int limit,int amount) 
	{
		// TODO Auto-generated method stub
		if(limit<=0)
			return -1;
		else
		{
			for(;amount!=0;amount--)
			{
				if(stack_cnt<0)
					return 0;
				if(stack_cnt==0)
				{
					easy_AI.first=true;
					ai.start=true;
				}
				if(ChessBoard.mode==2)
					ai.delete();
				table_now[table_stack[stack_cnt][0]][table_stack[stack_cnt][1]]=0;
				stack_cnt--;
			}
		}
		return 1;
	}
	@Override
	public boolean is_win(int x, int y) 
	{
		// TODO Auto-generated method stub
		//竖直
		cnt=1;
		for(i=x,j=y;j>0&&table_now[i][j]==table_now[i][j-1];j--)
			cnt++;
		for(i=x,j=y;j<14&&table_now[i][j]==table_now[i][j+1];j++)
			cnt++;
		if(cnt>=5)
			return true;
		//水平
		cnt=1;
		for(i=x,j=y;i>0&&table_now[i][j]==table_now[i-1][j];i--)
			cnt++;
		for(i=x,j=y;i<14&&table_now[i][j]==table_now[i+1][j];i++)
			cnt++;
		if(cnt>=5)
			return true;
		//右上左下
		cnt=1;
		for(i=x,j=y;i<14&&j>0&&table_now[i][j]==table_now[i+1][j-1];i++,j--)
			cnt++;
		for(i=x,j=y;j<14&&i>0&&table_now[i][j]==table_now[i-1][j+1];i--,j++)
			cnt++;
		if(cnt>=5)
			return true;
		//右下左上
		cnt=1;
		for(i=x,j=y;i<14&&j<14&&table_now[i][j]==table_now[i+1][j+1];i++,j++)
			cnt++;
		for(i=x,j=y;i>0&&j>0&&table_now[i][j]==table_now[i-1][j-1];i--,j--)
			cnt++;
		if(cnt>=5)
			return true;
		
		return false;
	}
	@Override
	public void Reset() 
	{
		// TODO Auto-generated method stub
		this.cnt=0;		this.stack_cnt=-1;
		ai.Reset();
		easy_AI.reset();
		for(i=0;i<15;i++)
		{
			for(j=0;j<15;j++)
			{
				table_now[i][j]=0;
			}
		}
		for(i=0;i<255;i++)
		{
			table_stack[i][0]=0;
			table_stack[i][1]=0;
		}
		this.i=0;	this.j=0;
		return;
	}

	
	@Override
	public int[] pc_ai(int x, int y, int degree ,int chess_color) 
	{
		// TODO Auto-generated method stub
		int [] result=new int [2];
		if(degree==1)
			result=new easy_AI().easy_pc(x,y,chess_color);
		else if(degree==2)
			result=ai.normal_pc(x,y,chess_color);
		else 
			result=new hard_AI().hard_pc(x,y,chess_color);
		return result;
	}
}
