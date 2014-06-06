package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveNotAllowedExceptionTest 
{

	@Test
	public void ctor () 
	{
		MoveNotAllowedException e = null ;
		e = new MoveNotAllowedException ( "" ) ;
		assertTrue ( e != null ) ;
	}
	
}
