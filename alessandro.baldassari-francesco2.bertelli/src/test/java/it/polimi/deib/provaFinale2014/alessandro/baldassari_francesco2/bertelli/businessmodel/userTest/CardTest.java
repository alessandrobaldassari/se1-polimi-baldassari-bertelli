package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.userTest;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.exception.*;

import org.junit.*;

import static org.junit.Assert.*;


public class CardTest{
	private Card hillCard;
	private Card sellableCardWithoutPrice;
	
	@Before public void setUp(){
		hillCard = new Card(RegionType.HILL, 1, 4);
		sellableCardWithoutPrice = new Card(RegionType.FOREST, 2, 2);
		sellableCardWithoutPrice.setSellable(true);
	}
	
	@Test public void Card(){
		assertTrue(new Card(RegionType.HILL, 1, 4).equals(hillCard));
		assertFalse(new Card(RegionType.HILL, 1, 4).equals(sellableCardWithoutPrice));

		
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
		assertEquals(hillCard.getInitialPrice(), 4);
		assertNotEquals(hillCard.getInitialPrice(), 3);
	}
	
	@Test public void isSellable(){
		assertEquals(hillCard.isSellable(), false);
		assertNotEquals(hillCard.isSellable(), true);
	}
	
	@Test public void setSellable(){
		hillCard.setSellable(true);
		assertTrue(hillCard.isSellable());
	}
	
	@Ignore("Problem with exceptions")
	@Test (expected = NotSellableException.class) public void getSellingPrice(){
				try {
					hillCard.getSellingPrice();
				} catch (NotSellableException e) {
					e.printStackTrace();
				} catch (SellingPriceNotSetException e){
					e.printStackTrace();
				}
	}
	
	@Test (expected = IllegalArgumentException.class) public void setSellingPrice(){
		sellableCardWithoutPrice.setSellingPrice(-1);
	}
}
