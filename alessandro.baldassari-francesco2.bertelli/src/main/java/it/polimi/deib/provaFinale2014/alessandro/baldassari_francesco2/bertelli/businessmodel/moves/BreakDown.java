package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.util.Collection;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;

public class BreakDown extends ExecutableGameMove
{

	private static final int MINIMUM_POINTS_TO_BE_PAYED = 5 ;
	private static final int AMOUNT_TO_PAY_FOR_SILENCE = 2 ;
	private Sheperd breaker ;
	private Animal animalToBreak ;
	
	BreakDown ( Sheperd breaker , Animal animalToBreak ) 
	{
		if ( breaker != null )
		{
			this.breaker = breaker ;
			this.animalToBreak = animalToBreak ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException 
	{
		Collection < Player > adjacentPlayers ;
		adjacentPlayers = new LinkedList < Player > () ;
		for ( Road road : breaker.getPosition().getAdjacentRoads () )
			if ( road.getElementContained () != null && road.getElementContained () instanceof Sheperd )
				adjacentPlayers.add ( ( ( Sheperd ) road.getElementContained () ).getOwner() ) ;
		for ( Player player : adjacentPlayers )
			if ( player.launchDice () < MINIMUM_POINTS_TO_BE_PAYED )
				adjacentPlayers.remove ( player ) ;
		try 
		{
			breaker.getOwner().pay ( adjacentPlayers.size () * AMOUNT_TO_PAY_FOR_SILENCE ) ;
			for ( Player player : adjacentPlayers )
				player.receiveMoney ( AMOUNT_TO_PAY_FOR_SILENCE );
			animalToBreak.getPosition ().getContainedAnimals ().remove ( animalToBreak ) ;
			animalToBreak.moveTo ( null ) ;
		} 
		catch ( TooFewMoneyException e ) 
		{
			e.printStackTrace();
			throw new MoveNotAllowedException () ;
		}
	}

}
