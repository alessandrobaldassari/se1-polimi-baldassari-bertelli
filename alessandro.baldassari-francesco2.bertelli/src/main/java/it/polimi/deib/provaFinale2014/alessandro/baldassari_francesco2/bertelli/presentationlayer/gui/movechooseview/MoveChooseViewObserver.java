package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This class defines the events a MoveChooseViewObserver can listen to.
 */
public interface MoveChooseViewObserver extends Observer
{
	
	/**
	 * Called when the User choosed a move and wants to confirm it. 
	 */
	public void onMoveChoosed ( GameMoveType move ) ;
	
	/**
	 * Undo action. 
	 */
	public void onDoNotWantChooseMove () ;
	
}
