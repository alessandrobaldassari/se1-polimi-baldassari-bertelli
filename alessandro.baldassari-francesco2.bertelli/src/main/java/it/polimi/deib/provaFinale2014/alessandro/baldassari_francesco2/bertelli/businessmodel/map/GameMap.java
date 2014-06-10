package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement.GameMapElementObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElement.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WithReflectionObservableSupport;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class is a passive data structure that represents the GameMap ( as the name says ).
 * It hides the bipartite graph structure of the real map, and offer to its clients only the methods
 * they need to interact with it.
 * The object is immutable, also if the regions and roads contained here are not.
 */
public class GameMap implements Serializable , Observable  < GameMap.GameMapObserver > , GameMapElementObserver
{

	// ATTRIBUTES
	
	/**
	 * The number of Region in a Map. 
	 */
	public static final int NUMBER_OF_REGIONS = 19 ;
	
	/**
	 * The number of Road in a Map 
	 */
	public static final int NUMBER_OF_ROADS = 42 ;
	
	/**
	 * The map of all the regions that compose this map with the UID as the key and the Region object as the value.
	 */
	private final Map < Integer , Region > regions ;
	
	/**
	 * The map of all the roads that compose this map with the UID as the key and the Road object as the value.
	 */
	private final Map < Integer , Road > roads ;
	
	/**
	 * An object to easily implement the Observer pattern. 
	 */
	private WithReflectionObservableSupport < GameMapObserver > support ;
	
	// METHODS
	
	/**
	 * Constructs a GameMap object using two InputStreams where raw data about regions, roads and their 
	 * relationship are stored. 
	 * Assumes that these streams are already open and that the contents they point to are the right ones
	 * 
	 * @param regionsCSVInputStream the InputStream where take the raw data about the regions
	 * @param roadsCSVInputStream the InputStream where take the raw data about the roads
	 */
	protected GameMap ( Map < Integer , Couple < Region , int [] > > regionsMap , Map < Integer , Couple < Road , int [] > > roadsMap ) 
	{
		Collection < Road > borderRoads ;
		Collection < Road > adjacentRoads ;
		Region region ;
		Road road ;
		regions = new HashMap < Integer , Region > () ;
		roads = new HashMap < Integer , Road > () ;
		support = new WithReflectionObservableSupport < GameMapObserver > () ;
		for ( Couple < Region , int [] > couple : regionsMap.values() )
		{
			region = couple.getFirstObject () ;
			regions.put ( region.getUID() , region ) ;
			region.addObserver ( this ) ;
			borderRoads = new ArrayList < Road > ( couple.getSecondObject ().length ) ;
			for ( Integer roadUID : couple.getSecondObject () )
				borderRoads.add ( roadsMap.get ( roadUID ).getFirstObject() ) ;
			region.setBorderRoads ( borderRoads ) ;
		}
		for ( Couple < Road , int [] > couple : roadsMap.values () )
		{
			road = couple.getFirstObject () ;
			roads.put ( road.getUID () , road ) ;
			road.addObserver ( this ) ;
			adjacentRoads = new ArrayList < Road > ( couple.getSecondObject().length ) ;
			for ( Integer roadUID : couple.getSecondObject () )
				adjacentRoads.add ( roadsMap.get ( roadUID ).getFirstObject() ) ;
			road.setAdjacentRoads ( adjacentRoads ) ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver( GameMapObserver newObserver ) 
	{
		support.addObserver ( newObserver ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void removeObserver ( GameMapObserver oldObserver ) 
	{
		support.removeObserver ( oldObserver ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onElementAdded ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
	{
		try {
			support.notifyObservers ( "onPositionableElementAdded" , whereType , whereId , whoType , whoId );
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onElementRemoved ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
	{
		try {
			support.notifyObservers ( "onPositionableElementRemoved" , whereType , whereId , whoType , whoId );
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an Iterable containing all the Region objects stored in this GameMap.
	 * 
	 * @return an Iterable containing all the Region objects stored in this GameMap.
	 */
	public Iterable <Region> getRegions ()
	{
		return regions.values () ;
	}
	/**
	 * Accessor method for this GameMap object; gives access to the Region object identified by
	 * the UID passed by parameter.
	 * 
	 * @param uid the UID of the Region object being accessed.
	 * @return the wanted Region.
	 */
	public Region getRegionByUID ( int uid ) 
	{
		Region res ;
		res = regions.get ( uid ) ;
		return res ;
	}
	
	/**
	 * Select and return all the Region objects in this Map that has the type property
	 * equals to the parameter passed.
	 * 
	 * @param type the target region
	 * @return an Iterable containing all the Region object whose type property matches
	 *         the one passed as a parameter.
	 * @throws IllegalArgumentException if the type parameter is null
	 */
	public Iterable < Region > getRegionByType ( RegionType type ) 
	{
		Collection < Region > res ;
		res = new LinkedList < Region > () ;
		for ( Region r : regions.values () )
			if ( r.getType () == type )
				res.add ( r ) ;
		return res ;
	}
	
	/**
	 * Accessor method for this GameMap object: gives access to the Road object identified by
	 * the UID passed parameter.
	 * 
	 * @param uid the UID of the Road object being accessed.
	 * @return the wanted Road.
	 */
	public Road getRoadByUID ( int uid ) 
	{
		Road res ;
		res = roads.get ( uid ) ;
		return res ;
	}
	
	/**
	 * Select and returns all the free Road objects contained in this map.
	 * A Road is considered free if, and only if, there is nor a Sheperd or a Fence in it.
	 * 
	 * @return an Iterable containing all the Road object considered free.
	 */
	public Iterable < Road > getFreeRoads () 
	{
		Collection < Road > res ;
		res = new LinkedList < Road > () ;
		for ( Road road : roads.values () )
			if ( road.getElementContained() == null )
				res.add ( road ) ;
		return res ;
	}

	/**
	 * AS THE SUPER ONE'S
	 */
	@Override
	public String toString () 
	{
		String res ;
		res = "GameMap." ;
		res = res + "Regions : \n" ; 
		for ( Region r : regions.values() )
			res = res + "* " + r + "\n" ;
		res = res + "Roads :\n" ;
		for ( Road r : roads.values() )
			res = res + "* " + r + "\n" ;
		return res ;
	}	
	
	/***/
	public interface GameMapObserver extends Observer 
	{
		
		/***/
		public void onPositionableElementAdded ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) ;
	
		/***/
		public void onPositionableElementRemoved ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) ;
		
	}
	
}
