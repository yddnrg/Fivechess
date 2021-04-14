package model;

public class normal_AI {

	
	private int i, j, k, m, n, icount=0;
	// 白子为1，黑子为2,初始为0；
	private boolean[][][] blacktable = new boolean[15][15][572]; // 黑棋获胜组合
	private boolean[][][] whitetable = new boolean[15][15][572]; // 白棋获胜组合
	private int[][] wnum = new int[15][15]; // 白棋在棋盘上各个位置的分值 
	private int[][] bnum = new int[15][15]; // 黑棋在棋盘上各个位置的分值
	private int wgrade, bgrade;             //黑白棋子分数
	private int[][] win = new int[3][572]; // 记录棋子在棋盘上的获胜组合中填入了多少棋子
	public boolean start;
	private int wmat, wnat, bmde, bnde;
	private boolean[][][][]oldblacktable = new boolean[15][15][572][30]; // 旧黑棋获胜组合
	private boolean[][][][] oldwhitetable = new boolean[15][15][572][30]; // 旧白棋获胜组合
	private int[][][]oldwnum = new int[15][15][30]; // 旧白棋在棋盘上各个位置的分值 
	private int[][][]oldbnum = new int[15][15][30]; // 旧黑棋在棋盘上各个位置的分值
	private int[][][]oldwin = new int[3][572][30]; // 旧的记录棋子在棋盘上的获胜组合中填入了多少棋子
	public static int normal_AI_stack=-1;
	
	public int[] normal_pc(int x, int y, int chess_color) 
	{
		// TODO Auto-generated method stub
		int [] result=new int [2];
		result=ComTurn(x, y,chess_color);
		return result;
	}
	public void Reset() 
	{
		this.normal_AI_stack=-1;
		this.start = true;
		icount=0;
		// 初始化棋盘
		for (i = 0; i < 15; i++)
			for (j = 0; j < 15; j++)	
			{
				this.bnum[i][j] = 0;
				this.wnum[i][j] = 0;
			}
		// 遍历所有的五连子可能情况的权值
		
		// 横
		for (i = 0; i < 15; i++)
			for (j = 0; j < 11; j++) 
			{
				for (k = 0; k < 5; k++) 
				{
					this.blacktable[j + k][i][icount] = true;
					this.whitetable[j + k][i][icount] = true;
				}
				icount++;
			}
		
		// 竖
		for (i = 0; i < 15; i++)
			for (j = 0; j < 11; j++) 
			{ 
				for (k = 0; k < 5; k++) 
				{
					this.blacktable[i][j + k][icount] = true;
					this.whitetable[i][j + k][icount] = true;
				}
				icount++;
			}
		
		// 捺
		for (i = 0; i < 11; i++)
			for (j = 0; j < 11; j++) 
			{
				for (k = 0; k < 5; k++) 
				{
					this.blacktable[j + k][i + k][icount] = true;
					this.whitetable[j + k][i + k][icount] = true;
				}
				icount++;
			}
		
		// 撇
		for (i = 0; i < 11; i++)
			for (j = 14; j >= 4; j--) 
			{
				for (k = 0; k < 5; k++) 
				{
					this.blacktable[j - k][i + k][icount] = true;
					this.whitetable[j - k][i + k][icount] = true;
				}
				icount++;
			}
		
		for (i = 1; i <= 2; i++)
			// 初始化黑子白子上的每个权值上的连子数
			for (j = 0; j < 572; j++)
				this.win[i][j] = 0;
		
	}
	public int[] ComTurn(int x, int y,int chess_color) // 找出电脑最佳落子点
	{ 
		int index[] = new int[2];
		normal_AI_stack++;
		//保存棋局
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
				for(int z=0;z<572;z++)
				{
					oldblacktable[i][j][z][normal_AI_stack]= blacktable[i][j][z];
					oldwhitetable[i][j][z][normal_AI_stack]=whitetable[i][j][z];
				}
		
		
		for(int i=0;i<3;i++)
			for(int j=0;j<572;j++)
		     oldwin[i][j][normal_AI_stack]=win[i][j];
		
		
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
			{
				oldwnum[i][j][normal_AI_stack]=wnum[i][j];
		        oldbnum[i][j][normal_AI_stack]=bnum[i][j];
			}
		
		
			for(i=0;i<572;i++)
			{
				if(this.blacktable[x][y][i] && this.win[2][i] != 7)
					this.win[2][i]++;//给黑子的所有五连子可能的加载当前连子数
				if(this.whitetable[x][y][i])
				{
					this.whitetable[x][y][i] = false;
					this.win[1][i]=7;
				}
			}
			for (i = 0; i <= 14; i++)
				// 遍历棋盘上的所有坐标
				for (j = 0; j <= 14; j++) 
				{
					this.bnum[i][j] = 0; // 该坐标的黑子奖励积分清零
					if (basic_op.table_now[i][j] == 0) // 在还没下棋子的地方遍历
						for (k = 0; k < 572; k++)
							// 遍历该棋盘可落子点上的黑子所有权值的连子情况，并给该落子点加上相应奖励分
							if (this.blacktable[i][j][k]) 
							{
								switch (this.win[2][k]+chess_color*10) 
								{
									case 11: // 一连子
										this.bnum[i][j] += 5;
										break;
									case 12: // 两连子
										this.bnum[i][j] += 52;
										break;
									case 13: // 三连子
										this.bnum[i][j] += 150;
										break;
									case 14: // 四连子
										this.bnum[i][j] += 410;
										break;
										
								//区分棋子，自己的棋子分数略高
								
									case 21: // 一连子
										this.bnum[i][j] += 5;
										break;
									case 22: // 两连子
										this.bnum[i][j] += 50;
										break;
									case 23: // 三连子
										this.bnum[i][j] += 140;
										break;
									case 24: // 四连子
										this.bnum[i][j] += 400;
										break;
								}
							}
					this.wnum[i][j] = 0;// 该坐标的白子的奖励积分清零
					if (basic_op.table_now[i][j] == 0) // 在还没下棋子的地方遍历
						for (k = 0; k < 572; k++)
							// 遍历该棋盘可落子点上的白子所有权值的连子情况，并给该落子点加上相应奖励分
							if (this.whitetable[i][j][k]) 
							{
								switch (this.win[1][k]+chess_color*10) 
								{
									case 11: // 一连子
										this.wnum[i][j] += 5;
										break;
									case 12: // 两连子
										this.wnum[i][j] += 50;
										break;
									case 13: // 三连子
										this.wnum[i][j] += 140;
										break;
									case 14: // 四连子
										this.wnum[i][j] += 400;
										break;
										
									//区分棋子，自己的棋子分数略高
										
									case 21: // 一连子
										this.wnum[i][j] += 5;
										break;
									case 22: // 两连子
										this.wnum[i][j] += 52;
										break;
									case 23: // 三连子
										this.wnum[i][j] += 150;
										break;
									case 24: // 四连子
										this.wnum[i][j] += 410;
										break;
								}
							}
				}
			if (this.start) 
			{ // 开始时白子落子坐标
				if(basic_op.table_now[7][7]==0)
				{
					m = 7;
					n = 7;
				}else
				{
					m=7;
					n=8;
				}
				this.start = false;
			} else 
			{
				for (i = 0; i < 15; i++)
					for (j = 0; j < 15; j++)
						if (basic_op.table_now[i][j] == 0) 
						{ // 找出棋盘上可落子点的黑子白子的各自最大权值，找出各自的最佳落子点
							if (this.wnum[i][j] >= this.wgrade) 
							{
								this.wgrade = this.wnum[i][j];
								this.wmat = i;
								this.wnat = j;
							}
							if (this.bnum[i][j] >= this.bgrade) 
							{
								this.bgrade = this.bnum[i][j];
								this.bmde = i;
								this.bnde = j;
							}
						}
				if (this.wgrade >= this.bgrade) 
				{ // 如果白子的最佳落子点的权值比黑子的最佳落子点权值大，则电脑的最佳落子点为白子的最佳落子点，否则相反
					m = wmat;
					n = wnat;
				} else 
				{
					m = bmde;
					n = bnde;
				}
			}
			this.wgrade = 0;
			this.bgrade = 0;
			 // 电脑下子位置
			System.out.println("电脑下在了" + m + "," + n);
			for (i = 0; i < 572; i++) 
			{
				if (this.whitetable[m][n][i] && this.win[1][i] != 7)
					this.win[1][i]++; // 给白子的所有五连子可能的加载当前连子数
				   
				if (this.blacktable[m][n][i]) 
				{
					this.blacktable[m][n][i] = false;
					this.win[2][i] = 7;
				}
			}

			index[0] = m;
			index[1] = n;

			return index;
}
	
	public void delete() 
	{
		// TODO Auto-generated method stub
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
				for(int z=0;z<572;z++)
				{
					blacktable[i][j][z]=oldblacktable[i][j][z][normal_AI_stack];
					whitetable[i][j][z]=oldwhitetable[i][j][z][normal_AI_stack];
				}

		for(int i=0;i<3;i++)
			for(int j=0;j<572;j++)
			     win[i][j]=oldwin[i][j][normal_AI_stack];

		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
			{
				wnum[i][j]=oldwnum[i][j][normal_AI_stack];
		        bnum[i][j]=oldbnum[i][j][normal_AI_stack];
			}
		this.normal_AI_stack--;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
