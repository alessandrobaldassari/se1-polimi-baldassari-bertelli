package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

public interface SuspendedClientHandlerBuffer 
{

	public ClientHandler < ? > getClientHandler ( Integer clientHandlerUID ) ;
	
}
