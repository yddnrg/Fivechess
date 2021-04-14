package uncompleted_net;

import util.AudioPlayer;

import javax.swing.*;

import view.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Xman on 2017/6/14 0014.
 */
public class online_room  extends JFrame implements ActionListener {
    JLabel titleLabel;
    JPanel mainPan;
    String panelTitle;
    JButton backBt;
    CardLayout mainCardLayout;
    online_room olRoom=this;
    int chess_white=2,chess_black=1;
    GameFrame GF;
    ///欢迎界面
    JButton btSearch;
    JButton btFightHost;

    ///在线主机
    JPanel onlinePan;
    ArrayList<User> onlineUserList = new ArrayList<>();
    ArrayList<SButton> onlineHostList = new ArrayList<>();

    //等待对方同意
    JPanel waitPan;
    JLabel waitLabel;

    //收到邀请的Dialog
    JDialog invitedDialog;
    JButton btAgree;
    JButton btDeny;

    ///游戏界面
    ChessBoard gamePan;

    //游戏结束
    JPanel gameOverPan;
    JLabel winnerLabel;
    JLabel loserLabel;

    Container container;

    LAN lan;
    private User user;

    
    public LAN getLan() {
    	return lan;
	}
    
    public online_room() {
        container = this.getContentPane();
        LAN lan = new LAN() {
            @Override
            public void deny() {
                if (onlinePan != null && mainPan != null) {
                    mainCardLayout.show(mainPan, "online");
                }
                JOptionPane.showConfirmDialog(online_room.this, "reject!", null, JOptionPane.CLOSED_OPTION);
            }

            @Override
            public void agree() {
                beginGame(false);
            }

            //搜索到一台新的在线主机
            @Override
            public void NewPlay(User user, int i) {
                onlineUserList.add(user);
                addOnlineHost(user, i);
            }

            //被user1邀请
            @Override
            public void invited(User user1) {
                user = user1;
                showInvitedDialog();
            }

            @Override
            public void read(int x, int y) {

                //收到游戏信息
                changeGameView(x,y);

            }
        };


        mainCardLayout = new CardLayout();
        mainPan = new JPanel(mainCardLayout);
        container.add(mainPan);

        this.setSize(800, 700);
        this.setTitle("五子棋游戏");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        
        showOnlineHost();
        lan.Search();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        //收到游戏邀请-》同意
        if (e.getSource() == btAgree) {
            //lan 发送同意消息
            lan.sendBC("ha", user.getIp());
            lan.setEnemy(user);
            invitedDialog.dispose();
            beginGame(true);
        }

        ///收到游戏邀请=》拒绝
        if (e.getSource() == btDeny) {
            //lan发送拒绝消息
            lan.sendBC("hd", user.getIp());
            invitedDialog.dispose();

        }

        if (e.getSource() instanceof SButton) {
            SButton sButton = (SButton) e.getSource();
            int hostIndex = -1;
            hostIndex = sButton.getId();
            System.out.println("conn "+ onlineUserList.get(hostIndex).getIp());
            if (hostIndex != -1) {
                String ip = onlineUserList.get(hostIndex).getIp();
                waitLabel = new JLabel("Wait....");
                waitLabel.setHorizontalAlignment(JLabel.CENTER);
                waitLabel.setBounds(300, 300, 200, 50);
                waitPan = new JPanel(null);
                waitPan.add(waitLabel);
                mainPan.add(waitPan, "wait");
                mainCardLayout.show(mainPan, "wait");
                lan.sendBC("$" + lan.getUser().getName() + "," + lan.getUser().getIp(), ip);   //发送联机邀请
                lan.setEnemy(onlineUserList.get(hostIndex));
            }

        }
    }

    public void showInvitedDialog() {
        invitedDialog = new JDialog(this, "有新的游戏请求");
        btAgree = new JButton("Agree");
        btAgree.addActionListener(this);
        btDeny = new JButton("Deny");
        btDeny.addActionListener(this);
        invitedDialog.setLayout(null);
        invitedDialog.setLocationRelativeTo(null);
        invitedDialog.setSize(300, 200);
        btAgree.setBounds(80, 100, 70, 30);
        btDeny.setBounds(160, 100, 70, 30);
        invitedDialog.add(btAgree);
        invitedDialog.add(btDeny);
        invitedDialog.show();
    }

    public void showOnlineHost() {
        if (onlinePan == null) {
            onlinePan = new JPanel();
        }
        onlinePan.validate();
        mainPan.add(onlinePan, "online");
        mainCardLayout.show(mainPan, "online");
        mainPan.validate();

    }

    public void addOnlineHost(User user, int i) {
        if (onlinePan == null) {
            onlinePan = new JPanel();
        }
        SButton sButton = new SButton(user.getName(), i);
        sButton.addActionListener(this);
        onlineHostList.add(sButton);
        onlinePan.add(sButton);
        onlinePan.validate();
    }

    public void fightHost() {

    }

    public void beginGame(boolean beInvited) 
    {
        //发起请求的一方为白棋
        //被要求的一方为黑棋
        if (beInvited)///被邀请
            gamePan = new ChessBoard(olRoom,2,true);
        else///主动的一方
            gamePan = new ChessBoard(olRoom,1,false);
    }

    public void changeGameView(int x,int y) {
        //更新gamePan 的界面显示
        System.out.println("from enemy: x->"+x+" y->"+y);
        gamePan.setName(x+","+y+",");
    }
    private void resetGame() 
	{
	    gamePan.getRule().Reset();
	    repaint(); 
	    ChessBoard.chess_cnt = 0;
	    if(ChessBoard.color_human==chess_white)
	    	ChessBoard.lock=true;
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
		String Lim=JOptionPane.showInputDialog("请输入最大悔棋次数");
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
	
}
