package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionableTest;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test tests PositionableElement class
 */
public class PositionableElementTest {
	
	/*
	 * Declaring all the variables needed for the setup phase of the test
	 */
	static AdultOvine sheep;
	static AnimalFactory animalFactory ;
	static GameMap map;
	static DummyMatchIdentifier dummyMatchIdentifier;
	
	/*
	 * Building the test environment
	 */
	@BeforeClass
	public static void setUpBeforeClass()
	{
		dummyMatchIdentifier = new DummyMatchIdentifier();
		try {
			animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		sheep = (AdultOvine) animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
		
		try {
			map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheep.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
	}
	
	/*
	 * This test tests the getPosition() method
	 */
	@Test
	public void getPosition() {
		assertTrue(sheep.getPosition().getType() == RegionType.SHEEPSBURG);
	}
	
	/*
	 * This dummy matchIdentifier is used to build the animalfactory
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
