package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the actions a GameMapElementObserver may be notified about. 
 */
public interface GameMapElementObserver extends Observer
{
	
	/**
	 * Called when an element is added into the observed GameMapElement. 
	 * 
	 * @param whereType the Type of GameMapElement where the event took place. 
	 * @param whereId the UID of the GameMapElement where the event took place. 
	 * @param whoType the type of the element inserted into this region.
	 * @param whoId the UID of the element inserted into this region.
	 */
	public void onElementAdded ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) ;

	/**
	 * Called when an element is removed from the observed GameMapElement.
	 * 
	 * @param whereType the Type of GameMapElement where the event took place. 
	 * @param whereId the UID of the GameMapElement where the event took place. 
	 * @param whoType the type of the element removed from this region.
	 * @param whoId the UID of the element removed from this region.
	 */
	public void onElementRemoved ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) ;
	
}