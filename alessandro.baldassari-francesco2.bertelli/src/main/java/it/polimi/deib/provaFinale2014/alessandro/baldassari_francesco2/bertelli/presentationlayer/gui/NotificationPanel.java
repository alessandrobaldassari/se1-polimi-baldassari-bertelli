package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.DynamicResizingManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;

/**
 * This class represents a Component that shows some notifications to the user 
 */
public class NotificationPanel extends FrameworkedWithGridBagLayoutPanel
{

	/**
	 * The scroll pane that will contain the notification area. 
	 */
	private JScrollPane scrollPane ;
	
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
		scrollPane = new JScrollPane () ;
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
		layoutComponent ( scrollPane , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		scrollPane.setViewportView ( notificationArea ) ;
		scrollPane.setOpaque ( false ) ;
		scrollPane.setBorder ( new TitledBorder ( "Whatsapp(ening)" ) );
		notificationArea.setEditable(false) ;
		notificationArea.setOpaque(false); 
		setOpaque ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners() 
	{
		notificationArea.addComponentListener ( new DynamicResizingManager ( scrollPane ) ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents() 
	{
		add ( scrollPane ) ;
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
		notificationArea.requestFocus () ;
	}
	
	/**
	 * Remove all the notifications from this NotificationPanel. 
	 */
	public void clearNotifications () 
	{
		notificationArea.setText ( Utilities.EMPTY_STRING ) ;
		notificationArea.requestFocus () ;
	}
	
}

class NotificationViewModel 
{

	private List < String > notifications ;

	public NotificationViewModel () 
	{
		notifications = new LinkedList < String > () ;
	}
	
	public void addNotification ( String newNot )
	{
		if ( newNot != null )
			notifications.add ( notifications.size () , newNot );
	}
 	
}
