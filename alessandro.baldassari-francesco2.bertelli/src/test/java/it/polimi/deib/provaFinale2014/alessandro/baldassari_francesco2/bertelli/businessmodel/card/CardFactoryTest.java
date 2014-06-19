package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card;

import static org.junit.Assert.*;

import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.CardFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

public class CardFactoryTest {

	CardFactory cardFactory;
	Identifiable<Match> dummyMatchIdentifier;
	LinkedList <Card> initialCards;
	LinkedList <SellableCard> sellableCards;
	byte cultivable, desert, hill, forest, lacustrine, mountain;
	
	
	@Before
	public void setUp() throws Exception 
	{
		dummyMatchIdentifier = MatchIdentifier.newInstance();
	}

	@Test (expected = IllegalArgumentException.class)
	public void generatedInitialCards() throws IllegalArgumentException, SingletonElementAlreadyGeneratedException {
		cardFactory = CardFactory.getInstance();
		try {
			initialCards = (LinkedList<Card>) cardFactory.generatedInitialCards(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			fail();
		}
		assertTrue(initialCards.size() == 6);
		for( Card card: initialCards)
			if(card.getRegionType() == RegionType.SHEEPSBURG)
				fail();
		initialCards = (LinkedList<Card>) cardFactory.generatedInitialCards(null);
	}
	
	@Test
	public void generatedSellableCards(){
		cardFactory =  CardFactory.getInstance();
		try {
			sellableCards = (LinkedList<SellableCard>) cardFactory.generatedSellableCards(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			fail();
		}
		assertTrue(sellableCards.size() == 30);
		hill = 0;
		desert = 0;
		forest = 0;
		lacustrine = 0;
		mountain = 0;
		cultivable = 0;
		for( Card card: sellableCards){
			if(card.getRegionType() == RegionType.SHEEPSBURG)
				fail();
			if(card.getRegionType() == RegionType.HILL)
				hill++;
			if(card.getRegionType() == RegionType.DESERT)
				desert++;
			if(card.getRegionType() == RegionType.FOREST)
				forest++;
			if(card.getRegionType() == RegionType.LACUSTRINE)
				lacustrine++;
			if(card.getRegionType() == RegionType.MOUNTAIN)
				mountain++;
			if(card.getRegionType() == RegionType.CULTIVABLE)
				cultivable++;	
		}
		assertTrue(hill == 5);
		assertTrue(desert ==5);
		assertTrue(forest == 5);
		assertTrue(lacustrine == 5);
		assertTrue(mountain == 5);
		assertTrue(cultivable == 5);
		
				
			
	}

}
