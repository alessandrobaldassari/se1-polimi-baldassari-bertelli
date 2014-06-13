package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This class contains some useful methods that operates over a GameMap and are useful algorithms 
 * and utilities about this object. 
 */
public final class MapUtilities 
{

	/**
	 * This methods check if a Road and a Region in a Map are adjacent.
	 * 
	 * @param road the road that has to be tested for adjacence.
	 * @param target the region that has to be tested for adjacence.
	 * @return true if the road param and the target param are adjacent, false else.
	 */
	public static boolean areAdjacents ( Road road , Region target ) 
	{
		Region border1 ;
		Region border2 ;
		boolean res ;
		border1 = road.getFirstBorderRegion () ;
		border2 = road.getSecondBorderRegion () ;
		res = border1.equals( target ) || border2.equals ( target ) ;
		return res ;
	}
	
	/**
	 * This method finds the only BlackSheep which, at the beginning of the Game is in Sheepsburg.
	 * 
	 * @param gameMap the GameMap object over which operate
	 * @return a reference to the only BlackSheep present in the gameMap parameter
	 * @throws WrongStateMethodCallException if the BlackSheep is not found.
	 */
	public static BlackSheep findBlackSheepAtStart ( GameMap gameMap ) throws WrongStateMethodCallException 
	{
		Iterable < Animal > sheepsburgAnimals ;
		BlackSheep result ;
		Region sheepsburg ;
		sheepsburg = gameMap.getRegionByType ( RegionType.SHEEPSBURG ).iterator().next () ;
		sheepsburgAnimals = sheepsburg.getContainedAnimals () ;
		result = null ;
		for ( Animal a : sheepsburgAnimals )
			if ( a instanceof BlackSheep )
			{
				result = ( BlackSheep ) a ;
				break ;
			}
		if ( result == null )
			throw new WrongStateMethodCallException () ;
		return result ;
	}
	
	/**
	 * This method finds the only Wolf which, at the beginning of the Game is in Sheepsburg.
	 * 
	 * @param gameMap the GameMap over which perform the search.
	 * @return a reference to the only Wolf present in the gameMap parameter
	 * @throws WrongStateMethodCallException if the Wolf is not found 
	 */ 
	public static Wolf findWolfAtStart ( GameMap gameMap ) throws WrongStateMethodCallException 
	{
		Iterable < Animal > sheepsburgAnimals ;
		Wolf result ;
		Region sheepsburg ;
		sheepsburg = gameMap.getRegionByType ( RegionType.SHEEPSBURG ).iterator().next () ;
		sheepsburgAnimals = sheepsburg.getContainedAnimals () ;
		result = null ;
		for ( Animal a : sheepsburgAnimals )
			if ( a instanceof Wolf )
			{
				result = ( Wolf ) a ;
				break ;
			}
		if ( result == null )
			throw new WrongStateMethodCallException () ;
		return result ;
	}
	
}
