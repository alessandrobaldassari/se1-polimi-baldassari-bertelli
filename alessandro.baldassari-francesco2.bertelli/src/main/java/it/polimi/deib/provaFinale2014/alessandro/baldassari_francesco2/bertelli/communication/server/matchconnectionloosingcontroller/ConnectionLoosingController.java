package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This component represents one that can be invoked to manage a Client when it seems he loosed connection.
 * This Manager try to retrieve the connection and notify its listeners about this problem. 
 */
public interface ConnectionLoosingController extends Observable < ConnectionLoosingController.ConnectionLoosingManagerObserver > 
{
	
	/***/
	public static final long WAITING_TIME = 60 * Utilities.MILLISECONDS_PER_SECOND ;

	/***/
	public static final long SUSPENSION_TIME = 60 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/**
	 * The method that has to be invoked to manage a component that is loosing connection.
	 * 
	 * @param looser the component that is supposed to loose connectivity.
	 * @param pingAlreadyTested true if the caller has already test the connectivity of the looser param.
	 * @return true if this method re-establish the connection with the looser param, false else.
	 */
	public boolean manageConnectionLoosing ( Suspendable looser , ClientHandler < ? > clientHandler , boolean pingAlreadyTested ) ;

	/***/
	ClientHandler < ? > getClientHandler ( int clientHandlerUID ) ;
	
	/**
	 * An interface that one must implement if he wants to be a Listener for a ConnectionLoosingManager. 
	 */
	public interface ConnectionLoosingManagerObserver extends Observer 
	{
	
		/***/
		public void onBeginSuspensionControl ( Suspendable pendant ) ;
		
		/***/
		public void onEndSuspensionControl ( boolean suspendedRetrieved ) ;
		
		/***/
		public void onConnectionRetrieved ( Suspendable retrievedElem ) ;
		
	}
		
}

