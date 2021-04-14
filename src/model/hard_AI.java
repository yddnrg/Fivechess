package model;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class hard_AI {
	private final int chess_black=1,chess_white=2;
	private hard_Node[][] chessBeans = new hard_Node[15][15];
	hard_Node chessBeansForTree;
	int level=3;//博弈树深度
	int node=10;//每个节点的子节点数量
	
	
	
	public int[] hard_pc(int x,int y,int chess_color)
	{
		for (int i = 0; i < 15; i++) 
			for (int j = 0; j < 15; j++) 
			{
				hard_Node chessBean = new hard_Node(i, j, 0, 0);
				chessBeans[i][j] = chessBean;
				chessBeans[i][j].setPlayer(basic_op.table_now[i][j]);
			}
		int []result=new int[2];//用来保存结果
		hard_Node bean;
		getValueByTrees2(0, chess_color, chessBeans, -Integer.MAX_VALUE,
				Integer.MAX_VALUE);
		bean = chessBeansForTree;
		result[0]=bean.getX();
		result[1]=bean.getY();
		return result;
	}
	protected int getValueByTrees2(int d, int player, hard_Node[][] chessBeans2, int alpha, int beta) { //
		hard_Node[][] temBeans = clone(chessBeans2);	//复制一份棋盘用于模拟
		List<hard_Node> orderList = getSortList(player, temBeans);	//根据空位权值对每个空位排序
		if (d == level) 
			// 如果达到了目标搜索深度，返回当前局势得分
			return orderList.get(0).getSum();
		
		for (int i = 0; i < node; i++) 
		{
			hard_Node bean = orderList.get(0);
			int score;
			if (bean.getSum() > Level.ALIVE_4.score)
				//找到杀棋
				score = bean.getSum();
			else 
			{
				// 模拟下棋
				temBeans[bean.getX()][bean.getY()].setPlayer(player);
				// temBeans[bean.getX()][bean.getY()] = bean;
				score = getValueByTrees2(d + 1, 3 - player, temBeans, alpha, beta);
			}
			if (d % 2 == 0) 
			{
				// AI层ֵ
				if (score > alpha) 
				{
					alpha = score;
					if (d == 0) 
						chessBeansForTree = bean;
				}
				if (alpha >= beta) 
				{
					//剪枝 ֦
					score = alpha;
					return score;
				}
			} else 
			{
				//对手层
				if (score < beta)
					beta = score;
				if (alpha >= beta) 
				{
					// 剪枝֦
					score = beta;
					return score;
				}
			}
		}
		return d % 2 == 0 ? alpha : beta;//AI层返回最大，对手层返回最小
	}
	
	
	private hard_Node[][] clone(hard_Node[][] chessBeans2) //复制棋局
	{ 
		hard_Node[][] temBeans = new hard_Node[15][15];
		for (int i = 0; i < 15; i++) 
			for (int j = 0; j < 15; j++) 
			{
				temBeans[i][j] = new hard_Node(chessBeans2[i][j].getX(), chessBeans2[i][j].getY(),
						chessBeans2[i][j].getPlayer(), chessBeans2[i][j].getOrderNumber());
			}
		return temBeans;
	}
	
	
	private List<hard_Node> getSortList(int player, hard_Node[][] tempBeans) 
	{ 
		List<hard_Node> list = new ArrayList<>();
		for (hard_Node[] chessBeans2 : tempBeans) 
			for (hard_Node chessBean : chessBeans2) 
				if (chessBean.getPlayer() == 0) 
				{
					// 攻击分ֵ
					int offense = getValue(chessBean.getX(), chessBean.getY(), player, tempBeans);
					// 防守分ֵ
					int defentse = getValue(chessBean.getX(), chessBean.getY(), 3 - player, tempBeans);
					chessBean.setOffense(offense); //ֵ
					chessBean.setDefentse(defentse);//ֵ
					chessBean.setSum(offense + defentse); //ֵ
					list.add(chessBean);
				}
		Collections.sort(list); // 排序
		return list;
	}
	
	
	//获取当前位置分值
	private int getValue(int x2, int y2, int player, hard_Node[][] tempBeans) 
	{
		Level level1 = getLevel(x2, y2, Direction.HENG, player, tempBeans);
		Level level2 = getLevel(x2, y2, Direction.SHU, player, tempBeans);
		Level level3 = getLevel(x2, y2, Direction.PIE, player, tempBeans);
		Level level4 = getLevel(x2, y2, Direction.NA, player, tempBeans);
		return levelScore(level1, level2, level3, level4) + position[x2][y2];
	}
	
	// 获取四个方向总分值ֵ
	private int levelScore(Level level1, Level level2, Level level3, Level level4) 
	{
		int[] levelCount = new int[Level.values().length];
		for (int i = 0; i < Level.values().length; i++)
				levelCount[i] = 0;
		
			// 统计棋形数量
			levelCount[level1.index]++;
			levelCount[level2.index]++;
			levelCount[level3.index]++;
			levelCount[level4.index]++;

			int score = 0;
			if (levelCount[Level.GO_4.index] >= 2
					|| levelCount[Level.GO_4.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 冲四活三
				score = 10000;
			else if (levelCount[Level.ALIVE_3.index] >= 2)// 双活三
				score = 5000;
			else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 眠三活三
				score = 1000;
			else if (levelCount[Level.ALIVE_2.index] >= 2)// 双活二
				score = 100;
			else if (levelCount[Level.SLEEP_2.index] >= 1 && levelCount[Level.ALIVE_2.index] >= 1)// 眠二活二
				score = 10;
			score = Math.max(score, Math.max(Math.max(level1.score, level2.score), Math.max(level3.score, level4.score)));
			return score;
		}
		private Level getLevel(int x2, int y2, Direction direction, int player, hard_Node[][] tempBeans) {
			String leftString = "";
			String rightString = "";
			String str;
			
			//横竖撇捺
			if (direction == Direction.HENG) 
			{
				leftString = getStringSeq(x2, y2, -1, 0, player, tempBeans);
				rightString = getStringSeq(x2, y2, 1, 0, player, tempBeans);
			} else if (direction == Direction.SHU) 
			{
				leftString = getStringSeq(x2, y2, 0, -1, player, tempBeans);
				rightString = getStringSeq(x2, y2, 0, 1, player, tempBeans);
			} else if (direction == Direction.PIE) 
			{
				leftString = getStringSeq(x2, y2, -1, 1, player, tempBeans);
				rightString = getStringSeq(x2, y2, 1, -1, player, tempBeans);
			} else if (direction == Direction.NA) 
			{
				leftString = getStringSeq(x2, y2, -1, -1, player, tempBeans);
				rightString = getStringSeq(x2, y2, 1, 1, player, tempBeans);
			}

			str = leftString + player + rightString;
			//利用正则表达式将字符串正反各匹配一次
			tempBeans[x2][y2].getBuffer().append("(" + (x2 + 1) + "," + (y2 - 1) + ")" + direction + "\t" + str + "\t");
			String rstr = new StringBuilder(str).reverse().toString();//反
			for (Level level : Level.values()) 
			{
				Pattern pat = Pattern.compile(level.regex[player - 1]);
				Matcher mat = pat.matcher(str); // 正
				boolean r1 = mat.find();
				mat = pat.matcher(rstr); // 反
				boolean r2 = mat.find();
				if (r1 || r2) 
				{
					tempBeans[x2][y2].getBuffer().append(level.name + "\n");
					if (direction == Direction.NA) 
						tempBeans[x2][y2].getBuffer().append("\n");
					return level;
				}
			}
			return Level.NULL;
		}
		// 从(x,y)出发，有8个方向的线，4个保持原有。4个字符串首尾调换
		private String getStringSeq(int x2, int y2, int i, int j, int player, hard_Node[][] tempBeans) {
			String sum = "";
			boolean isRight = false;
			if (i < 0 || (i == 0 && j < 0)) {
				isRight = true;
			}
			for (int k = 0; k < 5; k++) {
				x2 += i;
				y2 += j;
				if (x2 > 0 && x2 < 15 && y2 > 0 && y2 < 15) {
					if (isRight) {
						sum = tempBeans[x2][y2].getPlayer() + sum;
					} else {
						sum = sum + tempBeans[x2][y2].getPlayer();
					}
				}
			}
			return sum;
		}


	// ������Ϣ
		public static enum Level {
			CON_5("长连", 0, new String[] { "11111", "22222" }, 100000),
			ALIVE_4("活四", 1, new String[] { "011110", "022220" }, 10000),
			GO_4("冲四", 2, new String[] { "011112|0101110|0110110", "022221|0202220|0220220" }, 500),
			DEAD_4("死四", 3, new String[] { "211112", "122221" }, -5),
			ALIVE_3("活四", 4, new String[] { "01110|010110", "02220|020220" }, 200),
			SLEEP_3("眠三", 5,
					new String[] { "001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" },
					50),
			DEAD_3("死三", 6, new String[] { "21112", "12221" }, -5),
			ALIVE_2("活二", 7, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
			SLEEP_2("眠二", 8,
					new String[] { "000112|001012|010012|10001|2010102|2011002",
							"000221|002021|020021|20002|1020201|1022001" },
					3),
			DEAD_2("死二", 9, new String[] { "2112", "1221" }, -5), NULL("null", 10, new String[] { "", "" }, 0);
			private String name;
			private int index;
			private String[] regex;// 正则表达式
			int score;//ֵ


			private Level(String name, int index, String[] regex, int score) 
			{
				this.name = name;
				this.index = index;
				this.regex = regex;
				this.score = score;
			}


			@Override
			public String toString() {
				return this.name;
			}
		};

		
		//横竖撇捺
		private static enum Direction {
			HENG, SHU, PIE, NA
		};

		// 位置分
		private static int[][] position = { 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
				{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 },
				{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
				{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
				{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 }, 
				{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
				{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
				{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
				{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 }, 
				{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

}
