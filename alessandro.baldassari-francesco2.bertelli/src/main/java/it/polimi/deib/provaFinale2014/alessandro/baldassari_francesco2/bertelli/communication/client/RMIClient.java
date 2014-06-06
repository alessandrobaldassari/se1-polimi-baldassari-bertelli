package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientCommunicationProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * RMI-based implementation of the Client abstraction entity. 
 */
public class RMIClient extends Client 
{

	/**
	 * The IP address where locate the Server. 
	 */
	private final static String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The port where contact the Server. 
	 */
	private final static int SERVER_PORT = 3334 ;

	/**
	 * A broker object to interact with the Server. 
	 */
	private RMIClientBroker clientBroker ;
	
	/**
	 * @param communicationProtocolResponser the value for the communicationProtocolResponser field.
	 */
	public RMIClient ( CommunicationProtocolResponser communicationProtocolResponser ) 
	{
		super ( communicationProtocolResponser ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalConnect() throws IOException 
	{
		RMIServer server ;
		String key ;
		Registry registry ;
		try 
		{
			System.out.println ( "RMI - TECHNICAL CONNECT : BEGIN" ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : LOCATING RMI REGISTRY." ) ;
			registry = LocateRegistry.getRegistry ( SERVER_IP_ADDRESS , SERVER_PORT ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : RMI REGISTRY LOCATED." ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : RETRIEVING INITIAL CONNECTION SERVER." ) ;
			server = ( RMIServer ) registry.lookup ( RMIServer.LOGICAL_SERVER_NAME ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : INITIAL CONNECTION SERVER RETRIEVED." ) ;
			key = server.addPlayer () ; 
			clientBroker = ( RMIClientBroker ) registry.lookup ( key ) ;
		} 
		catch ( NotBoundException e ) 
		{
			e.printStackTrace();
		}
	}	
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalDisconnect () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected Message read () throws IOException 
	{
		Message res ;
		System.out.println ( "SOCKET_CLIENT - READ : WAITING FOR A MESSAGE." ) ; 			
		while ( clientBroker.isServerReady () == false ) ;
		res = clientBroker.getNextMessage () ;
		System.out.println ( "SOCKET_CLIENT - READ : MESSAGE READ." ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void write ( Message m ) throws IOException 
	{
		try 
		{
			System.out.println ( "SOCKET_CLIENT - WRITE : WRITING MESSAGE" ) ; 
			clientBroker.putNextMessage ( m ) ;
			System.out.println ( "SOCKET_CLIENT - WRITE : MESSAGE WRITTEN" ) ;
		}
		catch ( AnotherCommandYetRunningException e ) 
		{
			throw new IOException ( e ) ;
		}
	}
	
	/***
	 * AS THE SUPER'S ONE
	 */
	@Override
	protected void operationFinished () throws IOException 
	{
		clientBroker.setClientReady () ;
	}

	
}
