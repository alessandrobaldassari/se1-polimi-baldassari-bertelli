package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import java.io.Serializable;

/**
 * This class represent a generic element which composes the GameMapElement.
 * It is an immutable class, also if it is expectable that its subclasses will not.
 */
public abstract class GameMapElement implements Serializable
{

	// ATTRIBUTES
	
	/**
	 * The GameMapElementType of this Object. 
	 */
	private GameMapElementType gameMapElementType ;
	
	/**
	 * The UID for this region, unique for every component of the map that extends this class
	 */
	private final int uid ;
	
	// METHODS 
	
	/**
	 * @param uid the uid of this GameMapElement
	 * @throws IllegalArgumentException if the parameter is < 0  
	 */
	protected GameMapElement ( GameMapElementType gameMapElementType , int uid ) 
	{
		if ( gameMapElementType != null && uid >= 0 )
		{
			this.gameMapElementType = gameMapElementType ;
			this.uid = uid ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/**
	 * Getter method for the gameMapElementType property.
	 * 
	 * @return the gameMapElementType property.
	 */
	public GameMapElementType getGameMapElementType () 
	{
		return gameMapElementType ;
	}
	
	/**
	 * @return the uid property. 
	 */
	public int getUID () 
	{
		return uid ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	public boolean equals ( Object obj ) 
	{
		GameMapElement other ;
		boolean res ;
		if ( obj instanceof GameMapElement ) 
		{
			other = ( GameMapElement ) obj ;
			res = ( uid == other.getUID () ) ;
		}
		else
			res = false ;
		return res ;
		
	}
	
	// ENUMERATIONS
	
	public enum GameMapElementType
	{
		
		REGION ,
		
		ROAD
		
	}
	
}
