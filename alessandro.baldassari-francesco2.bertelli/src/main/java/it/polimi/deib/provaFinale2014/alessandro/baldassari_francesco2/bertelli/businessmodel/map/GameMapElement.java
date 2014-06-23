package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent a generic element which composes the GameMapElement.
 * It is an immutable class, also if it is expectable that its subclasses will not.
 */
public abstract class GameMapElement extends WithReflectionAbstractObservable < GameMapElementObserver > implements Serializable , Identifiable
{

	// ATTRIBUTES
	
	/**
	 * The name of the method to call during the onElementAdded event. 
	 */
	private static final String ON_ELEMENT_ADDED_METHOD_NAME = "onElementAdded" ;
	
	/**
	 * The name of the method to call during the onElementRemoved event. 
	 */
	private static final String ON_ELEMENT_REMOVED_METHOD_NAME = "onElementRemoved" ;
	
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
	 * @param gameMapElementType the GameMapElementType of this GameMapElement.
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
	 * AS THE SUPER'S ONE. 
	 */
	@Override
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
	 * Notify the Observers of this GameMapElement that an Element has been added here.
	 * 
	 * @param whereType the type of location where the event took place.
	 * @param whereId the id of the location where the event took place.
	 * @param whoType the type of the element added.
	 * @param whoId the id of the element added.
	 */
	protected synchronized final void notifyAddElement ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) 
	{
		try 
		{
			notifyObservers ( ON_ELEMENT_ADDED_METHOD_NAME , whereType , whereId , whoType , whoId );
		}
		catch ( MethodInvocationException e ) 
		{
			Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e );
		}
	}
	
	/**
	 * Notify the Observers of this GameMapElement that an Element has been removed from here.
	 * 
	 * @param whereType the type of location where the event took place.
	 * @param whereId the id of the location where the event took place.
	 * @param whoType the type of the element removed.
	 * @param whoId the id of the element removed.
	 */
	protected synchronized final void notifyRemoveElement ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) 
	{
		try 
		{
			notifyObservers ( ON_ELEMENT_REMOVED_METHOD_NAME , whereType , whereId , whoType , whoId );
		} 
		catch ( MethodInvocationException e ) 
		{
			Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e  ) ;
		}
	}
	
}
