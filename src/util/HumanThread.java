package util;

import model.ChessBoard;
import model.myRule;


public class HumanThread implements Runnable {
  private myRule rule;
  private ChessBoard chessTable;

  public HumanThread(ChessBoard chessTable, myRule rule) {
    this.chessTable = chessTable;
    this.rule = rule;
  }


  public void run() {
    chessTable.addMouseListener(chessTable.new MouseHandler());
  }

}
