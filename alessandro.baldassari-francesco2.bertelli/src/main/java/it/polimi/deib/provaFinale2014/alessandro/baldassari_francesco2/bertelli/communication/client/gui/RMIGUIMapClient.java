package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.gui;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.GUIMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.RMIGUIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

public class RMIGUIMapClient extends WithReflectionAbstractObservable < GameMapObserver > implements Runnable
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
	
	/***/
	public RMIGUIMapClient ( String brokerName ) 
	{
		super () ;
		this.brokerName = brokerName ;
		viewPresenter = null ;
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
		GUIMapNotificationMessage m ;
		String methodName ;
		while ( on )
		{
			try 
			{
				System.out.println ( "RMI_GUI_MAP_CLIENT : WAITING FOR A MESSAGE" ) ;
				m = ( GUIMapNotificationMessage ) clientBroker.getMessage () ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : MESSAGE CATCH" ) ;
				if ( m.getActionAssociated ().compareTo ( "ADDED" ) == 0 )
					methodName = "onPositionableElementAdded" ;
				else
					methodName = "onPositionableElementRemoved" ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : BEFORE NOTIFYING." ) ;
				notifyObservers ( methodName , m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () );
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : AFTER NOTIFYING." ) ;
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				viewPresenter.stopApp();
			} 
			catch  ( MethodInvocationException e ) 
			{
				e.printStackTrace();
				viewPresenter.stopApp();
			}
		}		
	}

}
