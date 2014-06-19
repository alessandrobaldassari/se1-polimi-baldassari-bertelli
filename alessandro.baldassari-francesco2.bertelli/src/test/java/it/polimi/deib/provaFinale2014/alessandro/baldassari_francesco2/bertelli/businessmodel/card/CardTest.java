package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;

import org.junit.Test;

public class CardTest
{
	
	@Test 
	public void ctor ()
	{
		Card c = null ;
		c = new Card ( RegionType.CULTIVABLE ,3 ) ;
		assertTrue ( c != null ) ;
	}
	
	@Test 
	public void getRegionType  ()
	{
		RegionType r ;
		r = null ;
		try
		{
			r = RegionType.values() [ MathUtilities.random ( 0 , RegionType.values().length - 1 ) ] ;
			Card c = new Card ( r ,3 ) ;	
			if ( r != RegionType.SHEEPSBURG )
			assertTrue ( c.getRegionType() == r && r != RegionType.SHEEPSBURG ) ;
		}
		catch ( IllegalArgumentException e ) 
		{
			assertTrue ( r == RegionType.SHEEPSBURG ) ;
		}
	}
	
	@Test
	public void getId ()
	{
		Card c = new Card ( RegionType.CULTIVABLE ,3 ) ;		
		assertTrue ( c.getId() == 3 ) ;	
	}
	
	@Test
	public void setGetOwner () 
	{
		Player p ;
		Card c = new Card ( RegionType.CULTIVABLE ,3 ) ;		
		p = new DummyPlayer ( "" ) ;
		assertTrue ( c.getOwner() == null ) ;
		c.setOwner(p);
		assertTrue ( c.getOwner().equals(p) ) ;
	}
	
	@Test
	public void equals () 
	{
		Card c1 = new Card ( RegionType.CULTIVABLE , 3 ) ;
		Card c2 = new Card ( RegionType.CULTIVABLE , 4 ) ;
		assertFalse ( c1.equals ( c2 ) ) ;
		c2 = new Card ( RegionType.CULTIVABLE , 3 ) ; 
		assertTrue ( c1.equals(c2) ) ;
		assertFalse ( c2.equals(3) );
		assertFalse ( c1.equals(null) ) ;
	}
	
}
