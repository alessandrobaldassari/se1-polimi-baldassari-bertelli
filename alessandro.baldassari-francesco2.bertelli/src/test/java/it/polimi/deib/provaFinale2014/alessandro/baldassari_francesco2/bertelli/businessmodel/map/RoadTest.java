package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import org.junit.Before;
import org.junit.Test;

public class RoadTest 
{

	private Road r ;
	
	private Region r1 ;

	private Region r2 ;
	
	@Before
	public void setUp () 
	{
		r1 = new Region ( RegionType.CULTIVABLE , 3 ) ;
		r2 = new Region ( RegionType.DESERT , 2 ) ;
	}
	
	@Test
	public void ctor () 
	{
		try 
		{
			r = new Road ( 0 , 10 , null , null ) ;
			fail () ;
		}
		catch ( IllegalArgumentException e ) 
		{
			assertTrue ( r == null ) ;
		}
	}
	
	@Test
	public void getNumber () 
	{
		r = new Road ( 3 , 4 , r1 , r2 ) ;
		assertTrue ( r.getNumber() == 3 ) ;
	}
	
	@Test
	public void setGetAdjacentRoad () 
	{
		Iterable < Road > res ;
		List < Road > ad = CollectionsUtilities.newListFromIterable( CollectionsUtilities.newIterableFromArray ( new Road [] { new Road ( 4 , 5 , r1 , r2 ) , new Road ( 5 , 6 , r1 , r2 ) } ) ) ; 
		r = new Road ( 3 , 4 , r1 , r2 ) ;
		r.setAdjacentRoads ( ad );
		res = r.getAdjacentRoads();
		int i = 0 ;
		assertTrue ( ad.size () == CollectionsUtilities.iterableSize ( res ) ) ;
		for ( Road ro : res )
		{
			assertTrue ( ro.equals ( ad.get(i) ) ) ;
			i++ ;
		}
	}
	
	@Test
	public void borderRegions () 
	{
		r = new Road ( 3 , 4 , r1 , r2 ) ;
		assertTrue( r.getFirstBorderRegion().equals(r1) ); 
		assertTrue ( r.getSecondBorderRegion().equals(r2) ) ;
	}
	
	@Test
	public void setGetElementContained () 
	{
		Sheperd p = new Sheperd ( "" , new NamedColor ( 2550 , 0 , 0 , "red" ), new DummyPlayer ( "" ) ) ;
		r = new Road ( 3 , 4 , r1 , r2 ) ;
		assertTrue ( r.getElementContained() == null ) ;
		r.setElementContained ( p ) ;
		assertTrue ( r.getElementContained().equals(p) ) ;
	}
	
	@Test
	public void equals () 
	{
		r = new Road ( 3 , 4 , r1 , r2 ) ;
		Road t1 = new Road ( 3 , 5 , r1 , r2 ) ;
		assertFalse ( t1.equals ( r ) ) ;
		t1 = new Road ( 3 , 4 , r1 , r2 );
		assertTrue ( t1.equals(r) ) ;
	}
	
}
