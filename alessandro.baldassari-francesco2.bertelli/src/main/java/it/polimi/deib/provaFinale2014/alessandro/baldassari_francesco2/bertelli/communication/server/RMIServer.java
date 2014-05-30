package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientHandler;

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
	
	public static final String LOGICAL_ABSTRACT_RMI_CLIENT_HANDLER = "SHEEPLAND_RMI_CLIENT_HANDLER_#_" ;
	
	public static final int SERVER_PORT = 3334 ;
 	
	/**
	 *  
	 */
	public String addPlayer ( String name ) throws RemoteException ;
	
}

/**
 * Implementation of the RMI Server interface 
 */
class RMIServerImpl implements RMIServer 
{

	/**
	 * Static variable to generate a different name for each ClientHandler. 
	 */
	private static int lastRMIIdGenerated = -1 ;

	/**
	 * Registry object to implement the RMI protocol. 
	 */
	private Registry registry ;
	
	/***/
	private MatchAdderCommunicationController matchAdderCommunicationController ;
	
	RMIServerImpl ( String localhostAddress , int registryPort , MatchAdderCommunicationController matchAdderCommunicationController ) throws RemoteException 
	{
		if ( matchAdderCommunicationController != null && localhostAddress != null )
		{
			this.matchAdderCommunicationController = matchAdderCommunicationController ;
			registry = LocateRegistry.getRegistry ( localhostAddress , registryPort ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String addPlayer ( String name ) throws RemoteException 
	{
		String clientBrokerName ;
		RMIClientHandler clientHandler ;
		RMIClientBroker stub ;
		clientHandler = new RMIClientHandler () ;
		matchAdderCommunicationController.addPlayer ( clientHandler )  ;
		lastRMIIdGenerated ++ ;
		clientBrokerName = LOGICAL_ABSTRACT_RMI_CLIENT_HANDLER+lastRMIIdGenerated ;
		stub = ( RMIClientBroker ) UnicastRemoteObject.exportObject ( ( Remote ) clientHandler , 0 ) ;
		System.out.println ( "Ricevuto : " + name ) ;
		try 
		{
			registry.bind ( clientBrokerName , stub ) ;
		}
		catch (AlreadyBoundException e) {
			e.printStackTrace();
			throw new RemoteException () ;
		}
		return clientBrokerName ;
	}

}
