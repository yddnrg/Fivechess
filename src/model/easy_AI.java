package model;

import java.util.HashMap;


public class easy_AI 
{
	public static boolean first=true;
	private final int chess_black=1,chess_white=2;
	private static int[][] siteValue=new int[15][15];
	public static void reset()
	{
		first=true;
		for(int i=0;i<15;i++)
		{
			for(int j=0;j<15;j++)
			{
				siteValue[i][j]=0;
			}
		}
	}
	public int[] easy_pc(int x, int y, int chess_color) 
	{
		// TODO Auto-generated method stub
		int [] result=new int [2];
		int max=0;
		if(first)
		{
			if(basic_op.table_now[7][7]==0)
			{
				result[0]=7;
				result[1]=7;			
			}
			else
			{
				result[0]=7;
				result[1]=8;
			}
			first=false;
		}
		else 
		{
			getSiteValue();
			for(int i=0;i<15;i++)
			{
				for(int j=0;j<15;j++)
				{
					if(basic_op.table_now[i][j]==0)
						if(siteValue[i][j]>=max)
						{
							max=siteValue[i][j];
							result[0]=i;
							result[1]=j;
						}
				}
			}
		}
		return result;
	}
	
	//棋子相连情况的划分
	public static HashMap<String,Integer> map = new HashMap<String,Integer>();//设置不同落子情况和相应权值的数组
	 static 
	 {
		 //被堵住
		 map.put("01", 17);//眠1连
		 map.put("02", 12);//眠1连
		 map.put("001", 17);//眠1连
		 map.put("002", 12);//眠1连
		 map.put("0001", 17);//眠1连
		 map.put("0002", 12);//眠1连
	  
		 map.put("0102",17);//眠1连，15
		 map.put("0201",12);//眠1连，10
		 map.put("0012",15);//眠1连，15
		 map.put("0021",10);//眠1连，10
		 map.put("01002",19);//眠1连，15
		 map.put("02001",14);//眠1连，10
		 map.put("00102",17);//眠1连，15
		 map.put("00201",12);//眠1连，10
		 map.put("00012",15);//眠1连，15
		 map.put("00021",10);//眠1连，10
	 
		 map.put("01000",21);//活1连，15
		 map.put("02000",16);//活1连，10
		 map.put("00100",19);//活1连，15
		 map.put("00200",14);//活1连，10
		 map.put("00010",17);//活1连，15
		 map.put("00020",12);//活1连，10
		 map.put("00001",15);//活1连，15
		 map.put("00002",10);//活1连，10
	 
		 //被堵住
		 map.put("0101",65);//眠2连，40
		 map.put("0202",60);//眠2连，30
		 map.put("0110",65);//眠2连，40
		 map.put("0220",60);//眠2连，30
		 map.put("011",65);//眠2连，40
		 map.put("022",60);//眠2连，30
		 map.put("0011",65);//眠2连，40
		 map.put("0022",60);//眠2连，30
	  
		 map.put("01012",65);//眠2连，40
		 map.put("02021",60);//眠2连，30
		 map.put("01102",65);//眠2连，40
		 map.put("02201",60);//眠2连，30
		 map.put("00112",65);//眠2连，40
		 map.put("00221",60);//眠2连，30
	 
		 map.put("01010",75);//活2连，40
		 map.put("02020",70);//活2连，30
		 map.put("01100",75);//活2连，40
		 map.put("02200",70);//活2连，30
		 map.put("00110",75);//活2连，40
		 map.put("00220",70);//活2连，30
		 map.put("00011",75);//活2连，40
		 map.put("00022",70);//活2连，30
	  
		 //被堵住
		 map.put("0111",150);//眠3连，100
		 map.put("0222",140);//眠3连，80
	  
		 map.put("01112",150);//眠3连，100
		 map.put("02221",140);//眠3连，80
	  
		 map.put("01101",1000);//活3连，130
		 map.put("02202",800);//活3连，110
	  	 map.put("01011",1000);//活3连，130
	  	 map.put("02022",800);//活3连，110
	  	 map.put("01110", 1000);//活3连
	  	 map.put("02220", 800);//活3连
	  
	  	 map.put("01111",3000);//4连，300
	  	 map.put("02222",3500);//4连，280
	 }
	 public Integer lineValue(Integer a,Integer b) 
	 {
		if(a==null||b==null) 
			return 0;
		else if((a>=10)&&(a<=25)&&(b>=10)&&(b<=25)) 
			return 60;
		else if(((a>=10)&&(a<=25)&&(b>=60)&&(b<=80))||((a>=60)&&(a<=80)&&(b>=10)&&(b<=25))) 
			return 800;
		else if(((a>=10)&&(a<=25)&&(b>=140)&&(b<=1000))||((a>=140)&&(a<=1000)&&(b>=10)&&(b<=25))||((a>=60)&&(a<=80)&&(b>=60)&&(b<=80)))
			return 3000;
		else if(((a>=60)&&(a<=80)&&(b>=140)&&(b<=1000))||((a>=140)&&(a<=1000)&&(b>=60)&&(b<=80))) 
			return 3000;
		else 
			return 0;
	}
	 public void getSiteValue()
	 {
		 for(int i=0;i<15;i++)
		 {
			 for(int j=0;j<15;j++)
			 {
				 if(basic_op.table_now[i][j]==0) 
				 {
					 String tmp="0";
					 //左
					 for(int k=j-1;k>=0&&k>=j-4;k--)
						 tmp=tmp+basic_op.table_now[i][k];
					 Integer left=map.get(tmp);
					 if(left!=null)	siteValue[i][j]+=left;
					 //右
					 tmp="0";
					 for(int k=j+1;k<15&&k<=j+4;k++)
						 tmp=tmp+basic_op.table_now[i][k];
					 Integer right=map.get(tmp);
					 if(right!=null)	siteValue[i][j]+=right;
					 //行
					 siteValue[i][j]+=lineValue(left, right);
					 //上
					 tmp="0";
					 for(int k=i-1;k>=0&&k>=i-4;k--)
						 tmp=tmp+basic_op.table_now[k][j];
					 Integer up=map.get(tmp);
					 if(up!=null)	siteValue[i][j]+=up;
					 //下
					 tmp="0";
					 for(int k=i+1;k<15&&k<=i+4;k++)
						 tmp=tmp+basic_op.table_now[k][j];
					 Integer down=map.get(tmp);
					 if(down!=null)	siteValue[i][j]+=down;
					 //列
					 siteValue[i][j]+=lineValue(up, down);
					 //右下
					 tmp="0";
					 for(int k=1;k<5;k++)
						 if((i+k>=0)&&(i+k<15)&&(j+k>=0)&&(j+k<15))
								 tmp=tmp+basic_op.table_now[i+k][j+k];
					 Integer rightdown=map.get(tmp);
					 if(rightdown!=null)	siteValue[i][j]+=rightdown;
					 //左上
					 tmp="0";
					 for(int k=1;k<5;k++)
						 if((i-k>=0)&&(i-k<15)&&(j-k>=0)&&(j-k<15))
								 tmp=tmp+basic_op.table_now[i-k][j-k];
					 Integer leftup=map.get(tmp);
					 if(leftup!=null)	siteValue[i][j]+=leftup;
					 //右下左上
					 siteValue[i][j]+=lineValue(leftup, rightdown);
					 //右上
					 tmp="0";
					 for(int k=1;k<5;k++)
						 if((i-k>=0)&&(i-k<15)&&(j+k>=0)&&(j+k<15))
								 tmp=tmp+basic_op.table_now[i-k][j+k];
					 Integer rightup=map.get(tmp);
					 if(rightup!=null)	siteValue[i][j]+=rightup;
					 //左下
					 tmp="0";
					 for(int k=1;k<5;k++)
						 if((i+k>=0)&&(i+k<15)&&(j-k>=0)&&(j-k<15))
								 tmp=tmp+basic_op.table_now[i+k][j-k];
					 Integer leftdown=map.get(tmp);
					 if(leftdown!=null)	siteValue[i][j]+=leftdown;
					 //右上左下
					 siteValue[i][j]+=lineValue(leftdown, rightup);
				 }
			 }
		 }
	 }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
