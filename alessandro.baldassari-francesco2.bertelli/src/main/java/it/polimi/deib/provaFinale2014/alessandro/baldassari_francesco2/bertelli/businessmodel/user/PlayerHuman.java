package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;

public class PlayerHuman extends Player {
	PlayerHuman(String name, int money){
		super(name, money);
	}
	
	public PlayerHuman newInstance(String name, int money){
		PlayerHuman playerHuman;
		return playerHuman = new PlayerHuman(name, money);
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
