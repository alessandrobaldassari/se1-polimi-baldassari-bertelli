package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.ServerEnvironment;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.UIDGenerator;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executors;

public class RMIGUIMapServer implements Runnable , GameMapObserver , Serializable
{
		
	private static int session = 0 ;
	
	private UIDGenerator uidGen ;
	
	private static Registry myRegistry ;

	private List < GUIGameMapNotificationMessage > messages ;
		
	private Map < String , RMIGUIGameMapNotifier > notifiers ;
	
	public RMIGUIMapServer () throws RemoteException 
	{
		super () ;
		if ( myRegistry == null )
		{
			myRegistry = LocateRegistry.createRegistry ( ServerEnvironment.RMI_GUI_MAP_SERVER_PORT ) ;
		}
		uidGen = new UIDGenerator ( 0 ) ;
		messages = new Vector <GUIGameMapNotificationMessage> () ;
		notifiers = new LinkedHashMap < String , RMIGUIGameMapNotifier > () ;
		System.out.println ( "RMI_GUI_MAP_SERVER - : REGISTRY LOCATED : " + myRegistry ) ;
	}
	
	public String addClient () 
	{
		RMIGUIClientBroker stub ;
		RMIGUIClientBroker broker ;
		String res ;
		try 
		{
			System.out.println ( "RMI_GUI_MAP_SERVER - ADD_PLAYER : BEGIN_ACCEPT_REQUEST" ) ;
			// initialize the broker.
			broker = new RMIGUIClientBrokerImpl () ;
			System.out.println ( "RMI_GUI_MAP_SERVER - EXPORT CLIENT BROKER FOR THIS REQUEST" ) ;
			// create a stub for the broker.
			stub = ( RMIGUIClientBroker ) UnicastRemoteObject.exportObject ( broker , 0 ) ;		
			System.out.println ( "RMI_GUI_MAP_SERVER - ADD_PLAYER : BROKER IS OUT." ) ;
			//
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : BINDING THE BROKER" ) ;
			// generating a name for this broker
			session ++ ;
			res = "RMI_GUI_CLIENT_BROKER_" + session + "_" + uidGen.generateNewValue() ;
			// bind the broker and make it available to name services.
			myRegistry.bind ( res , stub ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : BROKER BOUND." ) ;
			notifiers.put ( res , new RMIGUIGameMapNotifier ( messages , broker ) ) ;
			Executors.newSingleThreadExecutor().execute ( notifiers.get ( res ) ) ;
		}
		catch ( RemoteException r ) 
		{
			// here probably the stub has not been created.
			res = null ;
			throw new RuntimeException ( r );
		}
		catch ( AlreadyBoundException e ) 
		{
			// this should never happen.
			throw new RuntimeException ( e ) ;
		} 
		return res ;
	}
	
	public PlayerObserver getPlayerObserver ( String key ) 
	{
		return notifiers.get ( key ) ;
	}
	
	@Override
	public void run() 
	{
		while ( true ) ;
	}

	/**
	 * AS THE SUPER'S ONE 
	 */
	@Override
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , 
			PositionableElementType whoType, Integer whoId ) 
	{
		notificationAlgo ( "ADDED" , whereType , whereId , whoType , whoId ) ;
	}

	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public void onPositionableElementRemoved ( GameMapElementType whereType,
			Integer whereId, PositionableElementType whoType , Integer whoId)
	{
		notificationAlgo ( "REMOVED" , whereType , whereId , whoType , whoId ) ;	Vector cc ;	
	}
	
	/***/
	private void notificationAlgo ( String actionAssociated , GameMapElementType whereType,
			int whereId, PositionableElementType whoType, int whoId ) 
	{
		GUIGameMapNotificationMessage m ;
		m = new GUIGameMapNotificationMessage ( actionAssociated , whereType , whereId , whoType , whoId ) ;
		messages.add ( messages.size () , m ) ;
	}
	
}
