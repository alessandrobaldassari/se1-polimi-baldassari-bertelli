package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

import org.junit.*;

import static org.junit.Assert.*;

/***/
public class CoupleTest 
{
	/***/
	private Couple <Integer, String> couple;
	
	/***/
	@Before 
	public void setUp ()
	{
		couple = new Couple <Integer , String> ( 10 , "Pippo" ) ;
	}
	
	/***/
	@Test 
	public void getFirstObject ()
	{
		assertEquals ( couple.getFirstObject (), (Integer)10 ) ;
	}
	
	/***/
	@Test 
	public void getSecondObject ()
	{
		assertEquals ( couple.getSecondObject () , "Pippo" ) ;
	}
	
	@Test
	public void toStringT () 
	{
		assertTrue ( ! couple.toString().isEmpty() );
	}
	
	@Test
	public void equals () 
	{
		Couple c2 ;
		c2 = new Couple < Integer , String > ( 40 , "Bruto" ) ;
		assertTrue ( c2.equals(c2) ) ;
		assertFalse ( couple.equals(c2) ) ;
	}
	
}
