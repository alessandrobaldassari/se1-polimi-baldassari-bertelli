package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Before;
import org.junit.Test;

public class BuyCardTest 
{

private DummyMatch d ;
	
	@Before
	public void setUp ()
	{
		d = new DummyMatch () ;
	}
	
	/**
	 * Test the Exception launched by the constructor 
	 */
	@Test 
	public void constructor1 () 
	{
		BuyCard b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try
		{
			b = new BuyCard ( d.sheperds.get(0) , RegionType.LACUSTRINE ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( b == null ) ;
		}
	}
	
	/**
	 * Constructor has now to function well. 
	 */
	@Test
	public void constructor2 () 
	{
		BuyCard b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try
		{
			b = new BuyCard ( d.sheperds.get(0) , RegionType.CULTIVABLE ) ;
			assertTrue ( b != null ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
	}
	
	/**
	 * Test the precondition match != null 
	 */
	@Test 
	public void execute1 () 
	{
		BuyCard b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try
		{
			b = new BuyCard ( d.sheperds.get(0) , RegionType.LACUSTRINE ) ;
			b.execute(null);
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		} 
		catch (WorkflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the exception postcondition 1
	 */
	@Test 
	public void execute2 () 
	{
		BuyCard b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try
		{
			b = new BuyCard ( d.sheperds.get(0) , RegionType.LACUSTRINE ) ;
			boolean x = false ;
			do
			{
				try 
				{
					d.bank.sellACard ( d.bank.getPeekCardPrice(RegionType.LACUSTRINE ) , RegionType.LACUSTRINE ) ;
				} 
				catch (CardPriceNotRightException e) 
				{
					fail () ;
				} 
				catch (NoMoreCardOfThisTypeException e) 
				{
					x = true ;
				}
			}
			while ( x == false ) ;
			b.execute(d.match);
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		} 
		catch (WorkflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the precondition match != null 
	 */
	@Test 
	public void execute3 () 
	{
		BuyCard b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try
		{
			try 
			{
				d.bank.sellACard ( d.bank.getPeekCardPrice(RegionType.LACUSTRINE ) , RegionType.LACUSTRINE ) ;
				d.bank.sellACard ( d.bank.getPeekCardPrice(RegionType.LACUSTRINE ) , RegionType.LACUSTRINE ) ;
				d.bank.sellACard ( d.bank.getPeekCardPrice(RegionType.LACUSTRINE ) , RegionType.LACUSTRINE ) ;
			} 
			catch (CardPriceNotRightException e) 
			{
				fail () ;
			} 
			catch (NoMoreCardOfThisTypeException e) 
			{
				fail () ;
			}
			try {
				d.players.get(0).pay ( 4 ) ;
			} catch (TooFewMoneyException e) {
				fail () ;
			} catch (WrongStateMethodCallException e) {
				fail () ;
			}
			b = new BuyCard ( d.sheperds.get(0) , RegionType.LACUSTRINE ) ;
			b.execute(d.match);
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		} 
		catch (WorkflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the normal post. 
	 */
	@Test
	public void execute4 () 
	{
		BuyCard b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try 
		{
			d.bank.sellACard ( d.bank.getPeekCardPrice(RegionType.CULTIVABLE ) , RegionType.CULTIVABLE ) ;
			d.bank.sellACard ( d.bank.getPeekCardPrice(RegionType.CULTIVABLE ) , RegionType.CULTIVABLE ) ;
		} 
		catch (CardPriceNotRightException e) 
		{
			fail () ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			fail () ;
		}
		try 
		{
			int bankPeekPr ;
			int minit = d.players.get(0).getMoney();
			int nc0 = CollectionsUtilities.iterableSize( d.players.get(0).getSellableCards() );
			bankPeekPr = d.match.getBank().getPeekCardPrice(RegionType.CULTIVABLE);
			b = new BuyCard ( d.sheperds.get(0) , RegionType.CULTIVABLE ) ;
			b.execute ( d.match ) ;
			assertTrue ( 1 + bankPeekPr == d.match.getBank().getPeekCardPrice(RegionType.CULTIVABLE) ) ;
			assertTrue ( 1 + nc0 == CollectionsUtilities.iterableSize( d.players.get(0).getSellableCards() ) );
			assertTrue ( d.players.get ( 0 ).getMoney() == minit - bankPeekPr ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			fail () ;
		} catch (WrongStateMethodCallException e) {
			fail () ;
		} catch (WorkflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

