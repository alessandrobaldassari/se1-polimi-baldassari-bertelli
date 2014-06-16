package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

public interface PlayerObserver extends Observer
{

	public void onPay ( Integer paymentAmount , Integer moneyYouHaveNow ) ;
	
	public void onGetPayed ( Integer paymentAmount , Integer moneyYouHaveNow ) ;
	
	public void onCardAdded ( Card addedCard ) ;
	
	public void onCardRemoved ( Card removedCard ) ;
	
}
