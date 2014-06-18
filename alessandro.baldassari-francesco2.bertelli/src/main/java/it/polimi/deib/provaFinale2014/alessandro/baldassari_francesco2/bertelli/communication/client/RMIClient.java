package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.connectionresuming.RMIConnectionResumerServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RMIRequestAcceptServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RMIClientBroker.AnotherCommandYetRunningException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
	private final static int SERVER_DIRECT_PORT = 3334 ;

	/***/
	private static final int SERVER_CONNECTION_RESUME_PORT = 3332 ; 
	
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
	public void directTechnicalConnect() throws IOException 
	{
		RMIRequestAcceptServer server ;
		String key ;
		Registry registry ;
		try 
		{
			System.out.println ( "RMI - TECHNICAL CONNECT : BEGIN" ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : LOCATING RMI REGISTRY." ) ;
			registry = LocateRegistry.getRegistry ( SERVER_IP_ADDRESS , SERVER_DIRECT_PORT ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : RMI REGISTRY LOCATED." ) ;
			System.out.println ( "RMI - TECHNICAL CONNECT : RETRIEVING INITIAL CONNECTION SERVER." ) ;
			server = ( RMIRequestAcceptServer ) registry.lookup ( RMIRequestAcceptServer.SERVER_NAME ) ;
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
	public void resumeConnectionConnect () throws IOException
	{
		RMIConnectionResumerServer s ;
		RMIClientBroker br ;
		Registry registry ;
		String res ;
		registry = LocateRegistry.getRegistry( SERVER_IP_ADDRESS , SERVER_CONNECTION_RESUME_PORT ) ;
		try 
		{
			System.out.println ( "RMI CLIENT - TRY RESUME CONNECTION : BEGIN" ) ;		
			System.out.println ( "RMI CLIENT - TRY RESUME CONNECTION :" ) ;	
			s = ( RMIConnectionResumerServer ) registry.lookup ( RMIConnectionResumerServer.LOGICAL_SERVER_NAME ) ;
			s.resumeMe ( getUID () ) ;
			while ( s.areYouReadyForMe ( getUID () ) == null ) ;
			res = s.areYouReadyForMe ( getUID () ) ;
			if ( res.compareTo ( "KO" ) == 0 )
				throw new IOException () ;
			else
			{
				br = (RMIClientBroker) registry.lookup ( res ) ;
				this.clientBroker = br ;
			}
		}
		catch ( NotBoundException e ) 
		{
			System.out.println ( "RMI CLIENT - TRY RESUME CONNECTION : RESUMER SERVER NOT FOUND" ) ;		
			throw new IOException ( e ) ;
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
