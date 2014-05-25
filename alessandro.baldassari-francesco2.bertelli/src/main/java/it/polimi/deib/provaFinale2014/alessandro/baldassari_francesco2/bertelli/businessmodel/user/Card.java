package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;

/**
 * This class model the concept of a Card of the Game.
 * This details the static part ( about its structure ) of a card.
 */
public class Card 
{

	// ATTRIBUTES
	
	/**
	 * The Region type this card is associated with.
	 */
	private final RegionType regionType ;
	
	/**
	 * The identifier for this Card object. 
	 */
	private final int id ;
	
	
	// MEHTODS
	
	/**
	 * @param regionType the Region type represented by this Player.
	 * @param id the UID of this Card
	 * @throws IllegalArgumentException if the region type is null or the id is < 0 or if the
	 *         regionType is Sheepsburg.
	 */
	public Card ( RegionType regionType , int id ) 
	{
		if ( regionType != null && regionType != RegionType.SHEEPSBURG && id >=0 )
		{
			this.regionType = regionType ;
			this.id = id ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * Two card object are considered equals if, and only if, their two ids are the same.
	 * This definition is valid also for every subclass of Card, so this method is marked as final.
	 */
	@Override
	public final boolean equals ( Object obj ) 
	{
		Card otherCard ;
		boolean res ;
		if ( obj instanceof Card ) 
		{
			otherCard = ( Card ) obj ;
			if ( id == otherCard.getId () )
				res = true ;
			else
				res = false ;
		}
		else
			res = false ;
		return res ;
	}
	
	/**
	 * Getter for the Region Type property.
	 * 
	 * @return the region type associated with this Card.
	 */
	public RegionType getRegionType () 
	{
		return regionType ;
	}
	
	/**
	 * Getter for the id property.
	 * 
	 * @return the UID of this Card.
	 */
	public int getId () 
	{
		return id ;
	}
	
}
