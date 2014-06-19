package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

public class MoveSheepTest 
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
	public void ctor1 () 
	{
		MoveSheep m ;
		m = null ;
		try
		{
			m = new MoveSheep ( null , null , null ) ;
			fail () ;
		}
		catch ( IllegalArgumentException e ){
			assertTrue ( m == null ) ;
		} 
	}
	
	/**
	 * Test the Constructor working well 
	 */
	@Test
	public void ctor2 () 
	{
		MoveSheep m ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e1) 
		{
			fail () ;
		}
		catch (NoMoreCardOfThisTypeException e1 ) 
		{
			fail () ;
		}
		catch (WolfAlreadyGeneratedException e)
		{
			fail () ;
		} 
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
		try
		{
			m = new MoveSheep ( d.sheperds.get(0) , (Ovine) d.animals.get(5) , d.match.getGameMap().getRegionByUID(4) ) ; 
			assertTrue ( m != null ) ;
		} 
		catch ( IllegalArgumentException e )
		{
			fail () ;
		} 
	}
	
	/**
	 * Test the ovine is where the dest is.
	 */
	@Test
	public void execute1 () 
	{
		MoveSheep m ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e1) 
		{
			fail () ;
		}
		catch (NoMoreCardOfThisTypeException e1 ) 
		{
			fail () ;
		}
		catch (WolfAlreadyGeneratedException e)
		{
			fail () ;
		} 
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID( 4 ) ) ;
		d.match.getGameMap().getRoadByUID(4).setElementContained ( d.sheperds.get(0) ) ;
		d.animals.get(5).moveTo ( d.match.getGameMap().getRegionByUID ( 4 ) ) ;
		d.match.getGameMap().getRegionByUID(4).addAnimal ( d.animals.get(5) ) ; 
		try
		{
		
			m = new MoveSheep ( d.sheperds.get(0) , (Ovine) d.animals.get(5) , d.match.getGameMap().getRegionByUID(5) ) ; 
			m.execute ( d.match ) ;
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		} 
	}
	
	/**
	 * Test the Sheperd not near the ovine
	 */
	@Test
	public void execute2 () 
	{
		MoveSheep m ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e1) 
		{
			fail () ;
		}
		catch (NoMoreCardOfThisTypeException e1 ) 
		{
			fail () ;
		}
		catch (WolfAlreadyGeneratedException e)
		{
			fail () ;
		} 
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID( 4 ) ) ;
		d.match.getGameMap().getRoadByUID(4).setElementContained ( d.sheperds.get(0) ) ;
		d.animals.get(5).moveTo ( d.match.getGameMap().getRegionByUID ( 18 ) ) ;
		d.match.getGameMap().getRegionByUID(18).addAnimal ( d.animals.get(5) ) ; 
		d.match.getGameMap().getRegionByUID(5).addAnimal ( d.animals.get(0) );
		d.animals.get(0).moveTo(d.match.getGameMap().getRegionByUID ( 5 )); 
		try
		{
		
			m = new MoveSheep ( d.sheperds.get(0) , (Ovine) d.animals.get(5) , d.match.getGameMap().getRegionByUID(5) ) ; 
			m.execute ( d.match ) ;
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		} 
	}
	
	/**
	 * Test normal execution flow 
	 */
	@Test
	public void execute3 () 
	{
		MoveSheep m ;
		try 
		{
			d.initializePlayersAndSheperds();
			d.initializeAnimals();
		}
		catch (WriteOncePropertyAlreadSetException e1) 
		{
			fail () ;
		}
		catch (NoMoreCardOfThisTypeException e1 ) 
		{
			fail () ;
		}
		catch (WolfAlreadyGeneratedException e)
		{
			fail () ;
		} 
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID( 4 ) ) ;
		d.match.getGameMap().getRoadByUID(4).setElementContained ( d.sheperds.get(0) ) ;
		d.animals.get(5).moveTo ( d.match.getGameMap().getRegionByUID ( 5 ) ) ;
		d.match.getGameMap().getRegionByUID(5).addAnimal ( d.animals.get(5) ) ; 
		d.match.getGameMap().getRegionByUID(5).addAnimal ( d.animals.get(7) );
		d.animals.get(7).moveTo(d.match.getGameMap().getRegionByUID ( 5 )); 
		try
		{
			Region r0 ;
			r0 = d.match.getGameMap().getRegionByUID ( 5 ) ;
			m = new MoveSheep ( d.sheperds.get(0) , (Ovine) d.animals.get(5) , d.match.getGameMap().getRegionByUID(1) ) ; 
			m.execute ( d.match ) ;
			assertFalse ( CollectionsUtilities.newListFromIterable ( r0.getContainedAnimals() ).contains( d.animals.get(5) ) ) ; 
			assertTrue ( CollectionsUtilities.contains( d.match.getGameMap().getRegionByUID(1).getContainedAnimals() , d.animals.get(5) ) ) ;
		}
		catch (MoveNotAllowedException e) 
		{
			fail () ;
		} 
	}
	
}
