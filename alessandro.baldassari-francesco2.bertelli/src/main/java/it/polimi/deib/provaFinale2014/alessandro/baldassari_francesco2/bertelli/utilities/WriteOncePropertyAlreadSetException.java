package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

public class WriteOncePropertyAlreadSetException extends Exception 
{

	private String propertyName ;
	
	public WriteOncePropertyAlreadSetException ( String propertyName ) 
	{
		if ( propertyName != null )
			this.propertyName = propertyName ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public String getPropertyName () 
	{
		return propertyName ;
	}
	
}
