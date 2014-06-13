package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

public class AnimalFactoryTest 
{

	private AnimalFactory af ;
	
	@Before
	public void setUp ()
	{
		Identifiable < Match > id ;
		int i ;
		i = 0 ;
		do
		{
			try 
			{
				id = new DummyMatchIdentifier ( i ) ;
				af = AnimalFactory.newAnimalFactory ( id ) ;
			} 
			catch (SingletonElementAlreadyGeneratedException e) 
			{
				i ++ ;
			}
		}
		while ( af == null ) ;
	}
	
	@Test
	public void newAdultOvineWithName () 
	{
		String name ;
		Ovine o ;
		name = "AXEL" ;
		o = af.newAdultOvine ( name , AdultOvineType.RAM ) ;
		assertTrue ( o.getName ().compareTo ( name ) == 0 ) ;
		assertTrue ( o.getPositionableElementType().equals ( PositionableElementType.RAM ) )  ;
		assertTrue ( o.getPosition() == null ) ;
	}
	
	@Test
	public void newAdultOvineWithoutName () 
	{
		Ovine o1 ;
		Ovine o2 ;
		o1 = af.newAdultOvine ( AdultOvineType.RAM ) ;
		o2 = af.newAdultOvine ( AdultOvineType.SHEEP ) ;
		assertFalse ( o1.equals ( o2 ) ) ;
		assertTrue ( o1.getName().compareTo ( o2.getName() ) != 0 ) ;
		assertTrue ( o1.getPositionableElementType().equals ( PositionableElementType.RAM ) ) ;
		assertTrue ( o2.getPositionableElementType().equals ( PositionableElementType.SHEEP ) ) ;
	}
	
	@Test
	public void newWolf () 
	{
		Animal w ;
		try 
		{
			w = af.newWolf ();
			assertTrue ( w instanceof Wolf ) ;
			assertTrue ( w.getPositionableElementType().equals ( PositionableElementType.WOLF ) ) ;
			assertTrue ( w.getPosition () == null ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			fail () ;
		}
		w = null ;
		try 
		{
			w = af.newWolf() ;
			fail () ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			assertTrue ( w == null ) ;
		}
		try 
		{
			w = af.newBlackSheep();
			assertTrue ( w instanceof BlackSheep ) ;
			assertTrue ( w.getPositionableElementType().equals ( PositionableElementType.BLACK_SHEEP ) ) ;
		}
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
	}
	
	@Test
	public void newBlackSheep () 
	{
		Animal w ;
		try 
		{
			w = af.newBlackSheep ();
			assertTrue ( w instanceof BlackSheep ) ;
			assertTrue ( w.getPositionableElementType().equals ( PositionableElementType.BLACK_SHEEP ) ) ;
			assertTrue ( w.getPosition () == null ) ;
		}
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
		w = null ;
		try 
		{
			w = af.newBlackSheep();
			fail () ;
		}
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			assertTrue ( w == null ) ;
		}
		try 
		{
			w = af.newWolf();
			assertTrue ( w instanceof Wolf ) ;
			assertTrue ( w.getPositionableElementType().equals ( PositionableElementType.WOLF ) ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			fail () ;
		}
	}
	
}
