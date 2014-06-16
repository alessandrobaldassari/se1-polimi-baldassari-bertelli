package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.gui;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.GUINotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.GUIPlayerNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.RMIGUIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

public class RMIGUIMapClient implements Runnable
{

	/**
	 * The IP address where locate the Server. 
	 */
	private final static String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The port where contact the Server. 
	 */
	private final static int SERVER_DIRECT_PORT = 7000 ;
	
	/***/
	private final String brokerName ;
	
	/**
	 * A broker object to interact with the Server. 
	 */
	private RMIGUIClientBroker clientBroker ;
	
	/***/
	private boolean on ;
	
	private ViewPresenter viewPresenter ;
	
	private GameMapObserver gameMapObserver ;
	
	private PlayerObserver playerObserver ;
	
	/***/
	public RMIGUIMapClient ( String brokerName ) 
	{
		super () ;
		this.brokerName = brokerName ;
		viewPresenter = null ;
	}
	
	public void setGameMapObserver ( GameMapObserver g ) 
	{
		this.gameMapObserver = g ;
	}
	
	public void connect ( ViewPresenter viewPresenter ) throws IOException 
	{
		Registry registry ;
		try 
		{
			System.out.println ( "RMI_GUI_MAP_CLIENT - CONNECT : BEGIN" ) ;
			System.out.println ( "RMI_GUI_MAP_CLIENT - CONNECT : LOCATING RMI REGISTRY." ) ;
			registry = LocateRegistry.getRegistry ( SERVER_IP_ADDRESS , SERVER_DIRECT_PORT ) ;
			System.out.println ( "RMI_GUI_MAP_CLIENT - CONNECT : RMI REGISTRY LOCATED." ) ;
			System.out.println ( "RMI_GUI_MAP_CLIENT - CONNECT : RETRIEVING INITIAL CONNECTION SERVER." ) ;
			clientBroker = ( RMIGUIClientBroker ) registry.lookup ( brokerName ) ;
			on = true ;
			this.viewPresenter = viewPresenter ;
		} 
		catch ( NotBoundException e ) 
		{
			viewPresenter.stopApp();
		}
	}	
	
	@Override
	public void run () 
	{
		GUINotificationMessage m ;
		String methodName ;
		while ( on )
		{
			try 
			{
				System.out.println ( "RMI_GUI_MAP_CLIENT : WAITING FOR A MESSAGE" ) ;
				m = ( GUINotificationMessage ) clientBroker.getMessage () ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : MESSAGE CATCH" ) ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : BEFORE NOTIFYING." ) ;
				if ( m instanceof GUIGameMapNotificationMessage )
					manageGameMapMessage ( (GUIGameMapNotificationMessage) m ) ;
				else
					mangePlayerMessage ( (GUIPlayerNotificationMessage) m ) ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : AFTER NOTIFYING." ) ;
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				viewPresenter.stopApp();
			} 
		}		
	}
	
	private void manageGameMapMessage ( GUIGameMapNotificationMessage m ) 
	{
		if ( gameMapObserver != null )
		{
		if ( m.getActionAssociated ().compareTo ( "ADDED" ) == 0 )
			gameMapObserver.onPositionableElementAdded ( m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () ) ;
		else
			gameMapObserver.onPositionableElementRemoved ( m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () ) ;
		}
	}
	
	private void mangePlayerMessage ( GUIPlayerNotificationMessage m )
	{
		if ( playerObserver != null )
		{
			if ( m.getActionAssociated().compareToIgnoreCase ( "onPay" ) == 0 )
				playerObserver.onPay ( ( Integer ) m.getFirstParam() , (Integer) m.getSecondParam() );
			else
				if ( m.getActionAssociated().compareToIgnoreCase ( "onGetPayed" ) == 0 )
					playerObserver.onGetPayed ( ( Integer ) m.getFirstParam() , (Integer) m.getSecondParam() );
				else
					if ( m.getActionAssociated().compareToIgnoreCase ( "onCardAdded" ) == 0 )
						playerObserver.onCardAdded ( (Card) m.getFirstParam() );
					else
						if ( m.getActionAssociated().compareToIgnoreCase ( "onCardRemoved" ) == 0 )
							playerObserver.onCardRemoved ( (Card) m.getFirstParam() );
		}
	}
	
}
