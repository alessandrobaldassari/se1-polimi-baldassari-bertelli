package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;

/**
 * This class model the concept of a Card of the Game.
 * This details the static part ( about its structure ) of a card.
 */
public class Card implements Serializable , Identifiable
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
	
	/**
	 * The Player who owns this Card.
	 * The value will be null if this Card is not owned by any Player. 
	 */
	private Player owner ;
	
	// MEHTODS
	
	/**
	 * @param regionType the Region type represented by this Player.
	 * @param id the UID of this Card
	 * @throws IllegalArgumentException if the region type is null or the id is < 0 or if the
	 *         regionType is Sheepsburg.
	 */
	protected Card ( RegionType regionType , int id ) 
	{
		if ( regionType != null && regionType != RegionType.SHEEPSBURG && id >=0 )
		{
			this.regionType = regionType ;
			this.id = id ;
			owner = null ;
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
	@Override
	public int getUID () 
	{
		return id ;
	}
	
	/**
	 * Setter method for the owner property.
	 * 
	 * @param owner the value for the owner property.
	 * @throws IllegalArgumentException if the owner parameter is null.
	 */
	public void setOwner ( Player owner ) 
	{
		if ( owner != null )
			this.owner = owner ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the owner property.
	 * 
	 * @return the owner property.
	 */
	public Player getOwner () 
	{
		return owner ;
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
			if ( id == otherCard.getUID () )
				res = true ;
			else
				res = false ;
		}
		else
			res = false ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String toString ()
	{
		String res ;
		res = "Card\n" ;
		res = res + "Type : " + regionType + "\n" ;
		res = res + "Id : " + id ; 
		res = res + "Owner : " + owner !=null ? owner.getName() : null ;
		return res ;
	}
	
}
