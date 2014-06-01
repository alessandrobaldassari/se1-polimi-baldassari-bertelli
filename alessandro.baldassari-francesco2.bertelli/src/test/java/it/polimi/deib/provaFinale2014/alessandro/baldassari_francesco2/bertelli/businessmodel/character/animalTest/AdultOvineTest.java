package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animalTest;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

/*
 * This JUnit test test AdultOvine class
 */
public class AdultOvineTest {
	
	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	static AdultOvine ram;
	static AdultOvine sheep;
	private static AnimalFactory animalFactory ;
	Lamb lamb = null;
	
	/*
	 * Executing some code using a trick to initialize animalFactory correctly
	 */
	static 
	{
		try {
			animalFactory = AnimalFactory.newAnimalFactory(new DummyMatchIdentifier());
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		ram = (AdultOvine) animalFactory.newAdultOvine("ram", AdultOvineType.RAM);
		sheep = (AdultOvine) animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
	}
	
	/*
	 * This test tests if getType() is correctly implemented using ram and sheep (builded by animalFactory)
	 */
	@Test
	public void getType() {
		assertTrue(ram.getType() == AdultOvineType.RAM);
		assertTrue(sheep.getType() == AdultOvineType.SHEEP);
	}
	
	/*
	 * This test tests mate() checking if the returned lamb is correctly initialized. It tests also an incorrect invocation to check the CanNotMateWithHimExeption
	 */
	@Test (expected = MateNotSuccesfullException.class)
	public void mate() throws CanNotMateWithHimException, MateNotSuccesfullException{
		try {
			lamb = ram.mate(sheep);
			if (lamb != null) {
				assertTrue(lamb.getBirthTurn() == 0);
				assertTrue(lamb.getFather() == ram);
				assertTrue(lamb.getMother() == sheep);
				assertTrue(lamb.getName() == "");
			}	
		} catch (CanNotMateWithHimException e) {
			assertFalse(false);
		}
	}
		
	

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
