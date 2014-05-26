package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

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

	RMIServerImpl ( MasterServer masterServer ) {}

	@Override
	public void addPlayer ( String name ) throws RemoteException 
	{
		System.out.println ( "Ricevuto : " + name ) ;
	}

}
