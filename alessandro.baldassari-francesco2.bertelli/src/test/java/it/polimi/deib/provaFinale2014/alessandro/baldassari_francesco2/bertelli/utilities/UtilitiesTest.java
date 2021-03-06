package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collection;
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

	@Test
	public void lookForIdentifier () 
	{
		Collection < Identifiable > c ;
		c = new ArrayList < Identifiable > () ;
		Identifiable i1 ;
		Identifiable i2 ;
		i1 = new DummyId(0);
		i2 = new DummyId(1);
		c.add ( i1 ) ;
		c.add ( i2 ) ;
		assertTrue ( Utilities.lookForIdentifier ( c , 0 ).equals ( i1 ) ) ;
		assertTrue ( Utilities.lookForIdentifier ( c , 4 ) == null ) ;		
 	}
	
	private class DummyId implements Identifiable
	{

		private int x ;
		
		public DummyId ( int x ) 
		{
			this.x = x ;
		}
		
		@Override
		public int getUID() 
		{
			return x ;
		}
		
		
		
	}
	
}
