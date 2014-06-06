package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test class test Wolf
 */
public class WolfTestClosed {

	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	static AnimalFactory animalFactory ;
	static GameMap map;
	static DummyMatchIdentifier dummyMatchIdentifier;
	static Wolf wolf;
	static ArrayList<Fence> fences = new ArrayList<Fence>();
	
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
			wolf = (Wolf) animalFactory.newWolf();
		} catch (WolfAlreadyGeneratedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
			map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().addAnimal(wolf);
			wolf.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
		} catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	/*
	 * Testing escape() method. All border roads occupied.
	 */
	@Test 
	public void escape() {
		LinkedList <Road> borderRoads;
		LinkedList <Region> borderRegions = new LinkedList<Region>();
		for(int i=0; i<6; i++)
			fences.add(new Fence(FenceType.NON_FINAL));
		borderRoads = (LinkedList<Road>) map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getBorderRoads();
		for(Road road : borderRoads)
			road.setElementContained(fences.remove(0));
		try {
			wolf.escape();
		} catch (CharacterDoesntMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean wolfIsInside = false;
		
		for(Road road : borderRoads){
			System.out.println(road.toString());
			if(road.getFirstBorderRegion().getType() != RegionType.SHEEPSBURG)
				borderRegions.add(road.getFirstBorderRegion());
			else
				borderRegions.add(road.getSecondBorderRegion());
		}
		for(Region region : borderRegions){
			System.out.print(region.toString());
			if ( CollectionsUtilities.iterableSize( region.getContainedAnimals() ) > 0){
					wolfIsInside = true;
					break;
			}
		}
		assertTrue(wolfIsInside);
		assertTrue ( CollectionsUtilities.iterableSize( map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getContainedAnimals() ) == 0);
	}	
	
	/*
	 * Declaring a dummy MatchIdentifier to initialize animalFactory correctly
	 */
	public static class DummyMatchIdentifier implements Identifiable<Match>{

		public DummyMatchIdentifier() {
			// TODO Auto-generated constructor stub
		}

		public boolean isEqualsTo(Identifiable<Match> otherObject) {
			// TODO Auto-generated method stub
			return true;
		}
	}
}
