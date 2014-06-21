package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PlayerWantsToExitGameExceptionTest 
{

	@Test
	public void ctor () 
	{
		PlayerWantsToExitGameException p ;
		p = new PlayerWantsToExitGameException();
		assertTrue ( p.getMessage().isEmpty());
		p = new PlayerWantsToExitGameException( "ciao" ) ;
		assertTrue ( p.getMessage().compareToIgnoreCase ( "ciao" ) == 0 ) ;
	}
	
}
