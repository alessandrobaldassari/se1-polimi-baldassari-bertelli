package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientHandler;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote 
{
	
	public static final String SERVER_NAME = "SHEEPLAND" ; 
	
	public static final int RMI_SERVER_PORT = 3334 ;
	
	public void addPlayer ( String name ) throws RemoteException ;
	
}

class RMIServerImpl implements RMIServer 
{

	private MasterServer masterServer ;
	
	RMIServerImpl ( MasterServer masterServer ) 
	{
		if ( masterServer != null )
			this.masterServer = masterServer ;
		else
			throw new IllegalArgumentException () ;
	}

	@Override
	public void addPlayer ( String name ) throws RemoteException 
	{
		ClientHandler clientHandler ;
		clientHandler = new RMIClientHandler () ;
		masterServer.addPlayer();
		System.out.println ( "Ricevuto : " + name ) ;
	}

}
