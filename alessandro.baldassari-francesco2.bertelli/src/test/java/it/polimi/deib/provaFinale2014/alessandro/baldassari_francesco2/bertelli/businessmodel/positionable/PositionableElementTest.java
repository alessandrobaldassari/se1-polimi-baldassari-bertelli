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
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test tests PositionableElement class
 */
public class PositionableElementTest 
{
	
	/*
	 * Declaring all the variables needed for the setup phase of the test
	 */
	 AdultOvine sheep;
	 AnimalFactory animalFactory ;
	 GameMap map;
	 ObjectIdentifier < Match > dummyMatchIdentifier;
	
	/*
	 * Building the test environment
	 */
	@Before
	public void setUpBeforeClass()
	{
		int i ;
		i = 0 ;
		animalFactory = null ;
		do 
		{
			try 
			{
				dummyMatchIdentifier = MatchIdentifier.newInstance();
				animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
				map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
				sheep = (AdultOvine) animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
				sheep.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
			} 
			catch (SingletonElementAlreadyGeneratedException e) 
			{
				i ++ ;
			}
		}
		while ( animalFactory == null ) ;
	}
	
	/*
	 * This test tests the getPosition() method
	 */
	@Test
	public void getPosition () 
	{
		assertTrue ( sheep.getPosition().getType () == RegionType.SHEEPSBURG ) ;
	}
	
}
