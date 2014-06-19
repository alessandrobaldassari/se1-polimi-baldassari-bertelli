package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * An instance of this class represents a Move that a Player can do during the Game. 
 * The security approach the guarentee business rules is that a Move that can not be executed can
 * not be created ( not just executed ).
 * So, controls, are done in the constructor, not in the execute method.
 * Obviously is crucial that a Game environment does not change after the constructor execution and before
 * the execute execution.
 * To do so a simple method is to call the constructor and the the execute method in a chain, possibly
 * in a synchronized block over the match upon which the move will be executed.
 */
public abstract class GameMove 
{
	
	/***/
	protected GameMove () {}
	
	/**
	 * A method that will effectively execute the Move represented by this object on the match passed by parameter.
	 * 
	 * @param match the Match where this move will operate.
	 * @throws MoveNotAllowedException if the parameters supplied to this object are not correct and this move
	 *         can not be executed.
	 * @throws WorkflowException 
	 */
	public abstract void execute ( Match match ) throws MoveNotAllowedException , WrongStateMethodCallException, WorkflowException ;
		
}
