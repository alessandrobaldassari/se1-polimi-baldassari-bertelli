package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.awt.Color;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

public class AutomaticPlayer extends Player {
	AutomaticPlayer(String name){
		super(name);
	}
	
	public AutomaticPlayer newInstance ( String name ) 
	{
		AutomaticPlayer playerHuman;
		return playerHuman = new AutomaticPlayer(name);
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

	@Override
	public Sheperd chooseSheperdForATurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SellableCard chooseCardToBuy(Iterable<SellableCard> src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColorForSheperd(Iterable<Color> availableColors) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genericNotification(String message) {
		// TODO Auto-generated method stub
		
	}

}
