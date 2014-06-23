package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import java.util.Collection;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

/**
 * This class contains some useful methods that operates over a GameMap and are useful algorithms 
 * and utilities about this object. 
 */
public final class MapUtilities 
{

	// METHODS
	
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
	 * Tests if the two Roads passed by parameter are adjacent ( in a graph sense ).
	 * 
	 * @param r1 the first Road.
	 * @param r2 the second Road.
	 * @return true if r1 adjacent r2, false else.
	 * @throws {@link IllegalArgumentException} if r1 or r2 are null.
	 */
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
	 * @throws {@link IllegalArgumentException} if the gameMap parameter is null.
	 */
	public static BlackSheep findBlackSheepAtStart ( GameMap gameMap ) throws WrongStateMethodCallException 
	{
		Iterable < Animal > sheepsburgAnimals ;
		BlackSheep result ;
		Region sheepsburg ;
		if ( gameMap != null )
		{
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
		}
		else
			throw new IllegalArgumentException () ;
		return result ;
	}
	
	/**
	 * This method finds the only Wolf which, at the beginning of the Game is in Sheepsburg.
	 * 
	 * @param gameMap the GameMap over which perform the search.
	 * @return a reference to the only Wolf present in the gameMap parameter
	 * @throws WrongStateMethodCallException if the Wolf is not found 
	 * @throws {@link IllegalArgumentException} if the gameMap parameters is null.
	 */ 
	public static Wolf findWolfAtStart ( GameMap gameMap ) throws WrongStateMethodCallException 
	{
		Iterable < Animal > sheepsburgAnimals ;
		Wolf result ;
		Region sheepsburg ;
		if ( gameMap != null )
		{
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
			}
		else
			throw new IllegalArgumentException () ;
		return result ;
	}
	
	/**
	 * Extract from the src Collection the Animals which are AdultOvines
	 * 
	 * @param src the Collection where to perform the search.
	 * @return a List containing all of the AdultOvines which are in the src parameter.
	 * @throws  {@link IllegalArgumentException } if the src parameters is null.
	 */
	public static List < AdultOvine > extractAdultOvinesExceptBlackSheep ( Iterable < Animal > src ) 
	{
		List < AdultOvine > res ;
		if ( src != null )
		{
			res = new LinkedList < AdultOvine > () ;
			for ( Animal animal : src )
				if ( animal instanceof AdultOvine && ! ( animal instanceof BlackSheep ) )
					res.add ( ( AdultOvine ) animal ) ;
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Extract from the src Iterable, all the objects wich are Ovines but not BlackSheeps.
	 * 
	 * @param src the data structure to query.
	 * @return all the object which are in the src parameters and are Ovines but not BlackSheeps.
	 * @throws {@link IllegalArgumentException} if the src parameters is null.
	 */
	public static Collection < Ovine > extractOvinesExceptBlackSheep ( Iterable < Animal > src )
	{
		Collection < Ovine > res ;
		if ( src != null )
		{
			res = new LinkedList < Ovine > () ;
			for ( Animal a : src )
				if ( Ovine.class.isAssignableFrom ( a.getClass() ) && ! ( a instanceof BlackSheep ) )
					res.add((Ovine) a);
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Search for an AdultOvine of the parameter specified type in the src List, and if 
	 * it exists, return it.
	 * 
	 * @param src the List where to perform the search.
	 * @param type the type the returned AdultOvine must be.
	 * @return the target AdultOvine, null if it can not exists in the src List.
	 * @throws {@link IllegalArgumentException} if the src or the type parameters is null.
	 */
	public static AdultOvine lookForAnOvine ( List < AdultOvine > src , AdultOvineType type ) 
	{
		AdultOvine res ;
		int i ;
		res = null ;
		i = 0 ;
		if ( src != null && type != null )
		{
			while ( i < src.size() && src.get ( i ).getType () != type ) 
			i ++ ;
			if ( i < src.size () )
				res = src.get ( i ) ;
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}

	/**
	 * Count the Ovine instances in the region parameter.
	 * 
	 * @param region the Region to query.
	 * @return the number of Ovines in the region parameter.
	 * @throws {@link IllegalArgumentException} if the region parameter is null
	 */
	public static int ovineCount ( Region region ) 
	{
		int res ;
		if ( region != null )
		{
			res = 0 ;
			for ( Animal a : region.getContainedAnimals () )
				if ( Ovine.class.isAssignableFrom ( a.getClass () ) )
					res ++ ;
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Returns the Region which is adjacent to the Road r, but is not equals to the Region not.
	 * 
	 * @param r the Road object.
	 * @param not the Region object
	 * @return the Region which is adjacent to the Road r, but is not equals to the Region not.
	 * @throws {@link IllegalArgumentException} if the r or the not parameter is null or if the not Region
	 * 	       is not adjacent to the r Road.
	 */
	public static Region getOtherAdjacentDifferentFrom ( Road r , Region not ) 
	{
		Region res ;
		if ( r != null && not != null )
			if ( r.getFirstBorderRegion ().equals ( not ) )
				res = r.getSecondBorderRegion();
			else
				if ( r.getSecondBorderRegion().equals(not) )
					res = r.getFirstBorderRegion();
				else
					throw new IllegalArgumentException () ;
		else
			throw new IllegalArgumentException();
		return res ;
	}
	
	/**
	 * Find an Animal in the location parameters given the Animal uid.
	 * 
	 * @param location the Region where the Animal is.
	 * @param uid the uid of the animal to search.
	 * @return the animal which is in the location Region and has the uid parameter uid, null if not found.
	 * @throws IllegalArgumentException if the location parameters is null
	 */
	public static Animal findAnimalByUID ( Region location , int uid )
	{
		Animal res ;
		if ( location != null )
		{
			res = null ;
			for ( Animal a : location.getContainedAnimals () )
				if ( a.getUID () == uid )
				{
					res = a ;
					break ;
				}
			}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	
	
	/**
	 * Find an Animal, in the src Iterable whose PositionableElementType is p.
	 * 
	 * @param src the data structure where search.
	 * @param p the PositionableElementType of the Animal to find.
	 * @return an Animal, in the src Iterable whose PositionableElementType is p, null if not found.
	 * @throws {@link IllegalArgumentException} if the src or the p parameters is not null.
	 */
	public static Animal lookForAType ( Iterable < Animal > src , PositionableElementType p )
	{
		Animal res ;
		if ( src != null && p != null )
		{
			res = null ;
			for ( Animal a : src )
				if ( a.getPositionableElementType() == p )
				{
					res = a ;
					break ;
				}
			}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Retrieve all Players near adjacent to the breaker one.
	 * 
	 * @return a Collection containing all the Players adjacent to the breaker one.
	 * @throws {@link IllegalArgumentException} if the playerPosition is null
	 */
	public static Collection < Player > retrieveAdjacentPlayers ( Road playerPosition ) 
	{
		Collection < Player > adjacentPlayers ;
		if ( playerPosition != null )
		{
			adjacentPlayers = new LinkedList < Player > () ;
			for ( Road road :playerPosition.getAdjacentRoads () )
				if ( road.getElementContained () != null && road.getElementContained () instanceof Sheperd )
					adjacentPlayers.add ( ( ( Sheperd ) road.getElementContained () ).getOwner() ) ;
			}
		else
			throw new IllegalArgumentException () ;
		return adjacentPlayers ;
	}
	
	/**
	 * Constructs a Set that contains the Ovine types except the types of the Animals in the toRemove parameters.
	 * 
	 * @param toRemove the Animals whose type has to be removed from the result.
	 * @return  a Set that contains the Ovine types except the types of the Animals in the toRemove parameters
	 * @throws {@link IllegalArgumentException} if the toRemove parameter is null 
	 */
	public static Set < PositionableElementType > generateAllowedSet ( Iterable < Animal > toRemove ) 
	{
		Set < PositionableElementType > res ;
		if ( toRemove != null )
		{
			res = new LinkedHashSet<PositionableElementType>();
			res.add ( PositionableElementType.RAM ) ;
			res.add ( PositionableElementType.SHEEP ) ;
			res.add ( PositionableElementType.LAMB ) ;
			for ( Animal a : toRemove )
				if ( res.contains ( a.getPositionableElementType() ) )
					res.remove ( a.getPositionableElementType() ) ;
		}
		else
			throw new IllegalArgumentException();
		return res ;
	}
	
}
