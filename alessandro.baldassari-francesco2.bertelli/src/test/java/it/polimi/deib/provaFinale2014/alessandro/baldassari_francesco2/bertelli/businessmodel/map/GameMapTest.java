package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class GameMapTest 
{

	GameMap gameMap;
	Couple<Region, int []> region1Couple;
	Couple<Region, int []> region2Couple;
	Couple<Road, int []> roadCouple;
	Map <Integer, Couple <Region, int []> > regionsMap;
	Map <Integer, Couple <Road, int []> > roadsMap;
	ArrayList<Road> adjacentRoadsArrayList;
	ArrayList<Road> roadArrayList;
	final static int [] regionArray = {1};
	final static int [] roadArray = {};
	Region firstBorderRegion;
	Region secondBorderRegion;
	Road road;
	
	@Before
	public void setUp()
	{
		
		
		regionsMap = new HashMap <Integer, Couple <Region, int[]> > ();
		roadsMap = new HashMap <Integer, Couple <Road, int []> > ();
		adjacentRoadsArrayList = new ArrayList<Road>();
		roadArrayList = new ArrayList<Road>();
		
		firstBorderRegion = new Region(RegionType.HILL, 1);
		secondBorderRegion = new Region(RegionType.DESERT, 2);
		road = new Road(1, 1, firstBorderRegion, secondBorderRegion);
		
		
		roadArrayList.add(road);
		adjacentRoadsArrayList.add ( road ) ;
		
		firstBorderRegion.setBorderRoads(roadArrayList);
		secondBorderRegion.setBorderRoads(roadArrayList);
	
		region1Couple = new Couple<Region, int []> ( firstBorderRegion, regionArray ) ;
		region2Couple = new Couple<Region, int []> ( secondBorderRegion, regionArray ) ;
		roadCouple = new Couple < Road, int[]> ( road , roadArray ) ;
		regionsMap.put(1, region1Couple);
		regionsMap.put(2, region2Couple);
		roadsMap.put(1, roadCouple);
		gameMap = new GameMap(regionsMap, roadsMap);
	}
	
	@Test
	public void getRegions() {
		List <Region> regions;
		regions = CollectionsUtilities.newListFromIterable ( gameMap.getRegions () ) ;
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
		List <Region> regions;
		regions = (List<Region>) gameMap.getRegionByType ( RegionType.HILL ) ;
		assertTrue(regions.get(0).getType() == RegionType.HILL);
		assertTrue(regions.size() == 1);
		regions = (List<Region>) gameMap.getRegionByType(RegionType.MOUNTAIN);
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
		List <Road> freeRoads;
		freeRoads = (List<Road>) gameMap.getFreeRoads();
		assertTrue(freeRoads.size() == 1);
		assertTrue(freeRoads.get(0).getUID() == 1);
	}
	
	@Test
	public void observer () throws WolfAlreadyGeneratedException, SingletonElementAlreadyGeneratedException 
	{
		DummyMapObserver d = new DummyMapObserver() ;
		gameMap.getRegionByUID (1).addAnimal ( AnimalFactory.newAnimalFactory(MatchIdentifier.newInstance()).newAdultOvine(AdultOvineType.RAM) ); 
		gameMap.addObserver(d);
		assertTrue ( d.n == 1 ) ;
		Iterable < Animal > i = CollectionsUtilities.newCollectionFromIterable(gameMap.getRegionByUID(1).getContainedAnimals());
		for ( Animal a : i )
			gameMap.getRegionByUID(1).removeAnimal(a);
		assertTrue ( d.n == 0 ) ;
		gameMap.getRegionByUID(1).addAnimal ( AnimalFactory.newAnimalFactory(MatchIdentifier.newInstance()).newWolf() );
		assertTrue ( d.n == 1 ) ;
	}
	
	public class DummyMapObserver implements GameMapObserver 
	{

		public int n ;
		
		public DummyMapObserver() {
			n = 0 ;
		}
		
		@Override
		public void onPositionableElementAdded(GameMapElementType whereType,
				Integer whereId, PositionableElementType whoType, Integer whoId) {
			n ++ ;
		}

		@Override
		public void onPositionableElementRemoved(GameMapElementType whereType,
				Integer whereId, PositionableElementType whoType, Integer whoId) {
			n -- ;
		}
		
		
		
	}
	
}
