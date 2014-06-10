package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

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
}
