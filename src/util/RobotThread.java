package util;

import model.ChessBoard;
import model.myRule;


public class RobotThread implements Runnable {
  private myRule chess;
  private ChessBoard chessTable;
  public int degree;
  public RobotThread(ChessBoard chessTable,myRule chess){
    this.chessTable=chessTable;
    this.chess=chess;
  }

  public void run(){
    chessTable.robotChess(degree);
  }

}
