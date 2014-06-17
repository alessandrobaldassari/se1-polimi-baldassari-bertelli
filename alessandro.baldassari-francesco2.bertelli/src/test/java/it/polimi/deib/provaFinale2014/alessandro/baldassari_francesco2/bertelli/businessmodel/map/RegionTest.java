package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.NoRoadWithThisNumberException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceAlreadyPlacedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

public class RegionTest {
	
	Region region, borderRegion;
	Road road;
	ArrayList <Road> borderRoads;
	Ovine sheep;
	Fence fence;
 	
	@Before
	public void setUp()
 	{
		region = new Region(RegionType.CULTIVABLE, 1);
		borderRegion = new Region(RegionType.LACUSTRINE, 2);
		road = new Road(1, 1, region, borderRegion);
		borderRoads = new ArrayList<Road>();
		borderRoads.add(road);
		fence = new Fence(FenceType.NON_FINAL);
		sheep = new Sheep ( "t1" );
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void Region() {
		region = new Region(RegionType.HILL, 1);
		assertTrue(region.getType() == RegionType.HILL);
		assertTrue(region.getUID() == 1);
		region = new Region(null, 1);
	}
	
	@Test
	public void regionType(){
		region = new Region(RegionType.DESERT, 1);
		assertTrue(region.getType() == RegionType.DESERT);
	}
	
	@Test
	public void getBorderRoads(){
		region.setBorderRoads(borderRoads);
		assertTrue(region.getBorderRoads().equals(borderRoads));
	}
	
	@Test
	public void setBorderRoads(){
		region.setBorderRoads(borderRoads);
		assertTrue(region.getBorderRoads().equals(borderRoads));
	}
	
	@Test (expected = NoRoadWithThisNumberException.class)
	public void getBorderRoad() throws NoRoadWithThisNumberException{
		region.setBorderRoads(borderRoads);
		try {
			assertTrue(region.getBorderRoad(1).getUID() == 1);
		} catch (NoRoadWithThisNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		region.getBorderRoad(5);
	}
	
	@Test
	public void addAnimal(){
		List <Animal> animals ;
		region.addAnimal(sheep);
		assertTrue(region.getContainedAnimals().iterator().next().equals(sheep));
		animals = CollectionsUtilities.newListFromIterable ( region.getContainedAnimals( ));
		assertTrue(animals.size() == 1);
	}
	
	@Test
	public void removeAnimal () 
	{
		Sheep s1 ;
		Sheep s2 ;
		Animal w ;
		s1 = new Sheep ( "t2" ) ;
		s2 = new Sheep ( "t3" ) ;
		int i = 0 ;
		w = null ;
		do
		{
			try 
			{
				w = AnimalFactory.newAnimalFactory ( MatchIdentifier.newInstance() ).newWolf();
			} 
			catch (WolfAlreadyGeneratedException e) 
			{} 
			catch (SingletonElementAlreadyGeneratedException e ) 
			{
				i ++ ;
			}
		}
		while ( w == null ) ;
		region.addAnimal ( s1 ) ;
		region.addAnimal ( s2 ) ;
		region.addAnimal ( w ) ;
		region.removeAnimal(w) ;
		assertTrue ( CollectionsUtilities.contains ( region.getContainedAnimals () , s1 ) ) ;
		assertTrue ( CollectionsUtilities.contains ( region.getContainedAnimals () , s2 ) ) ;
		assertFalse ( CollectionsUtilities.contains ( region.getContainedAnimals () , w ) ) ;	
	}
	
	@Test
	public void getContainedAnimal()
	{
		List <Animal> animals ;
		region.addAnimal(sheep);
		assertTrue(region.getContainedAnimals().iterator().next().equals(sheep));
		animals = CollectionsUtilities.newListFromIterable( region.getContainedAnimals() );
		assertTrue(animals.size() == 1);
	}
	
	@Test
	public void isClosed()
	{
		region.setBorderRoads(borderRoads);
		assertFalse(region.isClosed());
		try {
			region.getBorderRoad(1).setElementContained(fence);
		} catch (NoRoadWithThisNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(region.isClosed());
	}

	public class Sheep extends Ovine{
		public Sheep ( String name )
		{
			super(PositionableElementType.SHEEP, name ) ;
		}
	}
}
