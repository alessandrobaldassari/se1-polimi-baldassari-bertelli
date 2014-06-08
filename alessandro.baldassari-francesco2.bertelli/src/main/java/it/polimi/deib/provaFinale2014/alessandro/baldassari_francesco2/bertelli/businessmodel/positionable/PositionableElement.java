package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement;

/**
 * This class models a generic element which can be positioned on the GameMap element. 
 */
public abstract class PositionableElement < T extends GameMapElement > implements Serializable
{

	// ATTRIBUTES
	
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
			this.positionableElementType = positionableElementType ;
		else
			throw new IllegalArgumentException () ;
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
		res = res + "Position : " + position + "\n" ;
		return res ;
	}
	
	// ENUMERATIONS
	
	public enum PositionableElementType 
	{
		
		FENCE ,
		
		SHEPERD ,
	
		WOLF ,

		LAMB ,

		RAM ,
		
		SHEEP ,
		
		BLACK_SHEEP ,
			
	}
	
}
