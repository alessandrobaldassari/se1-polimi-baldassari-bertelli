package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller.MatchLauncherCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller.MatchStartCommunicationController;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class implements the Singleton Pattern for the NetworkCommunicationController entity. 
 */
public class NetworkCommunicationControllerSingleton 
{

	/**
	 * A static reference to the only living instance of this class.
	 * Needed to implement the Singleton pattern. 
	 */
	private static NetworkCommunicationController instance ;

	/**
	 * Singleton method for this class.
	 * 
	 * @return the only instance of this class.  
	 * @throws IOException if something goes wrong with the creation of the only living instance of this class.
	 */
	public static NetworkCommunicationController getInstance () throws IOException 
	{
		if ( instance == null )
			instance = new NetworkCommunicationControllerImpl () ;
		return instance ;	
	}
	
	/**
	 * A default implementation for the NetworkCommunicationController.
	 * The only things it has to do is to launch a MatchLauncherCommunicationController object
	 */
	private static class NetworkCommunicationControllerImpl implements NetworkCommunicationController 
	{

		/**
		 * An executor object to exec the MatchLauncherCommunicationController 
		 */
		private Executor threadExec ;
		
		/**
		 * The MatchLauncherCommunicationController instance 
		 */
		private MatchLauncherCommunicationController adaptee ;
		
		/**
		 * @throws IOException if it is not possible to retrieve a MatchLauncherCommunicationController instance. 
		 */
		public NetworkCommunicationControllerImpl () throws IOException 
		{
			threadExec = Executors.newSingleThreadExecutor () ;
			adaptee = MatchLauncherCommunicationController.getInstance () ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			threadExec.execute ( adaptee ) ;
		}
		
	}
	
}
