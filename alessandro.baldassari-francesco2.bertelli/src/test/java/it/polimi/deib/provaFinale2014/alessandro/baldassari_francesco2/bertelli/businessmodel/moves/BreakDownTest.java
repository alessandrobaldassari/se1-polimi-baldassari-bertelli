package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BreakDownTest 
{

	private static DummyMatch d ;
	
	@BeforeClass
	public static void setUp ()
	{
		try 
		{
			d = new DummyMatch () ;
		}
		catch ( SingletonElementAlreadyGeneratedException e ) 
		{
			fail () ;
			e.printStackTrace () ;
		}
		catch ( WriteOncePropertyAlreadSetException e ) 
		{ 
			fail () ;
			e.printStackTrace();
		} 
		catch ( NoMoreCardOfThisTypeException e ) 
		{
			fail () ;
			e.printStackTrace();
		}
	}
	
	@Test ( expected = CannotDoThisMoveException.class )
	public void constructor1 () throws CannotDoThisMoveException 
	{
		Animal a ;
		Sheperd k ;
		boolean cond ;
		boolean exec ;
		a = d.animals.get ( 0 ) ;
		k = d.sheperds.get ( 0 ) ;
		cond = ( k.getPosition ().getFirstBorderRegion ().equals ( a.getPosition() ) ) || ( k.getPosition ().getSecondBorderRegion ().equals ( a.getPosition() ) ) ;
		new BreakDown ( k , a ) ;
		assertTrue ( cond ) ;
	}
	/*
	@Test 
	public void execute () 
	{
		try 
		{
			Region targetRegion ;
			Road killerRoad ;
			int initMoney ;
			boolean c1 ;
			boolean c2 ;
			//targetRegion = target.getPosition () ;
			//killerRoad = killer.getPosition () ;
			//initMoney = killer.getOwner ().getMoney () ;
			//instance.execute ( match ) ;
			System.out.println ( targetRegion ) ;
			System.out.println ( killerRoad.getFirstBorderRegion() ) ;
			System.out.println ( killerRoad.getSecondBorderRegion() ) ;
			//System.out.println ( target.getPosition() ) ;
			//c1 = ( target.getPosition () == null ) && ( killerRoad.getFirstBorderRegion ().equals ( targetRegion ) || killerRoad.getSecondBorderRegion ().equals ( targetRegion ) ) ;
			//c2 = target.getPosition() != null  && ( ! ( killerRoad.getFirstBorderRegion().equals ( targetRegion ) ) && ! ( killerRoad.getSecondBorderRegion ().equals ( targetRegion ) ) ) ;
			assertTrue ( ( c1 && !c2 ) || ( !c1 && c2 ) ) ;
			//assertTrue ( initMoney >= killer.getOwner ().getMoney() );
		}
		catch ( MoveNotAllowedException e ) 
		{
			e.printStackTrace();
			fail () ;
		}
		
	}
	*/
}
