package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.choosecardsview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the methods that a CardsChooseViewObserver can listen to. 
 */
public interface CardsChooseViewObserver extends Observer
{
	
	/**
	 * Called when the User has finished the choosing process and confirm his selection.
	 * 
	 * @param selectedCards the Cards the User has choosen.
	 */
	public void onCardChoosed ( Iterable < Card > selectedCards ) ;
	
	/**
	 * Called when the User does not want to make any selection. 
	 */
	public void onDoNotWantChooseAnyCard () ;
	
}