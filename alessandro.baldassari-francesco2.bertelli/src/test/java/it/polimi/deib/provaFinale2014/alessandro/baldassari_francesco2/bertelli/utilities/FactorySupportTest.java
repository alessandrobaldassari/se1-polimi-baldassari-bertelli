package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory.FactorySupport;

/***/
public class FactorySupportTest
{

	/***/
	private FactorySupport < Match > factorySupport ;
	
	/***/
	@Before
	public void setUp () 
	{
		factorySupport = new FactorySupport < Match > () ;
	}
	
	/***/
	@Test
	public void addUser () 
	{
		ObjectIdentifier < Match > id ;
		id = MatchIdentifier.newInstance();
		factorySupport.addUser ( id ) ;
		assertTrue ( factorySupport.isAlreadyUser ( id ) ) ;
	}
	
	@Test
	public void isAlreadyUser () 
	{
		ObjectIdentifier < Match > id1 ;
		ObjectIdentifier < Match > id2 ;
		id1 = MatchIdentifier.newInstance();
		id2 = MatchIdentifier.newInstance();
		factorySupport.addUser ( id1 ) ;
		assertTrue ( factorySupport.isAlreadyUser ( id1 ) ) ;
		assertFalse ( factorySupport.isAlreadyUser ( id2 ) ) ;
	}
	
}
