package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.NetworkCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.NetworkCommunicationControllerSingleton;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* This class represents the entry point of the Server Process.
* It's main method is the one that launches all the stuff related to the server component.
*/
public final class ServerMainClass 
{

	/***/
	private ServerMainClass () {}
	
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
		NetworkCommunicationController networkCommunicationController ;
		threadExecutor = null ;
		try 
		{
			threadExecutor = Executors.newSingleThreadExecutor () ;
			networkCommunicationController = NetworkCommunicationControllerSingleton.getInstance () ; 
			threadExecutor.submit ( networkCommunicationController ) ;
		}
		catch ( IOException e ) 
		{
			System.out.println ( "SERVER_MAIN_CLASS : CAN NOT CREATE A NETWORK_COMMUNICATION_CONTROLLER." + Utilities.CARRIAGE_RETURN + "Error details : " + e.getMessage () ) ;
			if ( threadExecutor != null )
				threadExecutor.shutdownNow () ;
		}	
	}
	
}