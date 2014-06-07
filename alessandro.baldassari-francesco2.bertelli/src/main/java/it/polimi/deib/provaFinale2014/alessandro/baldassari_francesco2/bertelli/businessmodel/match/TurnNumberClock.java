package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;

/**
 * This interface defines a component that manages the turn number succession in a Match. 
 */
public interface TurnNumberClock 
{

	/**
	 * Getter method for the turnNumber property.
	 * 
	 * @return the turnNumber property.
	 * @throws WrongMatchStateMethodCallException if this method is not called during the TURNATION phase.
	 */
	public int getTurnNumber () throws WrongMatchStateMethodCallException ;
	
}
