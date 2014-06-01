package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class MethodInvocationException extends Exception 
{

	private String methodName ;
	
	/***/
	public MethodInvocationException ( String methodName ) 
	{
		super () ;
		if ( methodName != null && methodName.compareToIgnoreCase ( "" ) != 0 )
			this.methodName = methodName ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public String getMethodName () 
	{
		return methodName ;
	}
	
}
