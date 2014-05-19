package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.util.List;

public class Match 
{

	private List < Player > players ;
	private GameMap gameMap ;
	private Bank bank ;
	private boolean inFinalPhase ;
	
	Match () {}
	
	public List < Player > getPlayers ()
	{
		return players ;
	}
	
	public GameMap getGameMap () 
	{
		return gameMap ;
	}
	
	public Bank getBank () 
	{
		return bank ;
	}
	
	public boolean isInFinalPhase () 
	{
		return inFinalPhase ;
	}
	
}
