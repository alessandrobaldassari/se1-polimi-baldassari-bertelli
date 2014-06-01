package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilitiesTest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

/***/
public class WriteOncePropertyAlreadySetExceptionTest 
{

	private WriteOncePropertyAlreadSetException w ;
	
	private String propertyName ;
	
	@Before
	public void setUp () 
	{
		propertyName = "DUMMY_METHOD" ;
		w = new WriteOncePropertyAlreadSetException ( propertyName ) ;
	}
	
	@Test
	public void getPropertyName ()
	{
		int res ;
		res = propertyName.compareTo ( w.getPropertyName () ) ;
		assertTrue ( res == 0 ) ;
	}
	
}
