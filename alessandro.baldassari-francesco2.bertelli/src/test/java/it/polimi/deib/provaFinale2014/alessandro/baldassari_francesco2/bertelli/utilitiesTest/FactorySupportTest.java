package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilitiesTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FactorySupport;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;

/***/
public class FactorySupportTest
{

	/***/
	private FactorySupport factorySupport ;
	
	/***/
	@Before
	public void setUp () 
	{
		factorySupport = new FactorySupport () ;
	}
	
	/***/
	@Test
	public void addUser () 
	{
		Identifiable < Match > id ;
		id = new DummyMatchIdentifier ( 0 ) ;
		factorySupport.addUser ( id ) ;
		assertTrue ( factorySupport.isAlreadyUser ( id ) ) ;
	}
	
	@Test
	public void isAlreadyUser () 
	{
		Identifiable < Match > id1 ;
		Identifiable < Match > id2 ;
		id1 = new DummyMatchIdentifier ( 0 ) ;
		id2 = new DummyMatchIdentifier ( 1 ) ;
		factorySupport.addUser ( id1 ) ;
		assertTrue ( factorySupport.isAlreadyUser ( id1 ) ) ;
		assertFalse ( factorySupport.isAlreadyUser ( id2 ) ) ;
	}
	
	private class DummyMatchIdentifier implements Identifiable < Match > 
	{

		private final int id ;
		
		DummyMatchIdentifier ( int  id ) 
		{
			this.id = id ;
		}
		
		@Override
		public boolean isEqualsTo ( Identifiable<Match> otherObject ) 
		{
			if ( otherObject instanceof DummyMatchIdentifier )
			return id == ( ( DummyMatchIdentifier ) otherObject ).id ;
			else
				return false ;
		}
		
	}
	
}
