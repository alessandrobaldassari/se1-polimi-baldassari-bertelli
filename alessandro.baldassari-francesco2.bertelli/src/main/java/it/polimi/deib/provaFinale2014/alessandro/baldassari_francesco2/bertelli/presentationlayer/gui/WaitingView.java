package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/***/
public class WaitingView extends JDialog 
{
	
	/***/
	private InitViewPanel i ;
	
	/***/
	public WaitingView () 
	{
		super ( ( Frame ) null , "JSheepland" , true ) ;
		i = new InitViewPanel () ;
		add ( i ) ;
	}
	
	public void setText ( String text ) 
	{
		i.setText ( text ) ;
	}
	
}

/***/
class InitViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/***/
	private final String BACKGROUND_IMAGE_FILE_PATH = "sheepland_init.jpg"; ;
	
	/***/
	private Image backgroundImage ;
	
	/***/
	private JLabel textLabel ;
	
	private JProgressBar p ;
	
	InitViewPanel () 
	{
		super () ;
	}
	
	public void setText ( String text ) 
	{
		if ( text != null )
		{
			textLabel.setText ( text ) ;
			repaint () ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}

	@Override
	protected void createComponents () 
	{
		try 
		{
			textLabel = new JLabel () ;
			p = new JProgressBar () ;
			backgroundImage = GraphicsUtilities.getImage ( BACKGROUND_IMAGE_FILE_PATH ) ;
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
			
	}

	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		textLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
		layoutComponent ( textLabel , 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( p , 0 , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		p.setIndeterminate ( true ) ;
					
	}

	@Override
	protected void bindListeners () 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void injectComponents () 
	{
		add ( textLabel ) ;
		add ( p ) ;
	}
	
}