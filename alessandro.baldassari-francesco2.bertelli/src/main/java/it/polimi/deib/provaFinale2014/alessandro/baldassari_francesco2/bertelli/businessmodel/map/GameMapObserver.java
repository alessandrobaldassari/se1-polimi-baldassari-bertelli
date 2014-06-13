package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the events a GameMapObserver can be notified about. 
 */
public interface GameMapObserver extends Observer 
{
	
	/**
	 * Called when an element is added into the observed GameMap. 
	 * 
	 * @param whereType the Type of the GameMapElement where the event took place. 
	 * @param whereId the UID of the the GameMapElement where the event took place. 
	 * @param whoType the type of the element inserted into this GameMap.
	 * @param whoId the UID of the element inserted into this GameMap.
	 */
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) ;

	/**
	 * Called when an element is removed from the observed GameMap.
	 * 
	 * @param whereType the Type of GameMapElement where the event took place. 
	 * @param whereId the UID of the GameMapElement where the event took place. 
	 * @param whoType the type of the element removed from this GameMap.
	 * @param whoId the UID of the element removed from this GameMap.
	 */
	public void onPositionableElementRemoved ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) ;
	
}