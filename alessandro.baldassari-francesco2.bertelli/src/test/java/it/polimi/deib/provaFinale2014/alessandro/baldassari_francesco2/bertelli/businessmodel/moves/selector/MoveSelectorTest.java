package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Before;
import org.junit.Test;

public class MoveSelectorTest 
{
	
	private DummyMatch d ;
	
	@Before
	public void setUp () 
	{
		d = new DummyMatch();
	}
	
	@Test ( expected = IllegalArgumentException.class )
	public void ctorExc () 
	{
		try 
		{
			new MoveSelector ( null ) ;
		}
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		}
	}
	
	@Test
	public void ctorOk () 
	{
		try 
		{
			d.initializePlayersAndSheperds();
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			e.printStackTrace();
		} catch (NoMoreCardOfThisTypeException e) 
		{
			e.printStackTrace();
		}
		MoveSelector s ;
		try 
		{
			s = new MoveSelector ( d.sheperds.get(0) ) ;
			assertTrue ( s != null ) ;

		} 
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		}
	}
	
	@Test
	public void infoSettersGetters () 
	{
		try 
		{
			d.initializePlayersAndSheperds();
			
		}
		catch (WriteOncePropertyAlreadSetException e) 
		{
			e.printStackTrace();
		} catch (NoMoreCardOfThisTypeException e) 
		{
			e.printStackTrace();
		}
		Collection < Region > bre ;
		Map < RegionType , Integer > m ;
		Collection < Region > mate;
		Collection < Region > moveS ;
		Collection < Road > moveSh ;
		MoveSelector s ;
		bre = new ArrayList < Region > () ;
		try
		{
			s = new MoveSelector ( d.sheperds.get ( 0 ) ) ;
			m = new HashMap < RegionType , Integer > () ;
			mate = new ArrayList<Region>();
			moveS = new ArrayList<Region>();
			moveSh = new ArrayList<Road>();
			bre.add ( d.match.getGameMap ().getRegionByUID ( 3 ) ) ;
			m.put ( RegionType.CULTIVABLE , 3 ) ;
			m.put ( RegionType.DESERT , 2 ) ;
			mate.add ( d.match.getGameMap().getRegionByUID ( 7 ) ) ;
			moveS.add( d.match.getGameMap().getRegionByUID ( 10 ) ) ;
			moveSh.addAll ( CollectionsUtilities.newCollectionFromIterable ( d.match.getGameMap().getFreeRoads () ) ) ;
			s.setAvailableMoney ( 3 ) ;
			s.setAvailableRegionsForBreakdown ( bre ) ;
			s.setAvailableRegionsForBuyCard ( m ) ;
			s.setAvailableRegionsForMate ( mate ) ;
			s.setAvailableRegionsForMoveSheep ( moveS );
			s.setAvailableRoadsForMoveSheperd ( moveSh ) ;
			assertTrue ( CollectionsUtilities.compareIterable ( s.getAvailableRegionsForBreakdown () , bre ) ) ;
			assertTrue ( CollectionsUtilities.compareIterable ( s.getAvailableRegionsForMate() , mate ) );
			assertTrue ( CollectionsUtilities.compareIterable ( s.getAvailableRegionsForMoveSheep () , moveS ) );
			assertTrue ( CollectionsUtilities.compareIterable ( s.getAvailableRoadsForMoveSheperd() , moveSh ) );
			assertTrue ( s.getAvailableRegionsForBuyCard().equals(m) );
		}
		catch (WrongStateMethodCallException e) 
		{
			fail () ;
		} 
		
	}
	
}
