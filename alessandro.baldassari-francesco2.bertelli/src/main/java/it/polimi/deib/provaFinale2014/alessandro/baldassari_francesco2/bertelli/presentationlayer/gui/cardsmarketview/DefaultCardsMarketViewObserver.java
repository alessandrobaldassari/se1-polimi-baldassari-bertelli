package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.cardsmarketview;

import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;

/***/
public class DefaultCardsMarketViewObserver implements CardsMarketViewObserver
{

	/***/
	private AtomicReference < SellableCard [] > cards ;
	
	/***/
	public DefaultCardsMarketViewObserver ( AtomicReference < SellableCard [] > cards ) 
	{
		this.cards = cards ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onCardChoosed ( SellableCard [] selectedCards ) 
	{
		synchronized ( cards ) 
		{
			cards.set ( selectedCards ) ;
			cards.notifyAll () ;
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onDoNotWantChooseAnyCard () {}

}
