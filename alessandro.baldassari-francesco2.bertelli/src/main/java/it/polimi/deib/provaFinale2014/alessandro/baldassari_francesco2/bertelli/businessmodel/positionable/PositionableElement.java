package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;

/**
 * This class models a generic element which can be positioned on the GameMap element. 
 */
public abstract class PositionableElement < T extends GameMapElement > implements Serializable , Identifiable
{

	// ATTRIBUTES
	
	private static transient int UIDGenerator = -1 ;
	
	/***/
	private final int uid ;
	
	/**
	 * The type of this PositionableElement. 
	 */
	private PositionableElementType positionableElementType ;
	
	/**
	 * The position of this element in the GameMap.
	 * It must refer to an Object contained in the GameMap element.
	 */
	private T position ;
	
	// METHODS

	/**
	 * @throw IllegalArgumentException if the parameter is null. 
	 */
	protected PositionableElement ( PositionableElementType positionableElementType ) 
	{
		if ( positionableElementType != null )
		{
			UIDGenerator ++ ;
			uid = UIDGenerator ;
			this.positionableElementType = positionableElementType ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public int getUID () 
	{
		return uid ;
	}
	
	/**
	 * Getter method for the positionableElementType property.
	 * 
	 * @return the positionableElementType value.
	 */
	public PositionableElementType getPositionableElementType () 
	{
		return positionableElementType ;
	}
	
	/**
	 * Setter for the position property.
	 * 
	 * @param position the new position for this PositionableElement.
	 */
	protected void setPosition ( T position ) 
	{
		this.position = position ;
	}

	/**
	 * Setter method for the positionableElementType field.
	 * 
	 * @param newType the value for the positionableElementType field.
	 * @throws IllegalArgumentException if the newType parameter is null.
	 */
	protected void setElementType ( PositionableElementType newType ) 
	{
		if ( newType != null )
			this.positionableElementType = newType ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the position property.
	 * 
	 * @return the value of the position property.
	 */
	public T getPosition () 
	{
		return position ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String toString () 
	{
		String res ;
		res = "Character.\n" ;
		res = res + "Type : " + positionableElementType + "\n" ;
			res = res + "Position : " + ( position != null ? position.getGameMapElementType () : " unset" )+ "\n" ;
		return res ;
	}
		
}
