package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * An interface that one must implement if he wants to be a Listener for a ConnectionLoosingManager. 
 */
public interface ConnectionLoosingManagerObserver extends Observer 
{

	/***/
	public void onBeginSuspensionControl ( Suspendable pendant ) ;
	
	/***/
	public void onEndSuspensionControl ( Boolean suspendedRetrieved ) ;
	
	/***/
	public void onConnectionRetrieved ( Suspendable retrievedElem ) ;
	
}