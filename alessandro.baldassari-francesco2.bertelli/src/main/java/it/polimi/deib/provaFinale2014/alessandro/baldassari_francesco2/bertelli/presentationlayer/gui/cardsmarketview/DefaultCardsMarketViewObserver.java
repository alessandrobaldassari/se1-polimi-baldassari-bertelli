package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.cardsmarketview;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.IterableContainer;

/***/
public class DefaultCardsMarketViewObserver implements CardsMarketViewObserver
{

	/***/
	private AtomicReference < IterableContainer < SellableCard > > cards ;
	
	/***/
	public DefaultCardsMarketViewObserver ( AtomicReference < IterableContainer < SellableCard > > cards ) 
	{
		this.cards = cards ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onCardChoosed ( IterableContainer < SellableCard > selectedCards ) 
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
	public void onDoNotWantChooseAnyCard () 
	{
		synchronized (cards) 
		{
			cards.set ( new IterableContainer < SellableCard > ( new ArrayList < SellableCard > ( 0 ) ) );
			cards.notifyAll();
		}
	}

}
