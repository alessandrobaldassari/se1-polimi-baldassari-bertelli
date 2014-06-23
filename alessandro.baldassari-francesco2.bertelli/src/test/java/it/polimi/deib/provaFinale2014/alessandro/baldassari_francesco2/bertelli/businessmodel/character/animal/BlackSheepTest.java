package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.*;

import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Before;
import org.junit.Test;

public class BlackSheepTest {

	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	AdultOvine blackSheep;
	BlackSheep blackSheepReal;
	AnimalFactory animalFactory ;
	GameMap map;
	ObjectIdentifier < Match > dummyMatchIdentifier;
	
	@Before
	public void setUpBeforeClass()
	{
		do 
		{
			try 
			{
				dummyMatchIdentifier = MatchIdentifier.newInstance();
				animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
				blackSheep = (AdultOvine) animalFactory.newBlackSheep();
				map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
				map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().addAnimal(blackSheep);
				blackSheepReal = (BlackSheep) map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getContainedAnimals().iterator().next();
				blackSheepReal.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
				blackSheepReal.escape();	
			}
			catch ( SingletonElementAlreadyGeneratedException e ) 
			{
				fail () ;
			}
			catch ( CharacterDoesntMoveException e ) 
			{}
		} 
		while ( animalFactory == null ) ;
	}
	
	@Test
	public void escape () 
	{
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
	
}