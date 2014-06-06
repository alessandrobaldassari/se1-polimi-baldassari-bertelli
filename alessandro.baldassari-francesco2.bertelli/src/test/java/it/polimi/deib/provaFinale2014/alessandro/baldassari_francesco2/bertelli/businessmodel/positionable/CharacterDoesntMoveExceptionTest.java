package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * this jUnit test tests CharacterDoesntMoveException class
 */
public class CharacterDoesntMoveExceptionTest {

	/*
	 * Declaring all the variables needed to the setup phase
	 */
	static Ovine sheep;
	static AnimalFactory animalFactory ;
	static Identifiable < Match > dummyMatchIdentifier;
	
	/*
	 * Building the test environment
	 */
	@BeforeClass
	public static void setUpBeforeClass()
	{
		int i ;
		i = 0 ;
		do
		{
			try 
			{
				dummyMatchIdentifier = new DummyMatchIdentifier ( i ) ;
				animalFactory = AnimalFactory.newAnimalFactory ( dummyMatchIdentifier);
				sheep =  animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
			} 
			catch (SingletonElementAlreadyGeneratedException e) 
			{
				i ++ ;
			}
		} 
		while ( animalFactory == null ) ;
	}
	
	/*
	 * This test tests the exception constructor
	 */
	@Test
	public void CharacterDoesntMoveException()
	{
		CharacterDoesntMoveException exception = new CharacterDoesntMoveException(sheep);
		assertTrue(exception.getCharacter().equals (sheep ) ) ;
	}
	
}