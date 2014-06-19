package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.cardsmarketview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the methods that a CardsChooseViewObserver can listen to. 
 */
public interface CardsMarketViewObserver extends Observer
{
	
	/**
	 * Called when the User has finished the choosing process and confirm his selection.
	 * 
	 * @param selectedCards the Cards the User has choosen.
	 */
	public void onCardChoosed ( Iterable < SellableCard > selectedCards ) ;
	
	/**
	 * Called when the User does not want to make any selection. 
	 */
	public void onDoNotWantChooseAnyCard () ;
	
}