package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

public class NotificationPanel extends FrameworkedWithGridBagLayoutPanel
{

	private JTextArea notificationArea ;
	
	public NotificationPanel () 
	{
		super () ;
	}
	
	@Override
	protected void createComponents () 
	{
		notificationArea = new JTextArea () ;
	}

	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( notificationArea , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		notificationArea.setBorder ( new TitledBorder ( "Notifications" ) );
	}

	@Override
	protected void bindListeners() {}

	@Override
	protected void injectComponents() 
	{
		add ( notificationArea ) ;
	}

	public void addNotification ( String notification ) 
	{
		notificationArea.append ( notification ) ;
	}
	
	public void clearNotifications () 
	{
		notificationArea.setText ( Utilities.EMPTY_STRING ) ;
	}
	
}
