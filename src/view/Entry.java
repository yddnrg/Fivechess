package view;

import javax.swing.SwingUtilities;
import util.AudioPlayer;

public class Entry {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub 
		SwingUtilities.invokeLater(new Runnable() 
		{
		      @Override
		      public void run( ) {
		        new GameFrame();
		      }
		});
	}

}
