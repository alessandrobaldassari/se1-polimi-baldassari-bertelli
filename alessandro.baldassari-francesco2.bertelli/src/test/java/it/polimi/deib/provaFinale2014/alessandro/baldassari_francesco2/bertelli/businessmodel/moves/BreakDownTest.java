package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import org.junit.Before;
import org.junit.Test;

public class BreakDownTest 
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
		BreakDown b = null ;
		Animal a ;
		Sheperd k ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
		a = d.animals.get ( 0 );
		k = d.sheperds.get ( 0 ) ;
		a.moveTo ( d.match.getGameMap().getRegionByUID ( 4 ) ) ;
		d.match.getGameMap().getRegionByUID ( 4 ).addAnimal ( a ) ; 
		k.moveTo ( d.match.getGameMap().getRoadByUID ( 42 ) ) ;
		d.match.getGameMap().getRoadByUID(42).setElementContained ( k ) ;
		try {
			b = new BreakDown ( k , a ) ;
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
		BreakDown b ;
		Animal a ;
		Sheperd k ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
		a = d.animals.get ( 0 );
		k = d.sheperds.get ( 0 ) ;
		a.moveTo ( d.match.getGameMap().getRegionByUID ( 4 ) ) ;
		d.match.getGameMap().getRegionByUID ( 4 ).addAnimal ( a ) ; 
		k.moveTo ( d.match.getGameMap().getRoadByUID ( 8 ) ) ;
		d.match.getGameMap().getRoadByUID ( 8 ).setElementContained ( k ) ;
		try {
			b = new BreakDown ( k , a ) ;
			assertTrue ( b != null ) ;
		} catch (MoveNotAllowedException e) {
			fail () ;
		}
	}
	
	/**
	 * Test the precondition match != null 
	 */
	@Test 
	public void execute1 () 
	{
		BreakDown b = null ;
		Animal a ;
		Sheperd k ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
		a = d.animals.get ( 0 );
		k = d.sheperds.get ( 0 ) ;
		a.moveTo ( d.match.getGameMap().getRegionByUID ( 4 ) ) ;
		d.match.getGameMap().getRegionByUID ( 4 ).addAnimal ( a ) ; 
		k.moveTo ( d.match.getGameMap().getRoadByUID ( 8 ) ) ;
		d.match.getGameMap().getRoadByUID ( 8 ).setElementContained ( k ) ;
		try 
		{
			b = new BreakDown ( k , a ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
		try
		{
			b.execute(null);
			fail () ;
		}
		catch ( IllegalArgumentException e )
		{
			assertTrue ( true ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
	}
	
	/**
	 * Test the exception postcondition
	 */
	@Test 
	public void execute2 () 
	{
		BreakDown b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.animals.get(0).moveTo(d.match.getGameMap().getRegionByUID(2));
		d.match.getGameMap().getRegionByUID(2).addAnimal(d.animals.get(0));
		
		d.sheperds.get(0).moveTo(d.match.getGameMap().getRoadByUID(5));
		d.match.getGameMap().getRoadByUID(5).setElementContained(d.sheperds.get(0));
		
		d.sheperds.get(1).moveTo(d.match.getGameMap().getRoadByUID(2));
		d.match.getGameMap().getRoadByUID(2).setElementContained(d.sheperds.get(1));
		
		d.sheperds.get(2).moveTo(d.match.getGameMap().getRoadByUID(6));
		d.match.getGameMap().getRoadByUID(6).setElementContained(d.sheperds.get(2));
		try 
		{
			d.sheperds.get(0).getOwner().pay(4);
		}
		catch (TooFewMoneyException e1) 
		{
			fail () ;
		}
		try 
		{
			b = new BreakDown ( d.sheperds.get(0) , d.animals.get(0) ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
		try
		{
			b.execute ( d.match ) ;
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		}
	}
	
	/**
	 * Test right post conditions with all the adjacent sheperds in the process payed
	 */
	@Test 
	public void execute3 () 
	{
		BreakDown b = null ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (NoMoreCardOfThisTypeException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
		
		d.match.getGameMap().getRegionByUID(2).removeAnimal(d.animals.get(0));

		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(0) );
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) );
		
		d.animals.get(4).moveTo(d.match.getGameMap().getRegionByUID(2));
		d.match.getGameMap().getRegionByUID(2).addAnimal(d.animals.get(4));
		
		d.sheperds.get(0).moveTo(d.match.getGameMap().getRoadByUID(5));
		d.match.getGameMap().getRoadByUID(5).setElementContained(d.sheperds.get(0));
		
		d.sheperds.get(1).moveTo(d.match.getGameMap().getRoadByUID(2));
		d.match.getGameMap().getRoadByUID(2).setElementContained(d.sheperds.get(1));
		
		d.sheperds.get(2).moveTo(d.match.getGameMap().getRoadByUID(6));
		d.match.getGameMap().getRoadByUID(6).setElementContained(d.sheperds.get(2));
		try 
		{
			b = new BreakDown ( d.sheperds.get(0) , d.animals.get ( 0 ) ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
		int [] initMon = new int [ 3 ] ;
		try
		{
			for ( byte i = 0 ; i < d.sheperds.size () ; i ++ )
				initMon [ i ] = d.sheperds.get(i).getOwner().getMoney();
			b.execute ( d.match ) ;
			assertTrue ( initMon [ 0 ] == d.sheperds.get(0).getOwner().getMoney() + 2 * BreakDown.AMOUNT_TO_PAY_FOR_SILENCE ) ;
			assertTrue ( initMon [ 1 ] == d.sheperds.get(1).getOwner().getMoney() - BreakDown.AMOUNT_TO_PAY_FOR_SILENCE ) ;
			assertTrue ( initMon [ 2 ] == d.sheperds.get(2).getOwner().getMoney() - BreakDown.AMOUNT_TO_PAY_FOR_SILENCE ) ;
			assertTrue ( d.animals.get(0).getPosition() == null ) ;
			assertFalse ( CollectionsUtilities.contains( d.match.getGameMap().getRegionByUID(2).getContainedAnimals() , d.animals.get(0) ) ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
	}
	
}
