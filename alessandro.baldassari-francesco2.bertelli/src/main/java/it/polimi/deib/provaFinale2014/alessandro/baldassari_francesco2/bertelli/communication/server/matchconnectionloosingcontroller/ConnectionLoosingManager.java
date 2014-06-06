package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;

/***/
public interface ConnectionLoosingManager extends Observable < ConnectionLoosingManager.ConnectionLoosingManagerObserver > 
{
	
	/***/
	public boolean manageConnectionLoosing ( Suspendable looser ,  boolean pingAlreadyTested ) ;

	/***/
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
