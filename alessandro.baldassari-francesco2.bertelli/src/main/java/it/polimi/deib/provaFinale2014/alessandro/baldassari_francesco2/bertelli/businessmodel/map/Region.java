package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class models a Region in the GameMap, a place where Animals can stay.
 */
public class Region extends GameMapElement
{

	// ATTRIBUTES
	
	/**
	 * The type of this Region.
	 * Determined by the geography of the GameMap. 
	 */
	private final RegionType type ;
	
	/**
	 * The set of Road object bordering this Region.
	 * Determined by the geography of the GameMap.
	 */
	private Iterable < Road > borderRoads ;
	
	/**
	 * The collection containing the Animal objects which stay in this Region.
	 * Evolves during the Match.
	 */
	private Collection < Animal > containedAnimals ; 
	
	// METHODS
	
	/**
	 * @param type the Type of this Region.
	 * @throws IllegalArgumentException if the type parameter is null.
	 */
	protected Region ( RegionType type , int uid ) 
	{
		super ( GameMapElementType.REGION , uid ) ;
		if ( type != null )
		{
			this.type = type ;
			containedAnimals = new LinkedList < Animal > () ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the type property.
	 * 
	 * @return the Type of this Region
	 */
	public RegionType getType () 
	{
		return type ;
	}
	
	/**
	 * Setter method for the borderRoads property.
	 * 
	 * @param an Iterable containing the Road objects which border this Region
	 * 		  Because the property borderRoads is thinked as an immutable ones ( the Roads bordering a Region are 
	 * 	      always the same ), this method makes a copy of the data structure passed by parameters 
	 * 	      ( so it is not backed end with the passing data structure anymore ).
	 */
	void setBorderRoads ( Iterable < Road > borderRoads ) 
	{
		this.borderRoads = CollectionsUtilities.newCollectionFromIterable ( borderRoads ) ;
	}
	
	/**
	 * Getter method for the borderRoads object.
	 * 
	 * @return an Iterable of Road objects which are the ones which border this Region
	 */
	public Iterable < Road > getBorderRoads () 
	{
		return borderRoads ;
	}
	
	/**
	 * @throws NoRoadWithThisNumberException */
	public Road getBorderRoad ( int number ) throws NoRoadWithThisNumberException 
	{
		Road res ;
		res = null ;
		for ( Road r : borderRoads )
			if ( r.getNumber () == number )
			{
				res = r ;
				break ;
			}
		if ( res == null )
			throw new NoRoadWithThisNumberException () ;
		return res ;
	}
	
	public void addAnimal ( Animal newAnimal ) 
	{
		containedAnimals.add ( newAnimal ) ;
	}
	
	public void removeAnimal ( Animal animal ) 
	{
		containedAnimals.remove ( animal ) ;
	}
	
	/**
	 * Getter for the containedAnimals property.
	 * 
	 * @return the containedAnimals property.
	 */
	public Iterable < Animal > getContainedAnimals () 
	{
		return containedAnimals ;
	}
	
	/***/
	public boolean isClosed () 
	{
		boolean result ;
		result = true ;
		for ( Road road : borderRoads )
			if ( road.getElementContained () == null || ! ( road.getElementContained () instanceof Fence ) )
			{
				result = false ;
				break ;
			}
		return result ;
	}
	
	@Override
	public String toString(){
		return type.toString();
	}
	
	// ENUMS
	
	/**
	 * The types of Region which exists in the GameMap.
	 * Determined by the GameMap.
	 */
	public enum RegionType 
	{
		
		HILL ,
		
		FOREST ,
		
		LACUSTRINE ,
		
		CULTIVABLE ,
		
		MOUNTAIN ,
		
		DESERT ,
		
		SHEEPSBURG
		
	}
	
	// INNER CLASSES
	
	// EXCEPTIONS
	
	/***/
	public class NoRoadWithThisNumberException extends Exception {}
	
	
}
