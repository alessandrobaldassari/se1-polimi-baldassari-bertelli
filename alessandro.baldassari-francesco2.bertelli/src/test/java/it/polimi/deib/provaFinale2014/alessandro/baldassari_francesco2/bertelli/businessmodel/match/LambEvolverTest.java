package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

public class LambEvolverTest 
{

	private AnimalFactory af ;
	
	private GameMap m ;
	
	private LambEvolver l ;
	
	@Before
	public void setUp () 
	{
		Identifiable<Match>id ;
		int i ;
		i = 0 ;
		do 
		{
			try 
			{
				id = MatchIdentifier.newInstance();

				af = AnimalFactory.newAnimalFactory ( id ) ;
				m = GameMapFactory.getInstance().newInstance ( id ) ;

			} catch (SingletonElementAlreadyGeneratedException e) 
			{
				i++ ;
			}
		}while ( af == null ) ;
		l = new LambEvolverImpl ( af ) ;
	}
	
	@Test
	public void evolve () 
	{
		AdultOvine mom ;
		AdultOvine dad ;
		Lamb la ;
		mom = (AdultOvine) af.newAdultOvine ( AdultOvineType.SHEEP ) ;
		dad = (AdultOvine) af.newAdultOvine ( AdultOvineType.RAM ) ;
		la= (Lamb) af.newLamb ( 0 , dad, mom);
		m.getRegionByUID(10).addAnimal(la);
		la.moveTo ( m.getRegionByUID(10) ); 
		l.evolve ( la ) ;
		assertTrue ( la.getPosition() == null ) ;
		assertFalse ( CollectionsUtilities.contains ( m.getRegionByUID ( 10 ).getContainedAnimals() , la) ) ;
		assertTrue ( CollectionsUtilities.iterableSize ( m.getRegionByUID(10).getContainedAnimals() ) == 1 ) ;
		assertTrue ( CollectionsUtilities.newListFromIterable ( m.getRegionByUID(10).getContainedAnimals() ).get(0) instanceof AdultOvine ) ;
	}
	
}
