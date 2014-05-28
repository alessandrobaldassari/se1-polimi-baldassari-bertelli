package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.RMIServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient extends Client  
{

	
	private final static String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	private final static int SERVER_PORT = 3334 ;
	
	private RMIServer server ;

	/***/
	@Override
	public void technicalConnect() throws IOException 
	{
		Registry registry ;
		registry = LocateRegistry.getRegistry ( "localhost" , SERVER_PORT ) ;
		try 
		{
			server = ( RMIServer ) registry.lookup ( RMIServer.SERVER_NAME ) ;
			server.addPlayer( "uri" ) ; 
		} 
		catch ( NotBoundException e ) 
		{
			e.printStackTrace();
		}
	}	
	
	/***/
	@Override
	public void technicalDisconnect () 
	{
	}
	
}
