package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.MessageView.MessageViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableFrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WithBackgroundImagePanel;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/***/
public class MessageView extends JDialog 
{

	/***/
	private MessageViewPanel messageViewPanel ;
	
	/***/
	protected MessageView ( MessageViewObserver obs )  
	{
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		messageViewPanel = new MessageViewPanel () ;
		messageViewPanel.addObserver ( obs ) ;
		add ( messageViewPanel ) ;
		setAlwaysOnTop ( true ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
	}
	
	/***/
	public void setMessage ( String msg ) 
	{
		messageViewPanel.setMessage ( msg ) ;
	}
	
	/***/
	public interface MessageViewObserver extends Observer 
	{
		
		/***/
		public void onMessageRead () ;
		
	}
	
}

class MessageViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel < MessageViewObserver >
{

	public static final String BACKGROUND_IMAGE_PATH = "sheepland_init.jpg" ;
	
	public static final String ICON_IMAGE_PATH = "sheepland_map.jpg" ;
	
	private BufferedImage backgroundImage ;
	
	private BufferedImage iconImage ;
	
	private WithBackgroundImagePanel iconPanel ;
	
	private JLabel messageLabel ;
	
	private JButton okButton ;
	
	public MessageViewPanel ()  
	{
		super () ;
		try 
		{
			backgroundImage = GraphicsUtilities.getImage ( BACKGROUND_IMAGE_PATH ) ;
			iconImage = GraphicsUtilities.getImage ( ICON_IMAGE_PATH ) ;
			iconPanel.setBackgroundImage ( iconImage ) ;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setMessage ( String msg ) 
	{
		messageLabel.setText ( Utilities.fromBackslashnStringToBrHtmlString ( msg ) ) ;
	}
	
	@Override
	protected void createComponents () 
	{
		iconPanel = new WithBackgroundImagePanel () ;
		messageLabel = new JLabel () ;
		okButton = new JButton () ;
	}

	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( iconPanel , 0 , 0 , 0.3 , 0.9 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets );
		layoutComponent ( messageLabel , 1 , 0 , 0.7 , 0.9 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets );
		layoutComponent ( okButton , 0 , 1 , 1 , 0.1 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
	}

	@Override
	protected void bindListeners () 
	{
		Action okAction ;
		okAction = new OkAction () ;
		okButton.setAction ( okAction ) ;
	}

	@Override
	protected void injectComponents () 
	{
		add ( iconPanel ) ;
		add ( messageLabel ) ;
		add ( okButton ) ;
	}
	
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}
	
	private class OkAction extends AbstractAction 
	{

		public static final String MESSAGE = "Capito." ;
		
		public OkAction () 
		{
			super ( MESSAGE ) ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				notifyObservers ( "onMessageRead" );
			}
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}		
		
	}

}
