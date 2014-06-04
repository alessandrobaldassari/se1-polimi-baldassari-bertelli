package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;

/**
 * An instance of this class represents a Move that a Player can do during the Game. 
 */
public abstract class GameMove implements Serializable
{
	
	/***/
	protected GameMove () {}
	
	/**
	 * A method that will effectively execute the Move represented by this object on the match passed by parameter.
	 * 
	 * @param match the Match where this move will operate.
	 * @throws MoveNotAllowedException if the parameters supplied to this object are not correct and this move
	 *         can not be executed.
	 */
	public abstract void execute ( Match match ) throws MoveNotAllowedException ;
	
	/**
	 * This Exception class models the situation where a GameMove can not be executed for some reasons. 
	 */
	public class MoveNotAllowedException extends Exception 
	{
		
		/***/
		protected MoveNotAllowedException () 
		{
			super () ;
		}
		
	}
	
}
