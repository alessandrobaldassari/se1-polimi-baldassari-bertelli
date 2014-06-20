package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElement;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

/**
 * This class models the Road, a GameMap element which inglobes the line road and the numbered
 * circles in the GameMap. 
 */
public class Road extends GameMapElement
{

	// ATTRIBUTES
	
	/**
	 * The min value for the number property.
	 * It's a business rule.
	 */
	private static final int MIN_NUMBER = 1 ;
	
	/**
	 * The max value for the number property.
	 * It's a business rule. 
	 */
	private static final int MAX_NUMBER = 6 ;

	/**
	 * The first Region object bordering this Road. 
	 * Determined by the geography of the GameMap.
	 */
	private final Region firstBorderRegion ;
	
	/**
	 * The second Region object bordering this Road.
	 * Determined by the geography of the GameMap. 
	 */
	private final Region secondBorderRegion ;
	
	/**
	 * The number on this Road in the GameMap.
	 * Determined by the GameMap. 
	 */
	private final int number ;
	
	/**
	 * A collection containing the Road objects adjacent to this one.
	 * Determined by the Geography of the GameMap. 
	 */
	private Iterable < Road > adjacentRoads ;
	
	/**
	 * An element which is, at a given moment in time, contained in this Road.
	 * May be null if this is Road is empty. 
	 */
	private PositionableElement < Road > elementContained ;
	
	// METHODS 
	
	/**
	 * @param number the number on this Road in the GameMap.
	 * @param uid the uid for this Road
	 * @param firstBorderRegion the first Region object bordering this Road.
	 * @param secondBorderRegion the second Region object bordering this Road.
	 * @throws IllegalArgumentException if the number parameter is not lasquely contained
	 *         between the MIN_NUMBER and MAX_NUMBER properties, or if the firstBorderRegion
	 *         or secondBorderRegion parameter is null, or if the firstBorderRegion and
	 *         the secondBorderRegion parameter are equals.
	 */
	protected Road ( int number , int uid , Region firstBorderRegion , Region secondBorderRegion ) 
	{
		super ( GameMapElementType.ROAD , uid ) ;
		if ( number >= MIN_NUMBER && number <= MAX_NUMBER && firstBorderRegion != null && secondBorderRegion != null && firstBorderRegion.equals ( secondBorderRegion ) == false )
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
	 * Setter method for the adjacentRoads property.
	 * 
	 * @param adjacentRoads the adjacentProperty value
	 * @throws IllegalArgumentException if the parameter contains this Road object.
	 */
	void setAdjacentRoads ( Iterable < Road > adjacentRoads ) 
	{
		if ( CollectionsUtilities.contains ( adjacentRoads , this ) == false )
		{	
			this.adjacentRoads = new Vector < Road > () ;
			for ( Road r : adjacentRoads )
				( ( Collection < Road > )this.adjacentRoads ).add ( r ) ;
		}
		else 
			throw new IllegalArgumentException () ;
	} 
	
	/**
	 * Getter method for the AdjacentRoad proeprty.
	 * 
	 * @return the adjacentRoads property. 
	 */
	public Iterable < Road > getAdjacentRoads () 
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
	 * Getter method for the elementContained property.
	 * 
	 * @param elementContained the value for the elementContained property.
	 */
	public void setElementContained ( PositionableElement < Road > elementContained ) 
	{
		if ( elementContained != null )
			notifyAddElement ( getGameMapElementType () , getUID () , elementContained.getPositionableElementType () , elementContained.getUID () ) ;
		else
			notifyRemoveElement ( getGameMapElementType () , getUID () , this.elementContained.getPositionableElementType() , this.getElementContained().getUID() ) ;
		this.elementContained = elementContained ;
	}
	
	/**
	 * Return the Positionable entity contained in this Road.
	 * If this Road is free / not occupied the value returned will be null.
	 * 
	 * @return the Positionable entity contained in this Road
	 */
	public PositionableElement < Road > getElementContained () 
	{
		return elementContained ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public boolean equals ( Object obj ) 
	{
		boolean res ;
		if ( obj instanceof Road )
			res = super.equals ( obj ) ;
		else
			res = false ;
		return res;
	}
	
	/**
	 * AS THE SUPER' ONE. 
	 */
	@Override
	public String toString ()
	{
		String res ;
		res = super.toString () ;
		res = res + "Number : " + number + "\n" ;
		res = res + "Element Contained : " ;
		if ( elementContained != null )
			res = res + elementContained.getPositionableElementType().toString() + "\n" ;
		else
			res = res + "\n" ;
		res = res + "First Border Region : " + firstBorderRegion.getGameMapElementType() + " [ " + firstBorderRegion.getType() + " ]\n" ;
		res = res + "Second Border Region : " + secondBorderRegion.getGameMapElementType() + "[ " + secondBorderRegion.getType() + " ]\n" ;
		res = res + "Adjacent roads : " ;
		for ( Road r : adjacentRoads )
			res = res + "* " + r.getGameMapElementType() + " [ " + r.getNumber() + " ]\n" ;
		return res ;
	}
}
