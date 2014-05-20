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
	
	/***/
	private final int id ;
	
	
	// MEHTODS
	
	/**
	 * @param regionType the Region type represented by this Player.
	 * @param id the UID of this Card
	 * @throws IllegalArgumentException if the region type is null or the id is < 0.
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
