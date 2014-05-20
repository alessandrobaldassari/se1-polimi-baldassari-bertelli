package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;

/**
 * This class models a game's Fence, an immutable static element which can be placed
 * in a Street of the GameMap.
 */
public class Fence extends PositionableElement < Road >
{

	/**
	 * The type of this Fence ( final or not final ).
	 */
	private FenceType type ;
	
	/**
	 * @param type The type of this Fence. 
	 * @throws IllegalArgumentException if the value of the param is null.
	 */
	public Fence ( FenceType type ) 
	{
		if ( type != null )
			this.type = type ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Querying method to know if this Fence is a final one or it is not.
	 * 
	 * @return true if this Fence is final, false in all the other cases.
	 */
	public boolean isFinal () 
	{
		return type == FenceType.FINAL ;
	}
	
	// ENUMS
	
	/**
	 * This enum describes the type of Fences which exists in the game.
	 * NON_FINAL : used in the first phase of the game.
	 * FINAL : used in the final phase of the game.
	 */
	public enum FenceType 
	{
		
		NON_FINAL ,
		
		FINAL
		
	}
	
}
