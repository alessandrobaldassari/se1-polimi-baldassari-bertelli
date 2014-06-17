package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import org.junit.Before;
import org.junit.Test;

public class MateTest 
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
		Mate m = null ;
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
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		try
		{
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 17 ) ) ;
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( m == null ) ;
		}
	}
	
	/**
	 * Constructor has now to function well. 
	 */
	@Test
	public void constructor2 () 
	{
		Mate m = null ;
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
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 1 ) ) ;
			assertTrue ( m != null ) ;
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
		Mate m = null ;
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
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
			m.execute(null);
		}
		catch (IllegalArgumentException e) 
		{
			assertTrue ( true ) ;
		} 
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		}
	}
	
	/**
	 * Test the exception postcondition 1 - no ram
	 */
	@Test 
	public void execute2 () 
	{
		Mate m = null ;
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
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(0) ) ;
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ;
		try
		{
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
			m.execute ( d.match );
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		}
	}
	
	/**
	 * Test the precondition match != null - yer ram, no sheep.
	 */
	@Test 
	public void execute3 () 
	{
		Mate m = null ;
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
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(2) ) ;
		
		d.animals.get(2).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ;
		d.match.getGameMap ().getRegionByUID ( 2 ).addAnimal ( d.animals.get(2) ) ;
		
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID ( 2 ) );
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(0) );
		
		try
		{
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
			m.execute(d.match);
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		}
	}
	
	/**
	 * Test the exception postcondition 3 and the right flow
	 */
	@Test
	public void execute4 () 
	{
		Mate m = null ;
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
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(2) ) ;
		
		d.animals.get(2).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ;
		d.match.getGameMap ().getRegionByUID ( 2 ).addAnimal ( d.animals.get(2) ) ;
		
		d.animals.get ( 8 ).moveTo ( d.match.getGameMap().getRegionByUID ( 2 ) );
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(8) );
		List < Animal > iniAn ;
		List afterAn ;
		iniAn = CollectionsUtilities.newListFromIterable ( d.match.getGameMap().getRegionByUID(2).getContainedAnimals() ) ;
		try
		{
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
			m.execute(d.match);
			afterAn = CollectionsUtilities.newListFromIterable (  d.match.getGameMap().getRegionByUID ( 2 ).getContainedAnimals() )  ;
			assertTrue ( afterAn.size() == iniAn.size() + 1 );
			afterAn.removeAll ( iniAn ) ;
			assertTrue ( afterAn.size() == 1 ) ;
			assertTrue ( afterAn.get(0) instanceof Lamb ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( iniAn.equals ( CollectionsUtilities.newListFromIterable ( d.match.getGameMap().getRegionByUID(2).getContainedAnimals() ) ) ) ;
		}
	}
	
	class DummyTurnNumberClock implements TurnNumberClock 
	{

		@Override
		public int getTurnNumber() throws WrongMatchStateMethodCallException {
			return 0;
		}
		
	}
	
	class DummyLambEvolver implements LambEvolver 
	{

		@Override
		public void evolve (Lamb lamb) 
		{}
		
	}
	
}
