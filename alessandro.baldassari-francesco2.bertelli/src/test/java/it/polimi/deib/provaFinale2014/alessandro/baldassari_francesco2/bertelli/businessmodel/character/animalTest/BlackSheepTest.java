package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animalTest;

import static org.junit.Assert.*;

import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

public class BlackSheepTest {

	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	static AdultOvine blackSheep;
	static BlackSheep blackSheepReal;
	static AnimalFactory animalFactory ;
	static GameMap map;
	static DummyMatchIdentifier dummyMatchIdentifier;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		dummyMatchIdentifier = new DummyMatchIdentifier();
		try {
			animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		try {
			blackSheep = (AdultOvine) animalFactory.newBlackSheep();
		} catch (BlackSheepAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		try {
			map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
			
			map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().addAnimal(blackSheep);
			blackSheepReal = (BlackSheep) map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getContainedAnimals().iterator().next();
			blackSheepReal.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
			try {
				blackSheepReal.escape();
			} catch (CharacterDoesntMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void escape() {
		LinkedList <Road> borderRoads;
		LinkedList <Region> borderRegions = new LinkedList<Region>();
		boolean blackSheepIsInside = false;
		borderRoads = (LinkedList<Road>) map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getBorderRoads();
		for(Road road : borderRoads){
			System.out.println(road.toString());
			if(road.getFirstBorderRegion().getType() != RegionType.SHEEPSBURG)
				borderRegions.add(road.getFirstBorderRegion());
			else
				borderRegions.add(road.getSecondBorderRegion());
		}
		for(Region region : borderRegions){
			System.out.print(region.toString());
			System.out.println(" " + CollectionsUtilities.iterableSize( region.getContainedAnimals() ) ) ;
			if( CollectionsUtilities.iterableSize ( region.getContainedAnimals() ) > 0)
			{
					blackSheepIsInside = true;
					break;
			}
		}
		assertTrue(blackSheepIsInside);
		assertTrue( CollectionsUtilities.iterableSize( map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getContainedAnimals() ) == 0);
			
	}
	
	static class DummyMatchIdentifier implements Identifiable<Match>{

		public DummyMatchIdentifier() {
			// TODO Auto-generated constructor stub
		}

		public boolean isEqualsTo(Identifiable<Match> otherObject) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	
}