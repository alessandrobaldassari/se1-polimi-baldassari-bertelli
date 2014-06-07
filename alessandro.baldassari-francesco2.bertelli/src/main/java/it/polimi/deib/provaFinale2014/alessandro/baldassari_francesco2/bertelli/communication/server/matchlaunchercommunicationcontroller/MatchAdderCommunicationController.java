package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

/**
 * This interface describes a component that accept request to add Players in a Match. 
 */
public interface MatchAdderCommunicationController 
{

	/**
	 * This method is called by a technical server to notify that a Player contacted him
	 * and asked him to add a Player to a Match.
	 * It asks the clientHandler that the technical server passed to it to request the user
	 * his name; if everything goes well, then add the player 
	 * 
	 * @param newClientHandler the ClientHandler to add.
	 */
	public abstract void addPlayer ( ClientHandler < ? > newClientHandler ) ; 
	
}
