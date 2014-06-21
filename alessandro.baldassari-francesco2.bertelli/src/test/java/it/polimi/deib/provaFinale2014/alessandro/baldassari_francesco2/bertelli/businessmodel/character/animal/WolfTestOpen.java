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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test class test Wolf
 */
public class WolfTestOpen 
{
	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	 AnimalFactory animalFactory ;
	 GameMap map;
	 Wolf wolf;
	 ArrayList<Fence> fences = new ArrayList<Fence>();
	
	@Before
	public  void setUpBeforeClass() throws SingletonElementAlreadyGeneratedException
	{
		
			animalFactory = AnimalFactory.newAnimalFactory(MatchIdentifier.newInstance());
		
			wolf = (Wolf) animalFactory.newWolf();
		
			map = GameMapFactory.getInstance().newInstance(MatchIdentifier.newInstance());
			map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().addAnimal(wolf);
			wolf.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
	}
	
	/*
	 * Testing escape() method. No border roads occupied.
	 */
	@Test 
	public void escape() {
		LinkedList <Road> borderRoads;
		LinkedList <Region> borderRegions = new LinkedList<Region>();
		borderRoads = (LinkedList<Road>) map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getBorderRoads();
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
			System.out.println(" " + CollectionsUtilities.iterableSize ( region.getContainedAnimals() ) ) ;
			if ( CollectionsUtilities.iterableSize ( region.getContainedAnimals() ) > 0)
			{
					wolfIsInside = true;
					break;
			}
		}
		assertTrue(wolfIsInside);
		assertTrue ( CollectionsUtilities.iterableSize( map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getContainedAnimals() ) == 0);
	}
	
	@Test
	public void equalsT ()
	{
		assertFalse ( wolf.equals ( null ) ) ;
		assertFalse ( wolf.equals ( animalFactory.newAdultOvine(AdultOvineType.RAM) ) ) ;
		assertTrue ( wolf.equals ( wolf ) ) ;
	}
	
}
