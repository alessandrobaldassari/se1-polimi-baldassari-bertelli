package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the events a SheperdColorRequestViewObserver can be notified about.
 */
public interface SheperdColorChooseViewObserver extends Observer
{
	
	/**
	 * Called when a color is choosed and the User confirm that he has decided the value. 
	 * 
	 * @param selectedColor the color the User choosed.
	 */
	public void onColorChoosed ( NamedColor selectedColor ) ;
	
	/**
	 * Undo events 
	 */
	public void onDoNotWantChooseColor () ;
	
}