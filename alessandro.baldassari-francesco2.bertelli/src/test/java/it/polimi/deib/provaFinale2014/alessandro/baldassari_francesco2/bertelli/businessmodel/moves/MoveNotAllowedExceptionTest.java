package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
	
	@Test
	public void ctor2 () 
	{
		MoveNotAllowedException e = null ;
		IOException i ;
		i = new IOException () ;
		e = new MoveNotAllowedException ( "" , i ) ;
		assertTrue ( e != null ) ;
		assertTrue ( e.getCause().equals(i) );
	}
	
}
