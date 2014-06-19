package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextArea;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;

/**
 * This class represents a Component that shows some notifications to the user 
 */
public class NotificationPanel extends FrameworkedWithGridBagLayoutPanel
{

	/**
	 * The technical area where notifications will be shown. 
	 */
	private JTextArea notificationArea ;
	
	/***/
	public NotificationPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		notificationArea = new JTextArea () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( notificationArea , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		notificationArea.setEditable(false) ;
		notificationArea.setOpaque(false); 
		setOpaque ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners() {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents() 
	{
		add ( notificationArea ) ;
	}

	/**
	 * Add a new notification to this NotificationPanel.
	 * 
	 * @param notification the new notification to show.
	 */
	public void addNotification ( String notification ) 
	{
		notificationArea.append ( Utilities.CARRIAGE_RETURN ) ;
		notificationArea.append ( notification ) ;
		repaint () ;
	}
	
	/**
	 * Remove all the notifications from this NotificationPanel. 
	 */
	public void clearNotifications () 
	{
		notificationArea.setText ( Utilities.EMPTY_STRING ) ;
	}
	
}
