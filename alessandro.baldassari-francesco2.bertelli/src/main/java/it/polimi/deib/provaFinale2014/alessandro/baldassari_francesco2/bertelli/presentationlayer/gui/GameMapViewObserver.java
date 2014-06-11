package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/***/
public interface GameMapViewObserver extends Observer 
{
	
	/***/
	public void onRegionSelected ( Integer regionUID ) ;
	
	/***/
	public void onRoadSelected ( Integer roadUID ) ;
	
	/***/
	public void onSheperdSelected ( Integer sheperdId ) ;
	
	/***/
	public void onAnimalSelected ( Integer animalId ) ;
	
	/***/
	public void onDoNotWantToMakeAnySelection () ;
	
}
