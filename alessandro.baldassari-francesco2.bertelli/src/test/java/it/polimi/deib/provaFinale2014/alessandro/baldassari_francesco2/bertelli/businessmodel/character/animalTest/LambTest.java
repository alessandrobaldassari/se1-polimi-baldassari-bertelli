package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animalTest;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test class test Lamb
 */
public class LambTest {

	static AdultOvine ram;
	static AdultOvine sheep;
	static AnimalFactory animalFactory ;
	static DummyMatchIdentifier dummyMatchIdentifier;
	static Lamb lamb;
	
	
	/*
	 * Setting up the environment for the test
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
		sheep = (AdultOvine) animalFactory.newAdultOvine("sheep", AdultOvineType.SHEEP);
		ram = (AdultOvine) animalFactory.newAdultOvine("ram", AdultOvineType.RAM);		
		try {
			lamb = ram.mate(sheep);
		} catch (CanNotMateWithHimException e) {
			System.out.println("c");
		} catch (MateNotSuccesfullException e) {
			System.out.println("m");
		}
		
	}
	
	/*
	 * Testing getBirthTurn() method
	 */
	@Test
	public void getBirthTurn(){
		assertTrue(lamb.getBirthTurn() == 0);
	}
	
	/*
	 * Testing getFather() method
	 */
	@Test
	public void getFather(){
		assertTrue(lamb.getFather() == ram);
	}
	
	/*
	 * Testing getMother() method
	 */
	@Test
	public void getMother(){
		assertTrue(lamb.getMother() == sheep);
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
