package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.MatchAdderCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientHandler;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This interface defines the behavior of an RMI Server  
 */
public interface RMIServer extends Remote 
{
	
	/**
	 * The logical name of this RMI Server.
	 */
	public static final String LOGICAL_SERVER_NAME = "SHEEPLAND_RMI_SERVER" ; 
	
	/**
	 * The base server port associated with this Server. 
	 */
	public static final int SERVER_PORT = 3334 ;
 	
	/**
	 * Allows a Client to request to add himself to a Match.
	 * 
	 * @return a String containing a key that will allow the client to retrieve an object
	 *         he will use to communicate with the Server application.
	 * @throws RemoteException if something goes wrong with the connections.
	 */
	public String addPlayer () throws RemoteException ;
	
}

/**
 * Implementation of the RMI Server interface subclassing the RequestAccepterServer
 */
class RMIServerImpl extends RequestAccepterServer implements RMIServer 
{

	/**
	 * Sleep time for a dummy-implementation of the lifeLoopImplementation method. 
	 */
	private static final long SLEEP_TIME = 25000L ;
	
	/**
	 * Static variable to generate a different name for each ClientHandler. 
	 */
	private static int lastRMIIdGenerated = -1 ;

	/**
	 * Registry object to implement the RMI protocol. 
	 */
	private Registry registry ;
	
	/**
	 * @param matchAdderCommunicationController the matchAdderCommunicationController field value.
	 * @param localhostAddress the IP address of the localhost where the created Server will be deployed.
	 * @param registryPort the port where the RMI Registry associated with the created Server will be listening
	 * @throws IllegalArgumentException if the localhostAddress parameter is null or the registryPort
	 *         parameter is < 0
	 */
	RMIServerImpl ( MatchAdderCommunicationController matchAdderCommunicationController , String localhostAddress , int registryPort ) throws RemoteException 
	{
		super ( matchAdderCommunicationController ) ;
		if ( localhostAddress != null && registryPort >= 0 )
		{
			registry = LocateRegistry.getRegistry ( localhostAddress , registryPort ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String addPlayer () throws RemoteException 
	{
		String clientBrokerName ;
		RMIClientHandler clientHandler ;
		RMIClientBroker stub ;
		RMIClientBroker broker ;
		System.out.println ( "RMI_SERVER : BEGIN_ACCEPT_REQUEST" ) ;
		broker = new RMIClientBrokerImpl () ;
		stub = (RMIClientBroker) UnicastRemoteObject.exportObject ( broker , 0 ) ;		
		clientHandler = new RMIClientHandler ( broker ) ;
		submitToMatchAdderCommunicationController ( clientHandler )  ;
		lastRMIIdGenerated ++ ;
		clientBrokerName = RMIClientHandler.LOGICAL_ABSTRACT_RMI_CLIENT_HANDLER + lastRMIIdGenerated ;
		try 
		{
			registry.bind ( clientBrokerName , stub ) ;
		}
		catch ( AlreadyBoundException e ) 
		{
			e.printStackTrace();
			throw new RemoteException () ;
		}
		System.out.println ( "RMI_SERVER : END_ACCEPT_REQUEST" ) ;
		return clientBrokerName ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void acceptRequest () 
	{
		try 
		{
			addPlayer () ;
		} 
		catch ( RemoteException e ) 
		{
			e.printStackTrace () ;
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalStart () throws IOException
	{
		Registry registry ;
		RMIServer stub ;
		registry = LocateRegistry.createRegistry ( SERVER_PORT ) ;
		stub = ( RMIServer ) UnicastRemoteObject.exportObject ( this , 0 ) ;
		registry.rebind ( LOGICAL_SERVER_NAME , stub ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void lifeLoopImplementation () 
	{
		try 
		{
			Thread.sleep ( SLEEP_TIME ) ;
		} 
		catch ( InterruptedException e ) 
		{
			e.printStackTrace();
		}
	}

}
