package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

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
	
	@Test ( expected = MoveNotAllowedException.class )
	public void constructor1 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, MoveNotAllowedException 
	{
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 17 ) ) ;
	}
	
	/**
	 * Constructor has now to function well. 
	 * @throws NoMoreCardOfThisTypeException 
	 * @throws WriteOncePropertyAlreadSetException 
	 * @throws BlackSheepAlreadyGeneratedException 
	 * @throws WolfAlreadyGeneratedException 
	 * @throws MoveNotAllowedException 
	 */
	@Test
	public void constructor2 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, MoveNotAllowedException 
	{
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRoadByUID ( 2 ).setElementContained ( d.sheperds.get(0) ) ;
		d.match.getGameMap().getRegionByUID(1).addAnimal ( d.animals.get(3)); 
		d.match.getGameMap().getRegionByUID(1).addAnimal ( d.animals.get(8));  
		m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 1 ) ) ;
		assertTrue ( m != null ) ;
	}
	
	/**
	 * Test the precondition match != null 
	 */
	@Test ( expected = IllegalArgumentException.class )
	public void execute1 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, MoveNotAllowedException, WorkflowException 
	{
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRoadByUID ( 2 ).setElementContained ( d.sheperds.get(0) ) ;
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(3)); 
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(8));  
		m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
		m.execute(null);
	
	}
	
	
	/*
	 * Test the exception postcondition 1 sheperd not near the mate region
	*/
	@Test ( expected = MoveNotAllowedException.class )
	public void execute2 () throws WorkflowException, WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, MoveNotAllowedException 
	{
		d = new DummyMatch();
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID(34) ); 
		d.match.getGameMap().getRoadByUID(34).setElementContained(d.sheperds.get(0)); 
		m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
	}
	
	
	/**
	 * Test the precondition No ram, no sheep.
	*/
	@Test 
	public void execute3 () throws WorkflowException, WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, MoveNotAllowedException 
	{
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRoadByUID(2);
		
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(0) ) ;
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) );
		// no ram
		try
		{
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
		}
		catch ( MoveNotAllowedException e )
		{
			assertTrue ( m == null ) ;
		}
		// no sheep
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(0) );
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ; 
		try
		{
			m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;
		}
		catch ( MoveNotAllowedException e )
		{
			assertTrue ( m == null ) ;
		}
		// ok now.
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(7) );
		d.animals.get(7).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ; 
		m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;		
		assertTrue ( m != null ) ;
	}
	/**
	 * Test the exception postcondition 3 and the right flow
	 * @throws NoMoreCardOfThisTypeException 
	 * @throws WriteOncePropertyAlreadSetException 
	 * @throws BlackSheepAlreadyGeneratedException 
	 * @throws WolfAlreadyGeneratedException 
	 * @throws MoveNotAllowedException 
	 * @throws WorkflowException 
	 */
	@Test
	public void execute4 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, MoveNotAllowedException, WorkflowException 
	{
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRoadByUID(2);
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(0) ) ;
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) );
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(0) );
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ; 		
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(7) );
		d.animals.get(7).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ; 
		m = new Mate ( new DummyTurnNumberClock() , new DummyLambEvolver () , d.sheperds.get(0) , d.match.getGameMap ().getRegionByUID ( 2 ) ) ;		
		m.execute ( d.match ) ;
	}
	
	@Test
	public void canMateDueToSexReasons () throws WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException, WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException 
	{
		Mate m = null ;
		d.initializePlayersAndSheperds();
		d.initializeAnimals();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) );
		d.match.getGameMap().getRoadByUID(2);
		d.match.getGameMap().getRegionByUID ( 2 ).addAnimal ( d.animals.get(0) ) ;
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) );
		assertFalse ( Mate.canMateDueToSexReasons ( d.match.getGameMap().getRegionByUID(2) ) ) ;
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(0) );
		d.animals.get(0).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ; 		
		assertFalse ( Mate.canMateDueToSexReasons ( d.match.getGameMap().getRegionByUID(2) ) ) ;
		d.match.getGameMap().getRegionByUID(2).addAnimal ( d.animals.get(7) );
		d.animals.get(7).moveTo ( d.match.getGameMap().getRegionByUID(2) ) ; 
		
	}
	
	class DummyTurnNumberClock implements TurnNumberClock 
	{

		@Override
		public int getTurnNumber()  {
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
