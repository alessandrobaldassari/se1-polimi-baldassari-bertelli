package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

import java.io.Serializable;

/**
 * This class represent a generic element which composes the GameMapElement.
 * It is an immutable class, also if it is expectable that its subclasses will not.
 */
public abstract class GameMapElement extends WithReflectionAbstractObservable < GameMapElementObserver > implements Serializable
{

	// ATTRIBUTES
	
	/**
	 * The UID for this region, unique for every component of the map that extends this class
	 */
	private final int uid ;
	
	/**
	 * The GameMapElementType of this Object. 
	 */
	private GameMapElementType gameMapElementType ;
	
	// METHODS 
	
	/**
	 * @param uid the uid of this GameMapElement
	 * @throws IllegalArgumentException if the parameter is < 0  
	 */
	protected GameMapElement ( GameMapElementType gameMapElementType , int uid ) 
	{
		super () ;
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
	@Override
	public String toString ()
	{
		String res ;
		res = "UID : " + uid + Utilities.CARRIAGE_RETURN ;
		res = res + "Type : " + gameMapElementType + Utilities.CARRIAGE_RETURN ;
		return res ;
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

	/**
	 *  
	 */
	protected final void notifyAddElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
	{
		try 
		{
			notifyObservers ( "onElementAdded" , whereType , whereId , whoType , whoId );
		}
		catch ( MethodInvocationException e ) 
		{
			e.printStackTrace () ;
			System.out.println ( e.getMessage() ) ;
			System.out.println ( e.getCause() ) ;
		}
	}
	
	/***/
	protected final void notifyRemoveElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
	{
		try 
		{
			notifyObservers ( "onElementRemoved" , whereType , whereId , whoType , whoId );
		} 
		catch ( MethodInvocationException e ) 
		{
			e.printStackTrace () ;
			System.out.println ( e.getMessage() ) ;
			System.out.println ( e.getCause() ) ;
		}
	}
	
	// ENUMERATIONS
	
	// INNER INTERFACES
	
}
