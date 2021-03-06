package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.JSheeplandServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		JSheeplandServer jSheeplandServer ;
		threadExecutor = null ;
		try 
		{
			threadExecutor = Executors.newSingleThreadExecutor () ;
			jSheeplandServer = JSheeplandServer.getInstance () ; 
			threadExecutor.submit ( jSheeplandServer ) ;
		}
		catch ( IOException e ) 
		{
			Logger.getGlobal().log ( Level.SEVERE ,  "SERVER_MAIN_CLASS : CAN NOT CREATE A NETWORK_COMMUNICATION_CONTROLLER." + Utilities.CARRIAGE_RETURN + "Error details : " + e.getMessage () , e );
			if ( threadExecutor != null )
				threadExecutor.shutdownNow () ;
		}	
	}
	
}