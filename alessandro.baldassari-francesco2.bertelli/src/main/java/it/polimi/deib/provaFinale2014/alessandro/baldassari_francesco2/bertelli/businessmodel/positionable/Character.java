package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement;

/**
 * This class models a Character, which is an active component of the Game, one who can
 * stay in the GameMap ( as all the PositionableElements ) and can also move from one point of
 * the GameMap to one other.
 */
public abstract class Character < T extends GameMapElement > extends PositionableElement < T > 
{

	// ATTRIBUTES
	
	/**
	 * The name of this Character.
	 */
	private final String name ;
	
	// METHODS
	
	/**
	 * @param positionableElementType the PositionableElementType of this Character.
	 * @param name the Name of this Character.
	 * @throws IllegalArgumentException if the name parameter is null.
	 */
	protected Character ( PositionableElementType positionableElementType , String name ) 
	{
		super ( positionableElementType ) ;
		if ( name != null )
			this.name = name ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This methods implements the moving behavior of this Character.
	 * Technically it's a setter for the position property defined in the PositionableElement superclass.
	 * 
	 * @param newPosition the Position where this Character wants to go.
	 */
	public void moveTo ( T newPosition ) 
	{
		setPosition ( newPosition ) ;
	}
	
	/**
	 * Getter for the name property.
	 * 
	 *  @return the name of this Character.
	 */
	public String getName ()
	{
		return name ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	public boolean equals ( Object obj ) 
	{
		Character otherCharacter ;
		boolean res ;
		if ( obj instanceof Character ) 
		{
			otherCharacter = ( Character ) obj ;
			if ( name.compareTo ( otherCharacter.getName () ) == 0 )
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
	public String toString () 
	{
		String res ;
		res = super.toString () ;
		res = res + "Name : " + name + "\n" ;
		return res ;
	}
	
}

