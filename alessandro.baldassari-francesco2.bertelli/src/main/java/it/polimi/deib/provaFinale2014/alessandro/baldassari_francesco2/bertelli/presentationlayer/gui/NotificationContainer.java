package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

/**
 * This interface represents a Component that contains a NotificationPanel. 
 */
public interface NotificationContainer 
{

	/**
	 * Ask this NotificationContainer to display a new notification.
	 * 
	 * @param notification the notification to show.
	 */
	public void addNotification ( String notification ) ;
	
}
