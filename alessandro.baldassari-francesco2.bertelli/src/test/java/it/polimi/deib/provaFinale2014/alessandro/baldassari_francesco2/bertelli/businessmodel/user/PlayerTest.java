package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.CardFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

public class PlayerTest {

	private Player p ;
	
	@Test
	public void ctorOkAndName () 
	{
		String n = "ALDO" ;
		p = new DummyPlayer(n) ;
		assertTrue ( p.getName().compareTo ( n ) == 0 ) ;
		assertTrue ( p.isSuspended() == false ) ;
 		assertTrue ( CollectionsUtilities.iterableSize ( p.getSellableCards() ) == 0 ) ;
 		assertTrue ( p.getSheperds() == null ) ;
	}
	
	@Test
	public void addRemoveHasCard () 
	{
		List <SellableCard> c;
		try {
			c = CollectionsUtilities.newListFromIterable( CardFactory.getInstance().generatedSellableCards ( MatchIdentifier.newInstance() ) );
			p = new DummyPlayer("aldo") ;
			SellableCard c1 = c.get(0);
			SellableCard c2 = c.get(1);
			p.addCard ( c1 ) ;
			assertTrue ( CollectionsUtilities.contains ( p.getSellableCards() , c1 ) ) ;
			assertFalse ( p.hasCard(c2) );
			p.addCard(c2) ;
			assertTrue ( CollectionsUtilities.contains ( p.getSellableCards() , c1 ) ) ;
			assertTrue ( p.hasCard(c1) ) ;
			assertTrue ( CollectionsUtilities.contains ( p.getSellableCards() , c2 ) ) ;
			p.removeCard ( c1 ) ;
			assertFalse ( CollectionsUtilities.contains ( p.getSellableCards() , c1 ) ) ;
			assertTrue ( CollectionsUtilities.contains ( p.getSellableCards() , c2 ) ) ;			
			assertTrue ( p.hasCard ( c2 ) ) ;
			assertFalse ( p.hasCard ( c1 ) ) ;
		} 
		catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void moneyMethods () 
	{
		p = new DummyPlayer ( "u" ) ;
		try 
		{
			p.getMoney();
			fail () ;
		} 
		catch (WrongStateMethodCallException e) 
		{
			assertTrue ( true ) ;
		}
		try 
		{
			p.pay ( 3 ) ;
			fail () ;
		} 
		catch (TooFewMoneyException e)
		{
			fail () ;
		}
		catch (WrongStateMethodCallException e) 
		{
			assertTrue ( true ) ;
		}
		try
		{
			p.receiveMoney ( 30 ) ;
			assertTrue ( p.getMoney() == 30 ) ;
		}
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		}
		try 
		{
			p.pay ( 3 ) ;
		}
		catch (TooFewMoneyException e) 
		{
			fail () ;
		}
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		}
		try 
		{
			assertTrue ( p.getMoney() == 27 ) ;
		}
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		}
		try 
		{
			p.pay(400);
			fail () ;
		}
		catch (TooFewMoneyException e) 
		{
			try 
			{
				assertTrue ( p.getMoney()==27 );
			}
			catch (WrongStateMethodCallException e1) 
			{
				fail ();
			}
		}
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		}
	}
	
	@Test
	public void suspension () 
	{
		p = new DummyPlayer ( "2" ) ;
		assertTrue ( ! p.isSuspended() ) ;
		p.suspend();
		assertTrue ( p.isSuspended() ) ;
	}
	
	@Test
	public void equals () 
	{
		Player p1 = new DummyPlayer("1") ;
		Player p2 = new DummyPlayer("1") ;
		assertTrue ( p1.equals (  p2) ) ;
		p2 = new DummyPlayer("4");
		assertTrue ( ! p1.equals(p2) );
	}
	
	@Test
	public void sheperds ()
	{
		Sheperd [] shs ;
		shs = new Sheperd [ 2 ] ;
		p = new DummyPlayer ( "" ) ;
		shs [ 0 ] = new Sheperd( "1", new NamedColor ( 255 , 0 ,0,"red" ) , p );
		shs [ 1 ] = new Sheperd( "2", new NamedColor ( 255 , 0 ,0,"red" ) , p );
		try
		{
			p.initializeSheperds ( shs ) ;
			assertTrue ( p.getSheperds() != null ) ;
			assertTrue ( CollectionsUtilities.compareIterable ( p.getSheperds() , CollectionsUtilities.newIterableFromArray ( shs )  ) ) ;
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			fail () ;
		}
	}
	
}
