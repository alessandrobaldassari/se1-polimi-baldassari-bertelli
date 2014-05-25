package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A core component of the whole system.
 * It represents a Match played by some players ( actually the game context ).
 * It is a passive component: other components ( the controller ) will manipulate
 * its methods to realize the application behavior. 
 */
public class Match 
{

	/**
	 * A list containing the players who are playing this Match. 
	 */
	private List < Player > players ;
	
	/**
	 * The GameMap object associated with this Match. 
	 */
	private GameMap gameMap ;
	
	/**
	 * The Bank object associated with this Match. 
	 */
	private Bank bank ;
	
	/**
	 * A flag indicating if this Match is in its final phase. 
	 */
	private boolean inFinalPhase ;
	
	/**
	 * This represents the actual state of the Match.
	 */
	private MatchState state;
	
	/**
	 * @param gameMap the GameMap object associated to this Match.
	 * @param bank the Bank object associated with this Match. 
	 * @throws IllegalArgumentException if the gameMap or the bank parameter is null.
	 */
	Match ( GameMap gameMap , Bank bank ) 
	{
		if ( gameMap != null && bank != null )
		{
			this.gameMap = gameMap ;
			this.bank = bank ;
			players = new ArrayList < Player > () ;
			state = MatchState.CREATED ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the players property.
	 * 
	 * @return the players property.
	 */
	public List < Player > getPlayers ()
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
	public void setMatchState ( MatchState state ) 
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
	public MatchState getMatchState ()
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
	
}
