package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class WorkflowExceptionTest 
{

	@Test
	public void ctor1 () 
	{
		WorkflowException w ;
		w = new WorkflowException ( "ciao" ) ;
		assertTrue ( w.getMessage().compareTo ( "ciao" ) == 0 ) ;
	}
	
	@Test
	public void ctor2 () 
	{
		WorkflowException w ;
		IOException i ;
		i = new IOException();
		w = new WorkflowException ( i , "ciao" ) ;
		assertTrue ( w.getMessage().compareTo ( "ciao" ) == 0 ) ;
		assertTrue ( w.getCause().equals ( i ) ) ;
	}
	
}
