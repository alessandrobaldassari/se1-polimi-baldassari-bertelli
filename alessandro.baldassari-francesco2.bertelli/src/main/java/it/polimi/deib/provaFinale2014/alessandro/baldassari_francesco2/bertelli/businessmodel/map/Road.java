package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import java.util.Collection;
import java.util.Collections;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Positionable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

public class Road extends GameMapElement
{

	// ATTRIBUTES
	
	private static final int MIN_NUMBER = 1 ;
	private static final int MAX_NUMBER = 6 ;
	private final Region firstBorderRegion ;
	private final Region secondBorderRegion ;
	private final int number ;
	private Collection < Road > adjacentRoads ;
	private Positionable < Road > elementContained ;
	
	// ACCESSOR METHODS 
	
	/**
	 * @param number
	 * @param uid
	 * @param firstBorderRegion
	 * @param secondBorderRegion
	 */
	Road ( int number , int uid , Region firstBorderRegion , Region secondBorderRegion ) 
	{
		super ( uid ) ;
		if ( number >= MIN_NUMBER && number <= MAX_NUMBER && firstBorderRegion != null && secondBorderRegion != null )
		{
			this.number = number ;
			this.firstBorderRegion = firstBorderRegion ;
			this.secondBorderRegion = secondBorderRegion ;
			elementContained = null ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Return the value written on the GameMap for this Road.
	 * 
	 * @return the value written on the GameMap for this Road.
	 */
	public int getNumber () 
	{
		return number ;
	}
	
	/**
	 * @param 
	 */
	void setAdjacentRoads ( Iterable < Road > adjacentRoads ) 
	{
		this.adjacentRoads = Collections.unmodifiableCollection ( CollectionsUtilities.newCollectionFromIterable ( adjacentRoads ) ) ;
	} 
	
	public Collection < Road > getAdjacentRoads () 
	{
		return adjacentRoads ;
	}
	
	/**
	 * Return the first Region bordering this Road.
	 * 
	 * @return the first Region bordering this Road
	 */
	public Region getFirstBorderRegion () 
	{
		return firstBorderRegion ;
	}
	
	/**
	 * Return the second Region bordering this Road.
	 * 
	 * @return the second Region bordering this Road
	 */
	public Region getSecondBorderRegion () 
	{
		return secondBorderRegion ;
	}
	
	/**
	 * @param
	 */
	public void setElementContained ( Positionable < Road > elementContained ) 
	{
		this.elementContained = elementContained ;
	}
	
	/**
	 * Return the Positionable entity contained in this Road.
	 * If this Road is free / not occupied the value returned will be null.
	 * 
	 * @return the Positionable entity contained in this Road
	 */
	public Positionable < Road > getElementContained () 
	{
		return elementContained ;
	}
	
}
