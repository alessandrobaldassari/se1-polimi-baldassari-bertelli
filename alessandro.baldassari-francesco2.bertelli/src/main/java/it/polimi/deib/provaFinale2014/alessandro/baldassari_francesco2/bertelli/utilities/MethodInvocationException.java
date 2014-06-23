package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class MethodInvocationException extends Exception 
{

	/***/
	private final String methodName ;
	
	/**
	 * @param methodName 
	 * @param cause 
	 * @throws IllegalArgumentException
	 */
	public MethodInvocationException ( String methodName , Throwable cause ) 
	{
		super ( cause )  ;
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
