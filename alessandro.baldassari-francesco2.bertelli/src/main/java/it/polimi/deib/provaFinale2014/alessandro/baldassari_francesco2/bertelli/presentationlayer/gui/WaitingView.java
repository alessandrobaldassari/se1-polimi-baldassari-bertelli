package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * This View offer a component to use during waiting times. 
 */
public class WaitingView extends JDialog 
{
	
	/**
	 * The effective view.
	 */
	private WaitingViewPanel waitingPanel ;
	
	/***/
	public WaitingView () 
	{
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		waitingPanel = new WaitingViewPanel () ;
		add ( waitingPanel ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
	}
	
	/**
	 * Set the text to display during the waiting time. 
	 */
	public void setText ( String text ) 
	{
		waitingPanel.setText ( text ) ;
	}
	
	public void addNotification ( String msg ) 
	{
		waitingPanel.addNotification ( msg ) ;
	}
	
	// INNER INTERFACES
	
	public interface WaitingViewObserver extends Observer {}
	
}

/**
 * The  
 */
class WaitingViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/***/
	private final String BACKGROUND_IMAGE_FILE_PATH = "sheepland_init.jpg"; ;
	
	/***/
	private Image backgroundImage ;
	
	/***/
	private JLabel textLabel ;
	
	/***/
	private NotificationPanel notificationArea ;
	
	/***/
	private JProgressBar p ;
	
	/***/
	protected WaitingViewPanel () 
	{
		super () ;
	}
	
	/***/
	public void setText ( String text ) 
	{
		if ( text != null )
		{
			textLabel.setText ( text ) ;
			textLabel.repaint () ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public void addNotification ( String n ) 
	{
		notificationArea.addNotification ( n ) ;
		notificationArea.repaint () ;
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
			notificationArea = new NotificationPanel();
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
		layoutComponent ( textLabel , 0 , 0 , 1 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( notificationArea , 0 , 1 , 1 , 0.75 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( p , 0 , 2 , 1 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		p.setIndeterminate ( true ) ;			
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () {}

	@Override
	protected void injectComponents () 
	{
		add ( textLabel ) ;
		add ( notificationArea ) ;
		add ( p ) ;
	}
	
}