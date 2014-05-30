package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientHandler;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/***/
public interface RMIServer extends Remote 
{
	
	public static final String SERVER_NAME = "SHEEPLAND" ; 
	
	public static final int RMI_SERVER_PORT = 3334 ;
	
	public String addPlayer ( String name ) throws RemoteException ;
	
}

/***/
class RMIServerImpl implements RMIServer 
{

	private static int lastRMIIdGenerated = -1 ;

	private Registry registry ;
	
	private MasterServer masterServer ;
	
	RMIServerImpl ( MasterServer masterServer ) throws RemoteException 
	{
		if ( masterServer != null )
		{
			this.masterServer = masterServer ;
			registry = LocateRegistry.getRegistry ( "localhost" , 3334 ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/***/
	@Override
	public String addPlayer ( String name ) throws RemoteException 
	{
		String clientBrokerName ;
		RMIClientBroker stub ;
		RMIClientHandler clientHandler ;
		clientHandler = new RMIClientHandler () ;
		masterServer.addPlayer ( clientHandler )  ;
		lastRMIIdGenerated ++ ;
		clientBrokerName = "RMI_CLIENT_BROKER_" + lastRMIIdGenerated ;
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
