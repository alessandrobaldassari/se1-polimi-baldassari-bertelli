package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientStartPoint 
{

	public static void main ( String [] args ) 
	{
		ExecutorService threadExecutor ;
		Client client ;
		try 
		{
			threadExecutor = Executors.newSingleThreadExecutor () ;
			client = new SocketClient () ;
			client.technicalConnect() ;
			threadExecutor.submit ( client ) ;
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
	}
	
}
