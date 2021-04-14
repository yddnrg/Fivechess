package model;
/*
 * 判断方法及操作方法接口
 */

public interface myRule {
	
	boolean downchess(int x,int y,int type);//落子
	int delete(int limit,int amount);//删除棋子
	boolean is_win(int x,int y);//判断胜负
	void Reset();//重置游戏
	int[] pc_ai(int x, int y, int degree, int chess_color);
}
