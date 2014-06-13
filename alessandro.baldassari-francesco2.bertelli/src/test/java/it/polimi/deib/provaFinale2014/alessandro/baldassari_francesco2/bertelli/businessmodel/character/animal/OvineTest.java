package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

import org.junit.Before;
import org.junit.Test;

public class OvineTest {

	private Ovine a1 ;
	
	private Ovine a2 ;
	
	@Before
	public void setUp ()
	{
		a1 = new DummyOvine ( PositionableElementType.RAM , "RAM1" ) ;
		a2 = new DummyOvine ( PositionableElementType.RAM , "RAM2" ) ;
	}
	
	@Test
	public void equals () 
	{
		assertTrue ( a1.equals ( a1 ) ) ;
		assertFalse ( a2.equals ( a1 ) ) ;
		assertFalse ( a1.equals ( new Integer ( 3 ) ) ) ;
		assertFalse ( a1.equals ( new Wolf ( "WOLF" ) ) ) ;
	}
	
}

class DummyOvine extends Ovine
{

	protected DummyOvine ( PositionableElementType positionableElementType ,
			String name ) 
	{
		super(positionableElementType, name);
	}
		
}
