package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

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
	
	public static boolean areAdjacents ( Road r1 , Road r2 ) 
	{
		boolean res ;
		if ( r1 != null && r2 != null )
			res = CollectionsUtilities.contains( r1.getAdjacentRoads() , r2 ) ;
		else
			throw new IllegalArgumentException();
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
	
	/**
	 * Extract from the src Collection the Animals which are AdultOvines
	 * 
	 * @param src the Collection where to perform the search.
	 * @return a List containing all of the AdultOvines which are in the src parameter.
	 */
	public static List < AdultOvine > extractAdultOvinesExceptBlackSheep ( Iterable < Animal > src ) 
	{
		List < AdultOvine > res ;
		res = new LinkedList < AdultOvine > () ;
		for ( Animal animal : src )
			if ( animal instanceof AdultOvine && ! ( animal instanceof BlackSheep ) )
				res.add ( ( AdultOvine ) animal ) ;
		return res ;
	}
	
	/**
	 * Search for an AdultOvine of the parameter specified type in the src List, and if 
	 * it exists, return it.
	 * 
	 * @param src the List where to perform the search.
	 * @param type the type the returned AdultOvine must be.
	 * @return the target AdultOvine, null if it can not exists in the src List.
	 */
	public static AdultOvine lookForAnOvine ( List < AdultOvine > src , AdultOvineType type ) 
	{
		AdultOvine res ;
		int i ;
		res = null ;
		i = 0 ;
		while ( i < src.size() && src.get ( i ).getType () != type ) 
		i ++ ;
		if ( i < src.size () )
			res = src.get ( i ) ;
		return res ;
	}

	
	public static int ovineCount ( Region region ) 
	{
		int res ;
		res = 0 ;
		for ( Animal a : region.getContainedAnimals () )
			if ( Ovine.class.isAssignableFrom ( a.getClass () ) )
				res ++ ;
		return res ;
	}
	
	public static Region getOtherAdjacentDifferentFrom ( Road r , Region not ) 
	{
		Region res ;
		if ( r != null )
			if ( r.getFirstBorderRegion ().equals ( not ) )
				res = r.getSecondBorderRegion();
			else
				res = r.getFirstBorderRegion();
		else
			throw new IllegalArgumentException();
		return res ;
	}
	
	/***/
	public static Animal findAnimalByUID ( Region location , int uid )
	{
		Animal res ;
		res = null ;
		for ( Animal a : location.getContainedAnimals () )
			if ( a.getUID () == uid )
			{
				res = a ;
				break ;
			}
		return res ;
	}
	
	/***/
	public static Identifiable lookForIdentifier ( Iterable < ? extends Identifiable > src, int key ) 
	{
		Identifiable res ;
		res = null ;
		for ( Identifiable i : src )
			if ( i.getUID() == key )
			{
				res = i ;
				break ;
			}
		return res ;
	}
	
	/***/
	public static Animal lookForAType ( Iterable < Animal > src , PositionableElementType p )
	{
		Animal res ;
		res = null ;
		for ( Animal a : src )
			if ( a.getPositionableElementType() == p )
			{
				res = a ;
				break ;
			}
		return res ;
	}
	
	/***/
	public static Set < PositionableElementType > generateAllowedSet ( Iterable < Animal > toRemove ) 
	{
		Set < PositionableElementType > res ;
		res = new LinkedHashSet<PositionableElementType>();
		res.add ( PositionableElementType.RAM ) ;
		res.add ( PositionableElementType.SHEEP ) ;
		res.add ( PositionableElementType.LAMB ) ;
		for ( Animal a : toRemove )
			if ( res.contains ( a.getPositionableElementType() ) )
				res.remove ( a.getPositionableElementType() ) ;
		return res ;
	}
	
}
