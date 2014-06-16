package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This class defines the events a GameMapViewObserver can listen to. 
 */
public interface GameMapViewObserver extends Observer 
{
	
	/**
	 * Called when a Region on the Map is selected.
	 * 
	 * @param regionUID the UID of the selected region.
	 */
	public void onRegionSelected ( Integer regionUID ) ;
	
	/***/
	public void onRoadSelected ( Integer roadUID ) ;
	
	/***/
	public void onSheperdSelected ( Integer sheperdId ) ;
	
	/***/
	public void onAnimalSelected ( Integer animalId ) ;
	
	/***/
	public void onWantToChangeMove () ;
	
}
