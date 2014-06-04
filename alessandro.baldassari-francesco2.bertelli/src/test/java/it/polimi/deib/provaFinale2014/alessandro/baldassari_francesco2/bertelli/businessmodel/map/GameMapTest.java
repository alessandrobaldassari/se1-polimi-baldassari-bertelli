package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class GameMapTest {

	GameMap gameMap;
	Couple<Region, int []> region1Couple;
	Couple<Region, int []> region2Couple;
	Couple<Road, int []> roadCouple;
	Map <Integer, Couple <Region, int []> > regionsMap;
	Map <Integer, Couple <Road, int []> > roadsMap;
	ArrayList<Road> adjacentRoadsArrayList;
	ArrayList<Road> roadArrayList;
	final static int [] regionArray = {1};
	final static int [] roadArray = {1, 2};
	Region firstBorderRegion;
	Region secondBorderRegion;
	Road road;
	
	@Before
	public void setUp(){
		
		
		regionsMap = new HashMap <Integer, Couple <Region, int[]> > ();
		roadsMap = new HashMap <Integer, Couple <Road, int []> > ();
		adjacentRoadsArrayList = new ArrayList<Road>();
		roadArrayList = new ArrayList<Road>();
		
		firstBorderRegion = new Region(RegionType.HILL, 1);
		secondBorderRegion = new Region(RegionType.DESERT, 2);
		road = new Road(1, 1, firstBorderRegion, secondBorderRegion);
		
		
		roadArrayList.add(road);
		adjacentRoadsArrayList.add(road);
		
		firstBorderRegion.setBorderRoads(roadArrayList);
		secondBorderRegion.setBorderRoads(roadArrayList);
	
		region1Couple = new Couple<Region, int []>(firstBorderRegion, regionArray);
		region2Couple = new Couple<Region, int []>(secondBorderRegion, regionArray);
		roadCouple = new Couple<Road, int[]>(road, roadArray);
		regionsMap.put(1, region1Couple);
		regionsMap.put(2, region2Couple);
		roadsMap.put(1, roadCouple);
		gameMap = new GameMap(regionsMap, roadsMap);
	}
	
	@Test
	public void getRegions() {
		ArrayList <Region> regions;
		regions = (ArrayList<Region>) gameMap.getRegions();
		assertTrue(regions.get(0).equals(firstBorderRegion));
		assertTrue(regions.get(1).equals(secondBorderRegion));
		
	}
	
	@Test
	public void getRegionByUID(){
		Region res;
		res = gameMap.getRegionByUID(1);
		assertTrue(res.getUID() == 1);
	}
	
	@Test
	public void getRegionByType(){
		ArrayList <Region> regions;
		regions = (ArrayList<Region>) gameMap.getRegionByType(RegionType.HILL);
		assertTrue(regions.get(0).getType() == RegionType.HILL);
		assertTrue(regions.size() == 1);
		regions = (ArrayList<Region>) gameMap.getRegionByType(RegionType.MOUNTAIN);
		assertTrue(regions.size() == 0);
	}
	
	@Test
	public void getRoadByUID(){
		Road road;
		road = gameMap.getRoadByUID(1);
		assertTrue(road.getUID() == 1);
	}
	
	@Test
	public void getFreeRoads(){
		ArrayList <Road> freeRoads;
		freeRoads = (ArrayList<Road>) gameMap.getFreeRoads();
		assertTrue(freeRoads.size() == 1);
		assertTrue(freeRoads.get(0).getUID() == 1);
		
		
	}
}
