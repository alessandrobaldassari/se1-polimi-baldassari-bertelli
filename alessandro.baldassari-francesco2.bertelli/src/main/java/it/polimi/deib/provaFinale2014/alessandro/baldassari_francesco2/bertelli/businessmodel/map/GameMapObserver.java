package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the events a GameMapObserver can be notified about. 
 */
public interface GameMapObserver extends Observer 
{
	
	/**
	 * 
	 */
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) ;

	/**
	 * 
	 */
	public void onPositionableElementRemoved ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) ;
	
}