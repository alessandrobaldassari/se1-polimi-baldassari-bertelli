package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/***/
public class WithBackgroundImagePanel extends JPanel
{

	/***/
	private Image backgroundImage ;
	
	/***/
	public WithBackgroundImagePanel () 
	{
		super () ;
	}
	
	/***/
	public void setBackgroundImage ( Image backgroundImage ) 
	{
		this.backgroundImage = backgroundImage ;
	}
	
	/***/
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		if ( backgroundImage != null )
			g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}
	
}
