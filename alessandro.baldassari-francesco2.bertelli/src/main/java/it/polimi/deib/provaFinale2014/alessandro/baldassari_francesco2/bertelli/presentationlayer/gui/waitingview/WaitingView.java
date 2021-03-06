package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.waitingview;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.NotificationContainer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.NotificationPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * This View offer a component to use during waiting times. 
 */
public class WaitingView extends JDialog implements NotificationContainer
{
	
	/**
	 * The effective view.
	 */
	private WaitingViewPanel waitingPanel ;
	
	/***/
	public WaitingView () 
	{
		super ( ( Frame ) null , PresentationMessages.APP_NAME , false ) ;
		waitingPanel = new WaitingViewPanel () ;
		add ( waitingPanel ) ;
		setSize ( GraphicsUtilities.getThreeFourthVgaResolution() ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
	}
	
	/**
	 * Set the text to display during the waiting time. 
	 */
	public void setText ( String text ) 
	{
		waitingPanel.setText ( text ) ;
		repaint () ;
		waitingPanel.requestFocus () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addNotification ( String msg ) 
	{
		waitingPanel.addNotification ( msg ) ;
	}
	
}

/**
 * The effective WaitingView panel.
 */
class WaitingViewPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	/**
	 * The background image for this Panel. 
	 */
	private Image backgroundImage ;
	
	/**
	 * The Label that indicates the User why is he waiting. 
	 */
	private JLabel textLabel ;
	
	/**
	 * 
	 */
	private JScrollPane scrollPane ;
	
	/**
	 * An area to show notification to the user during the waiting time. 
	 */
	private NotificationPanel notificationArea ;
	
	/**
	 * A ProgressBar to indicate the User that something is working behind the scenes. 
	 */
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
			repaint () ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public void addNotification ( String n ) 
	{
		notificationArea.addNotification ( n ) ;
		repaint () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		if ( backgroundImage != null )
			g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		textLabel = new JLabel () ;
		scrollPane = new JScrollPane () ;
		notificationArea = new NotificationPanel();
		p = new JProgressBar () ;
		backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getBackgroundImage ( true ) ;	
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		textLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
		layoutComponent ( textLabel , 0 , 0 , 1 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( scrollPane , 0 , 1 , 1 , 0.75 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( p , 0 , 2 , 1 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		p.setIndeterminate ( true ) ;
		scrollPane.setViewportView(notificationArea); 
		scrollPane.setOpaque(false); 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( textLabel ) ;
		add ( scrollPane ) ;
		add ( p ) ;
	}
	
}