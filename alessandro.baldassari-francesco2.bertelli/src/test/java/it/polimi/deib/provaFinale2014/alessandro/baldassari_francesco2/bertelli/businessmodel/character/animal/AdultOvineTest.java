package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

/*
 * This JUnit test test AdultOvine class
 */
public class AdultOvineTest {
	

	 AdultOvine ram;
	 AdultOvine sheep;
	 static AnimalFactory animalFactory ;
	Lamb lamb ;
	
	@Before
	public void setUp ()
	{
		try
		{
			animalFactory = AnimalFactory.newAnimalFactory ( MatchIdentifier.newInstance() ) ;
			ram = (AdultOvine) animalFactory.newAdultOvine("ram", AdultOvineType.RAM);
			sheep = (AdultOvine) animalFactory.newAdultOvine("Sheep", AdultOvineType.SHEEP);
		}
		catch (SingletonElementAlreadyGeneratedException e) {}
	}

	@Test ( expected = IllegalArgumentException.class )
	public void ctorExc () 
	{
		new AdultOvine ( PositionableElementType.RAM , "ciao" , null ) ;
	}
	
	/*
	 * This test tests if getType() is correctly implemented using ram and sheep (builded by animalFactory)
	 */
	@Test
	public void getType () 
	{
		assertTrue(ram.getType() == AdultOvineType.RAM);
		assertTrue(sheep.getType() == AdultOvineType.SHEEP);
	}
	
	@Test
	public void toStringTest () 
	{
		assertTrue ( ram.toString() != null ) ;
		assertTrue ( ! ram.toString().isEmpty() ) ;
	}
	
	@Test
	public void equals () 
	{
		assertTrue ( ram.equals(ram) );
		assertFalse ( ram.equals(sheep) ) ;
	}
	
	/*
	 * This test tests mate() checking if the returned lamb is correctly initialized. It tests also an incorrect invocation to check the CanNotMateWithHimExeption
	 */
	@Test 
	public void mate()
	{
		try 
		{
			lamb = ram.mate(sheep,3);
			if (lamb != null) 
			{
				assertTrue(lamb.getBirthTurn() == 3);
				assertTrue(lamb.getFather().equals( ram ) ) ;
				assertTrue(lamb.getMother().equals ( sheep ) );
				assertTrue(lamb.getName() == "");
			}	
		}
		catch (CanNotMateWithHimException e) 
		{
			fail () ;
		}
		catch ( MateNotSuccesfullException m )
		{
			assertTrue ( lamb == null ) ;
		}
	}
	
	@Test 
	public void mateExc ()
	{
		try 
		{
			ram.mate ( sheep , 0 ) ;
		}
		catch (CanNotMateWithHimException e) 
		{
			fail () ;
		} 
		catch (MateNotSuccesfullException e) 
		{
			assertTrue ( e.getFirstPartner().equals(ram) ) ;
			assertTrue ( e.getSecondPartner().equals(sheep) ) ;
		}
	}
	
}
