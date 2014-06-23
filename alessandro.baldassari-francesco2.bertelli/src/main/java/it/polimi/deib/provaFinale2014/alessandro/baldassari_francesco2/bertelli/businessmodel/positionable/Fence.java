package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;

/**
 * This class models a game's Fence, an immutable static element which can be placed
 * in a Street of the GameMap.
 */
public class Fence extends PositionableElement < Road >
{

	// ATTRIBUTES
	
	/**
	 * The type of this Fence ( final or not final ).
	 */
	private FenceType type ;
	
	/**
	 * The property which says if a Fence has been placed in the GameMap or not. 
	 */
	private boolean placed ;
	
	// METHODS
	
	/**
	 * @param type The type of this Fence. 
	 * @throws IllegalArgumentException if the value of the param is null.
	 */
	public Fence ( FenceType type ) 
	{
		super ( PositionableElementType.FENCE ) ;
		if ( type != null )
		{
			this.type = type ;
			placed = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * Two Fence objects are considered equals if, and only if thery are of the same type and they
	 * are both placed and in the same position. 
	 */
	@Override
	public boolean equals ( Object obj ) 
	{
		Fence otherFence ;
		boolean res ;
		if ( obj instanceof Fence ) 
		{
			otherFence = ( Fence ) obj ;
			if ( type == otherFence.type )
			{
				if ( placed == false )
				{
					if ( otherFence.isPlaced() == false ) 
					{
						if ( getPosition ().equals ( otherFence.getPosition() ) )
							res = true ;
						else
							res = false ;
					}
					else
						res = false ;
				}
				else
				{
					if ( otherFence.isPlaced () == true )
					{
						res = super.equals ( obj ) ;
					}
					else
						res = false ;
				}
			}
			else
				res = false ;
		}
		else 
			res = false ;
		return res ;
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
	
	/**
	 * Querying method to know if this Fence is a placed in the Map or not.
	 * 
	 * @return the placed property.
	 */
	public boolean isPlaced () 
	{
		return placed ;
	}
	
	/**
	 * This method set the position of this Fence.
	 * 
	 * @param road the Road where place this Road.
	 * @throws FenceAlreadyPlacedException if the position of this Fence has already been set. 
	 */
	public void place ( Road road ) throws FenceAlreadyPlacedException 
	{
		if ( placed == false )
		{
			if ( road != null )
			{
				setPosition ( road ) ;
				road.setElementContained ( this ) ;
				placed = true ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		else 
			throw new FenceAlreadyPlacedException ( this ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String toString () 
	{
		String res ;
		res = super.toString () ;
		res = res + "Type : " + type + "\n" ;
		res = res + "Place : " + placed + "\n" ;
		return res ;
	}
	
	// ENUMS
	
	/**
	 * This enum describes the type of Fences which exists in the game.
	 * 1. NON_FINAL : used in the first phase of the game.
	 * 2. FINAL : used in the final phase of the game.
	 */
	public enum FenceType 
	{
		
		NON_FINAL ,
		
		FINAL
			
	}
	
	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This class model the situation where a User tries to set the Position of an
	 * already placed Fence. 
	 */
	public class FenceAlreadyPlacedException extends Exception 
	{
		
		/**
		 * The fence that raised this Exception. 
		 */
		private Fence fence ;
		
		/**
		 * @throws IllegalArgumentException if the fence parameter is null. 
		 */
		public FenceAlreadyPlacedException ( Fence fence ) 
		{
			super () ;
			if ( fence != null )
				this.fence = fence ;
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * Getter method for the fence property.
		 * 
		 * @return the fence property.
		 */
		public Fence getFence () 
		{
			return fence ;
		}
		
	}
	
}
