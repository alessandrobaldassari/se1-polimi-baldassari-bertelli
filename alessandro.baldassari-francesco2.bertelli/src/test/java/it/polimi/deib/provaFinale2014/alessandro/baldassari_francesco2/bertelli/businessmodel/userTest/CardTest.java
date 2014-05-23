package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.userTest;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.SellingPriceNotSetException;

import org.junit.*;

import static org.junit.Assert.*;


public class CardTest{
	private Card hillCard;
	private SellableCard sellableCardWithoutPrice;
	
	@Before public void setUp(){
		hillCard = new Card(RegionType.HILL, 1);
		sellableCardWithoutPrice = new SellableCard(RegionType.FOREST, 2 , 3);
	}
	
	@Test public void Card(){
		assertTrue(new Card(RegionType.HILL, 1).equals(hillCard));
		assertFalse(new Card(RegionType.HILL, 1).equals(sellableCardWithoutPrice));

		
	}
	
	@Test public void getRegionType(){
		assertEquals(hillCard.getRegionType(), RegionType.HILL);
		assertNotEquals(hillCard.getRegionType(), RegionType.FOREST);
	}
	
	@Test public void getId(){
		assertEquals(hillCard.getId(), 1);
		assertNotEquals(hillCard.getId(), 2);		
	}
	
	@Test public void getInitialPrice(){
		assertEquals(sellableCardWithoutPrice.getInitialPrice(), 3);
		assertNotEquals(sellableCardWithoutPrice.getInitialPrice(), 4);
	}
	
	@Test public void isSellable(){
		assertEquals(sellableCardWithoutPrice.isSellable(), false);
		assertNotEquals(sellableCardWithoutPrice.isSellable(), true);
	}
	
	@Test public void setSellable(){
		sellableCardWithoutPrice.setSellable(true);
		assertTrue(sellableCardWithoutPrice.isSellable());
	}
	
	@Test (expected = NotSellableException.class) public void getSellingPrice() throws NotSellableException, SellingPriceNotSetException{
		sellableCardWithoutPrice.getSellingPrice();
	}
	
	@Test (expected = NotSellableException.class) public void getSellingPrice1() throws NotSellableException, SellingPriceNotSetException{
		assertEquals ( sellableCardWithoutPrice.getSellingPrice() , 3 );
	}
	@Test (expected = IllegalArgumentException.class) public void setSellingPrice(){
		sellableCardWithoutPrice.setSellingPrice(-1);
	}
}
