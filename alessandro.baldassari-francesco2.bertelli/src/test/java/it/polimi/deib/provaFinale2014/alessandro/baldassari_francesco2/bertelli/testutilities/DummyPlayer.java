package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

public class DummyPlayer extends Player 
{

	public DummyPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public int diceRes =6 ;

	@Override
	public int launchDice () 
	{
		return diceRes ;
	}

	@Override
	public void chooseCardsEligibleForSelling() throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Sheperd chooseSheperdForATurn() throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SellableCard chooseCardToBuy(Iterable<SellableCard> src)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameMove doMove(MoveFactory moveFactory, GameMap gameMap)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedColor getColorForSheperd(Iterable<NamedColor> availableColors)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Road chooseInitialRoadForASheperd(Iterable<Road> availableRoads)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genericNotification(String message) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}


