package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

/**
 * This interface defines a Component that is a Buffer of suspended ClientHandlers 
 */
public interface SuspendedClientHandlerBuffer 
{

	/**
	 * @param
	 * @return  
	 */
	public ClientHandler < ? > getClientHandler ( Integer clientHandlerUID ) ;
	
}
