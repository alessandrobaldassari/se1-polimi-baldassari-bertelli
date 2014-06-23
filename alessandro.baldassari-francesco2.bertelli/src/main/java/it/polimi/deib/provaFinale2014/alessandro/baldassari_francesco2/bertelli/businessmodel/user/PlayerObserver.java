package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the type of events a PlayerObserver can be notified of. 
 */
public interface PlayerObserver extends Observer
{
	
	/**
	 * Called when the observed Player receive a Card.
	 * 
	 * @param cardType the type of the added Card.
	 */
	public void onCardAdded ( RegionType cardType ) ;
	
	/**
	 * Called when the observed Player loose a Card.
	 * 
	 * @param cardType the type of the removed Card.
	 */
	public void onCardRemoved ( RegionType cardType ) ;
	
	/**
	 * Called when the observed Player pay.
	 * 
	 * @param paymentAmount the amount of money the Player payed.
	 * @param moneyYouHaveNow the money that the Player has after the transaction.
	 */
	public void onPay ( Integer paymentAmount , Integer moneyYouHaveNow ) ;
	
	/**
	 * Called when the observed Player get payed.
	 * 
	 * @param paymentAmount the amount of money the Player received.
	 * @param moneyYouHaveNow the money that the Player has after the transaction.
	 */
	public void onGetPayed ( Integer paymentAmount , Integer moneyYouHaveNow ) ;
	
}
