package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.WolfTestClosed.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * this jUnit test tests CharacterDoesntMoveException class
 */
public class CharacterDoesntMoveExceptionTest 
{

	/*
	 * Declaring all the variables needed to the setup phase
	 */
	Ovine sheep;
	AnimalFactory animalFactory ;
	Identifiable < Match > dummyMatchIdentifier;
	
	/*
	 * Building the test environment
	 */
	@Before
	public void setUpBeforeClass()
	{
		int i ;
		i = 0 ;
		try 
		{
			dummyMatchIdentifier = MatchIdentifier.newInstance();
			animalFactory = AnimalFactory.newAnimalFactory ( dummyMatchIdentifier);
			sheep =  animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
		} 
		catch (SingletonElementAlreadyGeneratedException e) {}
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