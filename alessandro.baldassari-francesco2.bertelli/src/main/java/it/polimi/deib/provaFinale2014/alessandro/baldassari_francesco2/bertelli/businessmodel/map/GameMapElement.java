package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElement.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WithReflectionObservableSupport;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * This class represent a generic element which composes the GameMapElement.
 * It is an immutable class, also if it is expectable that its subclasses will not.
 */
public abstract class GameMapElement implements Serializable , Observable < GameMapElement.GameMapElementObserver >
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
	
	/**
	 * An object to easily implement the Observer pattern. 
	 */
	private WithReflectionObservableSupport < GameMapElementObserver > support ;
	
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
			support = new WithReflectionObservableSupport < GameMapElementObserver > () ;
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
		res = "UID : " + uid ;
		res = res + "Type : " + gameMapElementType + "\n" ;
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
	
	protected final void notifyAddElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
	{
		try {
			support.notifyObservers ( "onElementAdded" , whereType , whereId , whoType , whoId );
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected final void notifyRemoveElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
	{
		try {
			support.notifyObservers ( "onElementRemoved" , whereType , whereId , whoType , whoId );
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	public void addObserver ( GameMapElementObserver obs )
	{
		support.addObserver ( obs ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	public void removeObserver ( GameMapElementObserver obs ) 
	{
		support.removeObserver ( obs ) ;
	}
	
	// ENUMERATIONS
	
	public enum GameMapElementType
	{
		
		REGION ,
		
		ROAD
		
	}
	
	// INNER INTERFACES
	
	public interface GameMapElementObserver extends Observer
	{
		
		public void onElementAdded ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) ;
	
		public void onElementRemoved ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) ;
		
	}
	
}
