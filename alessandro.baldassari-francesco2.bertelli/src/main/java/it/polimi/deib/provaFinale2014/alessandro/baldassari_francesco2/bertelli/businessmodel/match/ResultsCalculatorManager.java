package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
* Component that calculate the results of a Match 
*/
class ResultsCalculatorManager 
{

	// ATTRIBUTES
	
	/**
	 * The match where operate. 
	 */
	private Match match ;
	
	/**
	 * A Map containing the points that every regions has. 
	 */
	private Map <RegionType, Integer> regionValuesMap;
	
	// METHODS
	
	/**
	 * @param match the match where operate.
	 * @throws IllegalArgumentException if the match parameter is null.
	 * @throws WrongMatchStateMethodCallException if the match parameter's state is not CALCULATING_RESULTS. 
	 */
	protected ResultsCalculatorManager ( Match match ) throws WrongMatchStateMethodCallException
	{
		if ( match != null )
			if ( match.getMatchState() == MatchState.CALCULATING_RESULTS )
			{
				this.match = match ;
				regionValuesMap = new HashMap < RegionType , Integer > ( RegionType.values ().length - 1 ) ;
			}
			else
				throw new WrongMatchStateMethodCallException ( match.getMatchState () ) ;
		else
			throw new IllegalArgumentException () ;
		
	}

	/**
	 * Helper method to calculate the points for every region. 
	 */
	private void calculateRegionsResults () 
	{
		for ( RegionType r : RegionType.values () )
			if ( r != RegionType.SHEEPSBURG )
				regionValuesMap.put ( r , calculateRegionValue ( r ) ) ;
	}
	
	/**
	 * This method calculate the value of the RegionType passed by parameter.
	 * The procedure used to determine this value is the one specified by business rules.
	 * 
	 * @param rt the RegionType about that calculate the value.
	 * @return the value of the RegionType passed by parameter.
	 */
	private int calculateRegionValue ( RegionType rt )
	{
		Iterable<Region> regions;
		Iterable<Animal> animals;
		int amount;
		amount = 0;
		regions = match.getGameMap().getRegionByType ( rt ) ;
		for(Region r : regions)
		{
			animals = r.getContainedAnimals();
			for ( Animal a : animals )
				if ( ! ( a instanceof Wolf ) )
				{
					if ( a instanceof BlackSheep )
						amount = amount + 2;
					else 
						amount = amount + 1;
				}
		}
		return amount ;	
	}
	
	/**
	 * Calculate the score of the Player passed by parameter and returns it.
	 * 
	 * @param player the Player on which calculate the score.
	 * @return the score of the Player passed by parameter.
	 * @throws {@link WorkflowException} if an unexpected error occurs.
	 */
	public int calculatePlayerScore ( Player player ) throws WorkflowException
	{
		int res;
		Collection <Card> playerCards;
		if ( regionValuesMap.isEmpty () )
			calculateRegionsResults () ;
		playerCards = new ArrayList<Card> () ;
		for ( SellableCard s : player.getSellableCards () )
			playerCards.add ( s ) ;
		playerCards.add(player.getInitialCard());
		res = 0;
		for(Card card : playerCards)
			res = res + regionValuesMap.get(card.getRegionType());
		try {
			res = res + player.getMoney();
		} catch (WrongStateMethodCallException e) {
			throw new WorkflowException();
		}
		return res;	
	}
	
}