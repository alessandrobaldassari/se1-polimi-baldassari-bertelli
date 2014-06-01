package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WithGridBagLayoutPanel;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/***/
public class InitView extends JDialog 
{
	
	/***/
	private InitViewPanel i ;
	
	/***/
	public InitView () 
	{
		super ( ( Frame ) null , "JSheepland" , true ) ;
		i = new InitViewPanel () ;
		add ( i ) ;
	}
	
}

/***/
class InitViewPanel extends WithGridBagLayoutPanel 
{

	/***/
	private final String BACKGROUND_IMAGE_FILE_PATH = "sheepland_init.jpg"; ;
	
	/***/
	private final Image backgroundImage ;
	
	/***/
	private JLabel textLabel ;
	
	private JProgressBar p ;
	
	InitViewPanel () 
	{
		super () ;
 		try 
 		{
 			Insets insets ;
 			textLabel = new JLabel () ;
 			p = new JProgressBar () ;
 			insets = new Insets ( 5 , 5 , 5 , 5 ) ;
 			backgroundImage = GraphicsUtilities.getImage ( BACKGROUND_IMAGE_FILE_PATH ) ;
 			textLabel.setText ( "Connession al server, prego attendere ..." ) ;
 			textLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
 			layoutComponent ( textLabel , 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
 			layoutComponent ( p , 0 , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
 			p.setIndeterminate ( true ) ;
 			add ( textLabel ) ;
 			add ( p ) ;
 		} 
 		catch ( IOException e ) 
 		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
 		}
	}
	
	/***/
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}
	
}