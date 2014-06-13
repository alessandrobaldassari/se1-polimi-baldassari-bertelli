package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.choosecardsview;

import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;

public class DefaultCardsChooseViewObserver implements CardsChooseViewObserver
{

	private AtomicReference < Iterable < Card > > cards ;
	
	public DefaultCardsChooseViewObserver ( AtomicReference < Iterable < Card > > cards ) 
	{
		this.cards = cards ;
	}
	
	@Override
	public void onCardChoosed ( Iterable < Card > selectedCards ) 
	{
		synchronized ( cards ) 
		{
			cards.set ( selectedCards ) ;
			cards.notifyAll () ;
		}
	}

	@Override
	public void onDoNotWantChooseAnyCard () {}

}
