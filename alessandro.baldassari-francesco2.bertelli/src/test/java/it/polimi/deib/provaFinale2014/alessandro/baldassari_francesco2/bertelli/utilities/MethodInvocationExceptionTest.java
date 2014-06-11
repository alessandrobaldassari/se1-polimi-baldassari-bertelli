package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;

import org.junit.Before;
import org.junit.Test;

public class MethodInvocationExceptionTest 
{

	private MethodInvocationException m ;
	
	private String methodName = "dummyMethodName" ;
	
	@Before 
	public void setUp () 
	{
		m = new MethodInvocationException ( methodName , null ) ;
	}
	
	@Test ( expected = IllegalArgumentException.class )
	public void constructor () 
	{
		m = new MethodInvocationException ( null , null ) ;
	}
	
	@Test
	public void getMethodName () 
	{
		assertEquals ( 0 , methodName.compareTo ( m.getMethodName () ) ) ;
	}
	
}
