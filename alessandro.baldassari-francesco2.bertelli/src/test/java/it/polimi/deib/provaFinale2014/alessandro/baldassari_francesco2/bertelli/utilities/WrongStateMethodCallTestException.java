package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

import org.junit.Before;
import org.junit.Test;

public class WrongStateMethodCallTestException 
{

	private WrongStateMethodCallException w ;
	
	@Before
	public void setUp () 
	{
		w = new WrongStateMethodCallException () ;
	}
	
	@Test
	public void constructor () 
	{
		assertTrue ( w != null ) ;
	}
	
}
