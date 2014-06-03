package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class UtilitiesTest 
{

	@Test
	public void getTypes () 
	{
		Class < ? > [] res ;
		Object [] in ;
		in = new Object [ 3 ] ;
		in [ 0 ] = new Integer ( 3 ) ;
		in [ 1 ] = new String ( "" ) ;
		in [ 2 ] = new ArrayList < Integer > ( 3 ) ;
		res = Utilities.getTypes ( in ) ;
		assertTrue ( res [ 0 ] == Integer.class ) ;
		assertTrue ( res [ 1 ] == String.class ) ;
		assertTrue ( res [ 2 ] == ArrayList.class ) ;
		assertFalse ( res [ 2 ] == Collections.class ) ;
	}
	
}
