package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

public class AnimalTest
{

	private Animal a1 ;
	
	private Animal a2 ;
	
	@Before
	public void setUp ()
	{
		a1 = new DummyAnimal ( PositionableElementType.RAM , "RAM1" ) ;
		a2 = new DummyAnimal ( PositionableElementType.RAM , "RAM2" ) ;
	}
	
	@Test
	public void equals () 
	{
		assertTrue ( a1.equals ( a1 ) ) ;
		assertFalse ( a2.equals ( a1 ) ) ;
		assertFalse ( a1.equals ( new Integer ( 3 ) ) ) ;
	}
	
}

class DummyAnimal extends Animal 
{

	protected DummyAnimal ( PositionableElementType positionableElementType ,
			String name ) 
	{
		super(positionableElementType, name);
	}
		
}
