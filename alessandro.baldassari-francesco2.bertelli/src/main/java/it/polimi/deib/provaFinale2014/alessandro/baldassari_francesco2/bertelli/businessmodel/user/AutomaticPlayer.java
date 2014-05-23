package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;

public class AutomaticPlayer extends Player {
	AutomaticPlayer(String name, int money){
		super(name, money);
	}
	
	public AutomaticPlayer newInstance ( String name , int money ) 
	{
		AutomaticPlayer playerHuman;
		return playerHuman = new AutomaticPlayer(name, money);
	}

	@Override
	public void chooseCardsEligibleForSelling() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameMove doMove(MoveFactory moveFactory, GameMap gameMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
