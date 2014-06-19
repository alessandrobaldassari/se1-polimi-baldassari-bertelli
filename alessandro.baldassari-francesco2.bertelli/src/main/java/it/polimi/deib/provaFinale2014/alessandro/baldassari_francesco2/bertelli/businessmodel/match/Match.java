package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A core component of the whole system.
 * It represents a Match played by some players ( actually the game context ).
 * It is a passive component: other components ( the controller ) will manipulate
 * its methods to realize the application behavior. 
 */
public class Match implements Serializable
{

	/**
	 * A list containing the players who are playing this Match. 
	 */
	private List < Player > players ;
	
	/**
	 * The GameMap object associated with this Match. 
	 */
	private final GameMap gameMap ;
	
	/**
	 * The Bank object associated with this Match. 
	 */
	private final Bank bank ;
	
	/**
	 * This represents the actual state of the Match.
	 */
	private MatchState state;
	
	/**
	 * A flag indicating if this Match is in its final phase. 
	 */
	private boolean inFinalPhase ;
	
	/**
	 * @param gameMap the GameMap object associated to this Match.
	 * @param bank the Bank object associated with this Match. 
	 * @throws IllegalArgumentException if the gameMap or the bank parameter is null.
	 */
	public Match ( GameMap gameMap , Bank bank ) 
	{
		if ( gameMap != null && bank != null )
		{
			this.gameMap = gameMap ;
			this.bank = bank ;
			players = new Vector < Player > () ;
			state = MatchState.CREATED ;
			inFinalPhase = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Add a new Player to this Match.
	 * 
	 * @param newPlayer the new Player to add.
	 * @throws WrongMatchStateMethodCallException if this method is called while this Match object is
	 *         not in the WAIT_FOR_PLAYERS state.
	 * @throws IllegalArgumentException if the newPlayer parameter is null.
	 */
	public void addPlayer ( Player newPlayer ) throws WrongMatchStateMethodCallException 
	{
		if ( state == MatchState.WAIT_FOR_PLAYERS )
			if ( newPlayer != null )
				players.add ( newPlayer ) ;
			else
				throw new IllegalArgumentException () ;
		else
			throw new WrongMatchStateMethodCallException ( state ) ; 
	}
	
	/**
	 * This method simulates the phase of the Game where the Players' order is decided.
	 * This method implements this functionality as a randomic list mesh of the Players's list. 
	 * 
	 * @throws WrongMatchStateMethodCallException if this method is not called while the state attribute is
	 *         equals to INITIALIZATION.
	 */
	public void setPlayerOrder ( Map < Player , Integer > playersOrdering ) throws WrongMatchStateMethodCallException 
	{
		if ( state == MatchState.INITIALIZATION ) 
		{
			if ( playersOrdering.keySet ().containsAll ( players ) && players.containsAll ( playersOrdering.keySet() ) )
			{
				players = new ArrayList < Player > ( playersOrdering.keySet() ) ;
				//players.clear () ;
				//for ( Player p : playersOrdering.keySet () )
					//players.add ( playersOrdering.get ( p ) , p ) ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		else
			throw new WrongMatchStateMethodCallException ( state ) ;
	}
	
	/**
	 * Getter for the number of Player of this Match.
	 * 
	 * @return the number of Player of this Match.
	 */
	public int getNumberOfPlayers () 
	{
		return players.size () ;
	}
	
	/**
	 * Remove a Player from this Match.
	 * 
	 * @param playerToRemove the player to remove from this Match.
	 */
	void removePlayer ( Player playerToRemove ) 
	{
		players.remove ( playerToRemove ) ;
	}
	
	/**
	 * Getter for the players property.
	 * 
	 * @return the players property.
	 */
	public Iterable < Player > getPlayers ()
	{
		return players ;
	}
	
	/**
	 * Getter for the gameMap property.
	 * 
	 * @return the gameMap property.
	 */
	public GameMap getGameMap () 
	{
		return gameMap ;
	}
	
	/**
	 * Getter for the bank property.
	 * 
	 * @return the bank property.
	 */
	public Bank getBank () 
	{
		return bank ;
	}
	
	/**
	 * Partial setter method for the inFinalPhase attribute.
	 * The logical model requires that this method is called once during
	 * the Match lifecycle.
	 * It has to be called by an external component that realizes the game is
	 * entering in its final phase. 
	 * 
	 * @throws AlreadyInFinalPhaseException if the inFinalPhase attribute value is true.
	 */
	void enterFinalPhase () throws AlreadyInFinalPhaseException 
	{
		if ( inFinalPhase == false )
			inFinalPhase = true ;
		else 
			throw new AlreadyInFinalPhaseException () ;
	}
	
	/**
	 * Setter method for the inFinalPhase property.
	 * 
	 * @return the inFinalPhase property.
	 */
	public boolean isInFinalPhase () 
	{
		return inFinalPhase ;
	}
	
	/**
	 * Setter for the matchState property 
	 *
	 * @param state the new value for the MatchState property
	 * @throws IllegalArgumentException if the parameter is null
	 */
	void setMatchState ( MatchState state ) 
	{
		if ( state != null )
			this.state = state;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the state property.
	 * 
	 * @return the state property.
	 */
	public synchronized MatchState getMatchState ()
	{
		return state ;
	}
	
	// ENUMS
	
	/**
	 * This enum describe all the possible states where a Match can be.
	 * 1. CREATED
	 * 2. WAIT_FOR_PLAYERS
	 * 3. INITIALIZATION
	 * 4. TURNATION
	 * 5. SUSPENDED
	 * 6. CALCULATING_RESULTS  
	 */
	public enum MatchState 
	{
		
		CREATED ,
		
		WAIT_FOR_PLAYERS ,
		
		INITIALIZATION ,
		
		TURNATION ,
		
		SUSPENDED ,
		
		CALCULATING_RESULTS
		
	}

	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This class models the situation where someone try to set this Match in the final
	 * state also if it already is. 
	 */
	public class AlreadyInFinalPhaseException extends Exception {}
	
}
