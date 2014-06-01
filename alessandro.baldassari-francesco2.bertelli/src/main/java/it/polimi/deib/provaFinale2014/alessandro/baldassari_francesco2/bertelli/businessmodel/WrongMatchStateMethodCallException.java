package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This Exceptions models the situation where a Client tries to invoke a Method of a
 * GameController while the GameController itself is in a state where that method 
 * can not be called. 
 */
public class WrongMatchStateMethodCallException extends WrongStateMethodCallException 
{
	
	/**
	 * The state where the System is when this Exception is thrown. 
	 */
	private MatchState actualState ;
	
	/**
	 * @param actualState the state where the System is when this Exception is thrown. 
	 * @throws IllegalArgumentException if the actualState parameter is null.
	 */
	WrongMatchStateMethodCallException ( MatchState actualState ) 
	{
		if ( actualState != null )
			this.actualState = actualState ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the actualState property.
	 * 
	 * @return the actualState property.
	 */
	public MatchState getActualState () 
	{
		return actualState ;
	}
	
}

