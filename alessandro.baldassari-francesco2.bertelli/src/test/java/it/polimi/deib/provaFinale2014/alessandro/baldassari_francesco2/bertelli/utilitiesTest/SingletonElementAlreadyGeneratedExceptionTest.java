package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilitiesTest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

public class SingletonElementAlreadyGeneratedExceptionTest {

	private SingletonElementAlreadyGeneratedException s ;
	
	@Before
	public void setUp () 
	{
		s = new SingletonElementAlreadyGeneratedException () ;
	}
	
	@Test
	public void constructor () 
	{
		assertTrue ( s != null ) ;
	}
	
}
