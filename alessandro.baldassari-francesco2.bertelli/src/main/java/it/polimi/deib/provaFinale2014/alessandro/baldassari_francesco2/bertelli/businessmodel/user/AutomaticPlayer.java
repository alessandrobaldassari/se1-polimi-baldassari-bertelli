package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

public class AutomaticPlayer extends Player 
{

	public AutomaticPlayer ( String name ) 
	{
		super(name);
	}

	@Override
	public void chooseCardsEligibleForSellingImpl () {}

	@Override
	public Sheperd chooseSheperdForATurnImpl () 
	{
		return null;
	}

	@Override
	public SellableCard chooseCardToBuyImpl (Iterable<SellableCard> src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameMove doMove(MoveFactory moveFactory, GameMap gameMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedColor getColorForSheperd(Iterable<NamedColor> availableColors) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Road chooseInitialRegionForASheperd(Iterable<Road> availableRoads) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genericNotification(String message) {
		// TODO Auto-generated method stub
		
	}

}
