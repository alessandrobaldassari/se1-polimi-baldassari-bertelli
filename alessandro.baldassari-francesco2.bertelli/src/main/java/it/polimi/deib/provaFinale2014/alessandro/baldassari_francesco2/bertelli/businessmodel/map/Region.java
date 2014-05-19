package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * 
 */
public class Region extends GameMapElement
{

	// ATTRIBUTES
	
	private final RegionType type ;
	private Iterable < Road > borderRoads ;
	private Collection < Animal > containedAnimals ; 
	
	// ACCESSOR METHODS
	
	Region ( RegionType type , int uid ) 
	{
		super ( uid ) ;
		if ( type != null )
		{
			this.type = type ;
			containedAnimals = new LinkedList < Animal > () ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * @return the Type of this Region
	 */
	public RegionType getType () 
	{
		return type ;
	}
	
	/**
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
	 * @return an Iterable of Road objects which are the ones which border this Region
	 */
	public Iterable < Road > getBorderRoads () 
	{
		return borderRoads ;
	}
	
	/**
	 * @return 
	 */
	public Collection < Animal > getContainedAnimals () 
	{
		return containedAnimals ;
	}
	
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
	
}
