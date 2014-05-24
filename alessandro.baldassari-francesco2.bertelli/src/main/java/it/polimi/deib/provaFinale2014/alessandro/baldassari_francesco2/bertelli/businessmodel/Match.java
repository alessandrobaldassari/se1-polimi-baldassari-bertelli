package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.util.ArrayList;
import java.util.List;

/***/
public class Match 
{

	/***/
	private List < Player > players ;
	
	/***/
	private GameMap gameMap ;
	
	/***/
	private Bank bank ;
	
	/***/
	private boolean inFinalPhase ;
	
	/***/
	private MatchState state;
	
	/***/
	Match ( GameMap gameMap , Bank bank ) 
	{
		if ( gameMap != null && bank != null )
		{
			this.gameMap = gameMap ;
			this.bank = bank ;
			players = new ArrayList < Player > () ;
			state = null;
			
		}
	}
	
	/***/
	public List < Player > getPlayers ()
	{
		return players ;
	}
	
	/***/
	public GameMap getGameMap () 
	{
		return gameMap ;
	}
	
	/***/
	public Bank getBank () 
	{
		return bank ;
	}
	
	/***/
	public boolean isInFinalPhase () 
	{
		return inFinalPhase ;
	}
	
	/***/
	public void setMatchState(MatchState state){
		this.state = state;
	}
	
	/***/
	public MatchState getMatchState(){
		return state;
	}
	
	/***/
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
