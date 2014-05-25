package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the entry point of the Server Process.
 * It's main method is the one that launches all the stuff related to the server component.
 */
public class ServerStartPoint 
{

	/**
	 * This is the entry point for the Server Components
	 * 
	 * @param args the command line arguments
	 * @throws RuntimeException if something goes wrong with the creation of the
	 *         Master Server.
	 */
	public static void main ( String [] args ) 
	{
		ExecutorService threadExecutor ;
		MasterServer masterServer ;
		try 
		{
			threadExecutor = Executors.newSingleThreadExecutor () ;
			masterServer = new MasterServer () ;
			threadExecutor.submit ( masterServer ) ;
		}
		catch ( IOException e ) 
		{
			e.printStackTrace () ;
			throw new RuntimeException ( e ) ;
		}
	}

}
