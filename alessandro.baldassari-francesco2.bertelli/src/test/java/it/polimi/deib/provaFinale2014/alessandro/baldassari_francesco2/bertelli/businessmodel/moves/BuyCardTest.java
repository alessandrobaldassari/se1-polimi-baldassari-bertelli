package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import org.junit.Before;
import org.junit.Test;

public class BuyCardTest 
{

	private RegionType regionCardType ;
	
	private Sheperd buyer ;
	
	private Match match ;
	
	private BuyCard instance ;
	
	@Before
	public void setUp () 
	{
		DummyMatch d ;
		try 
		{
			d = new DummyMatch () ;
			match = d.match ;
			//buyer = d.sheperds [ 2 ] ;
			regionCardType = RegionType.values() [ MathUtilities.random ( 0 , RegionType.values().length - 1 ) ] ;
			instance = new BuyCard ( buyer , regionCardType ) ;
		
		}
		catch (SingletonElementAlreadyGeneratedException e) {
			fail () ;
			e.printStackTrace();
		}
		catch (WriteOncePropertyAlreadSetException e)
		{
			fail () ;
			e.printStackTrace();
		}
		catch (NoMoreCardOfThisTypeException e) 
		{
			fail () ;
			e.printStackTrace();
		}
	}
	
	@Test
	public void test () 
	{
		int p0 ;
		try 
		{
			p0 = match.getBank ().getPeekCardPrice ( regionCardType ) ;
			instance.execute ( match ) ;
		}
		catch (NoMoreCardOfThisTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MoveNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
