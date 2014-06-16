package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.SellingPriceNotSetException;

import org.junit.Before;
import org.junit.Test;

public class SellableCardTest {
	
	SellableCard sellableCard, notSetSellableCard;	
	
	@Before
	public void setUp() throws Exception {
		sellableCard = new SellableCard(RegionType.CULTIVABLE, 1, 4);
		sellableCard.setSellable(true);
		
		notSetSellableCard = new SellableCard(RegionType.HILL, 2, 1);
		notSetSellableCard.setSellable(false);
	}

	@Test (expected = IllegalArgumentException.class)
	public void SellableCard() {
		sellableCard = new SellableCard(RegionType.DESERT, 2, -1);
	}
	
	@Test
	public void getInitialPrice(){
		assertTrue(sellableCard.getInitialPrice() == 4);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void setSellingPrice(){
		try{
			sellableCard.setSellingPrice(1);
		}
		catch(IllegalArgumentException e){
			fail();
		}
		sellableCard.setSellingPrice(7);
	}
	
	@Test
	public void getSellingPrice() throws SellingPriceNotSetException, NotSellableException{
		try{
			notSetSellableCard.getSellingPrice();
		}
		catch(NotSellableException e){
			assertTrue(true);
		}
		try {
			sellableCard.getSellingPrice();
		} catch (SellingPriceNotSetException e) {
			assertTrue(true);
		}
		sellableCard.setSellingPrice(1);
		assertTrue(sellableCard.getSellingPrice() == 1);
		
	}
	
	@Test
	public void setSellable(){
		assertTrue(sellableCard.isSellable() == true);
	}
	
	@Test
	public void isSellable(){
		assertTrue(sellableCard.isSellable() == true);
		assertTrue(notSetSellableCard.isSellable() == false);
	}

}
