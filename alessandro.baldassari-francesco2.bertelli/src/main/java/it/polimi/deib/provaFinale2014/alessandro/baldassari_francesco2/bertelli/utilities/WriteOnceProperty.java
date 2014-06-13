package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.io.Serializable;

/***/
public class WriteOnceProperty < T > implements Serializable
{

	/***/
	private T value ;
	
	/***/
	private boolean set ;
	
	/***/
	public WriteOnceProperty () 
	{
		value = null ;
		set = false ;
	}
	
	/***/
	public synchronized void setValue ( T value ) throws WriteOncePropertyAlreadSetException 
	{
		if ( ! set )
		{
			this.value = value ;
			set = true ;
		}
		else
			throw new WriteOncePropertyAlreadSetException ( "VALUE" ) ;
	}
	
	/***/
	public synchronized T getValue () throws PropertyNotSetYetException 
	{
		if ( set ) 
			return value ;
		else
			throw new PropertyNotSetYetException () ;
	}
	
	/***/
	public synchronized boolean isValueSet () 
	{
		return set ;
	}
	
}
