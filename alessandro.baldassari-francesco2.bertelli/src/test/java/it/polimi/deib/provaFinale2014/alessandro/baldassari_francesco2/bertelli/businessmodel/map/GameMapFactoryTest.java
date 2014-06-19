package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Before;
import org.junit.Test;

public class GameMapFactoryTest {

	GameMap gameMap;
	Identifiable<Match> requesterDummyMatchIdentifier;
	List<Region> regions;
	List<Road> roads;
	
	@Before
	public void setUp()  
	{
		requesterDummyMatchIdentifier = MatchIdentifier.newInstance();
		
	}

	@Test
	public void newIstance() {
		try 
		{
			gameMap = GameMapFactory.getInstance().newInstance(requesterDummyMatchIdentifier);
		}
		catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//testing correct amount of region by type
		for(RegionType regionType : RegionType.values()){
			regions = CollectionsUtilities.newListFromIterable( gameMap.getRegionByType(regionType) );
			if(regionType == RegionType.SHEEPSBURG)
				assertTrue(regions.size() == 1);
			else {
				assertTrue(regions.size() == 3);
			}
		}
		
		//testing correct amount of free roads and #roads
		roads = CollectionsUtilities.newListFromIterable( gameMap.getFreeRoads());
		assertTrue(roads.size() == 42);
	}		
}

