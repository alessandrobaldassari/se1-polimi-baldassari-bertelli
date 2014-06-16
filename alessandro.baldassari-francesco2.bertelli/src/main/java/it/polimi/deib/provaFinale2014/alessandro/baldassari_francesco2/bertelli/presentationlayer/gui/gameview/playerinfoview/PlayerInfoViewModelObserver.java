package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

public interface PlayerInfoViewModelObserver extends Observer 
{

	/**
	 * @param newAmount the amount of money the observed has now.
	 * @param cause the cause of the change; if false ( negative ) it has been a payment, else the observed got payed.
	 */
	public void onMoneyReserveChanged ( int newAmount , boolean cause ) ;
	
	/***/
	public void onCardsChanged ( RegionType cardType , int numberOfCardOfThisTypeNow ) ;
	
}
