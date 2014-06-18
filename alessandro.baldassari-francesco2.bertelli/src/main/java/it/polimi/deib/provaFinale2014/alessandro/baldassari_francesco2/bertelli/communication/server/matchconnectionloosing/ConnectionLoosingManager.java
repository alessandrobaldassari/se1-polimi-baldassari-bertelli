package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;

/**
 * This component represents one that can be invoked to manage a Client when it seems he loosed connection.
 * This Manager try to retrieve the connection and notify its listeners about this problem. 
 */
public interface ConnectionLoosingManager extends Observable < ConnectionLoosingManagerObserver > 
{
	
	/**
	 * The method that has to be invoked to manage a component that is loosing connection.
	 * 
	 * @param looser the component that is supposed to loose connectivity.
	 * @param pingAlreadyTested true if the caller has already test the connectivity of the looser param.
	 * @return true if this method re-establish the connection with the looser param, false else.
	 */
	public boolean manageConnectionLoosing ( Suspendable looser , ClientHandler < ? > clientHandler , boolean pingAlreadyTested ) ;
		
}

