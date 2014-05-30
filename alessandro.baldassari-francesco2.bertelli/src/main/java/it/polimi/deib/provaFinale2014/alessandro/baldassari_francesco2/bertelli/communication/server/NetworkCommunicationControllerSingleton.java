package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;

public class NetworkCommunicationControllerSingleton 
{

	/**
	 * A static reference to the only living instance of this class.
	 * Needed to implement the Singleton pattern. 
	 */
	private static MasterServer instance ;

	/**
	 * Singleton method for this class.
	 * 
	 * @return the only instance of this class.  
	 * @throws IOException if something goes wrong with the creation of the only living instance of this class.
	 */
	public static NetworkCommunicationController getInstance () throws IOException 
	{
		if ( instance == null )
			instance = new MasterServer () ;
		return instance ;	
	}
	
}
