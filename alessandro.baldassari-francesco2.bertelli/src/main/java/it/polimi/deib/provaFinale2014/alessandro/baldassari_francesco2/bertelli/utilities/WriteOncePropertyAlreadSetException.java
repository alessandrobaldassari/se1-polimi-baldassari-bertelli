package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/**
 * This Exception models the situation where a component try to set a Property that can be write
 * just one time and has already been written before. 
 */
public class WriteOncePropertyAlreadSetException extends Exception 
{

	/**
	 * The property already written. 
	 */
	private final String propertyName ;
	
	/**
	 * @param propertyName the property already written.
	 * @throws IllegalArgumentException if the parameter is null. 
	 */
	public WriteOncePropertyAlreadSetException ( String propertyName ) 
	{
		if ( propertyName != null )
			this.propertyName = propertyName ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the propertyName property.
	 * 
	 * @return the propertyName field.
	 */
	public String getPropertyName () 
	{
		return propertyName ;
	}
	
}
