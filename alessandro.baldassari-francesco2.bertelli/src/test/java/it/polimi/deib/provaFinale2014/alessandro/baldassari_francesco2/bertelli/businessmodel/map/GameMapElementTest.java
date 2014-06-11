package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;

import org.junit.Test;

public class GameMapElementTest {

	Region region1 = new Region(RegionType.HILL, 1);
	Region region2 = new Region(RegionType.CULTIVABLE, 2);
	Road road1 = new Road(1,1,region1,region2);
	Road road2 = new Road(2,2,region1,region2);
	
	@Test
	public void gameMapElementType() {
		assertTrue(region1.getGameMapElementType() == GameMapElementType.REGION);
		assertTrue(road1.getGameMapElementType() == GameMapElementType.ROAD);
	}
	
	@Test
	public void getUID(){
		assertTrue(region2.getUID() == 2);
		assertTrue(road1.getUID() == 1);
	}
	
	@Test
	public void equals(){
		assertTrue(region1.equals(region1));
		assertFalse(region1.equals(region2));
		assertTrue(road1.equals(road1));
	}

}
