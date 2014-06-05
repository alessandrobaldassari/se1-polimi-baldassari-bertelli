package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test class test Lamb
 */
public class LambTest 
{

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
		dummyMatchIdentifier = new DummyMatchIdentifier ( 0 );
		try {
			animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		sheep = (AdultOvine) animalFactory.newAdultOvine("sheep", AdultOvineType.SHEEP);
		ram = (AdultOvine) animalFactory.newAdultOvine("ram", AdultOvineType.RAM);		
		lamb = null ;
		while ( lamb == null )
		try {
			lamb = ram.mate(sheep);
		}
		catch (CanNotMateWithHimException e) {}
		catch (MateNotSuccesfullException e) {}
		
	}
	
	/*
	 * Testing getBirthTurn() method
	 */
	@Test
	public void getBirthTurn(){
		assertTrue ( lamb.getBirthTurn() == 0);
	}
	
	/*
	 * Testing getFather() method
	 */
	@Test
	public void getFather(){
		assertTrue(lamb.getFather().equals(ram));
	}
	
	/*
	 * Testing getMother() method
	 */
	@Test
	public void getMother(){
		assertTrue(lamb.getMother().equals(sheep));
	}

}
