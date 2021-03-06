package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

/*
 * This JUnit test test Character class
 */
public class CharacterTest {
	/*
	 * Declaring variables that are going to be use for the setup phase
	 */
	AdultOvine sheep;
	AnimalFactory animalFactory ;
	GameMap map;
	ObjectIdentifier < Match > dummyMatchIdentifier;
	
	/*
	 * Building the environment for the test
	 */
	@Before
	public void setUpBeforeClass()
	{
		do
		{
			try 
			{
				dummyMatchIdentifier = MatchIdentifier.newInstance();
				animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
				sheep = (AdultOvine) animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
				map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
				sheep.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
			}
			catch (SingletonElementAlreadyGeneratedException e) {}
		}
		while ( animalFactory == null ) ;
	}
	
	/*
	 * This test tests moveTo() method
	 */
	@Test
	public void moveTo() {
		sheep.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
		assertTrue(sheep.getPosition().getType() == RegionType.SHEEPSBURG);
	}
	
	/*
	 * This test tests getName() method 
	 */
	@Test
	public void getName(){
		assertTrue(sheep.getName() == "Sheep");
	}
	
	/*
	 * This test tests equals() method override
	 */
	@Test
	public void equals(){
		assertTrue(sheep.equals(sheep));
	}
	
}
