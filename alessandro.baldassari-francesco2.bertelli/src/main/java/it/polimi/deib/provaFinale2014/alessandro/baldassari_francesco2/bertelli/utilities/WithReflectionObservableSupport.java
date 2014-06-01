package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/***/
public class WithReflectionObservableSupport < T extends Observer > 
{

	/***/
	private Collection < T > observers ;
 
	/***/
	public WithReflectionObservableSupport () 
	{
		observers = new ArrayList < T > () ;
	}
	
	/***/
	public void addObserver ( T t ) 
	{
		observers.add ( t ) ;
	}
	
	/***/
	public void removeObserver ( T t ) 
	{
		observers.remove ( t ) ;
	}

	public void notifyObservers ( String methodName , Object ... args ) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		Method m ; 
		if ( observers.isEmpty() == false )
		{
			m = obtainMethod ( methodName , args ) ;
			effectiveNotification ( m , args ) ;
		}
	}
	
	private Method obtainMethod ( String methodName , Object ... parameters ) throws NoSuchMethodException, SecurityException 
	{
		Class < ? > [] parametersTypes ;
		Method res ; 
		byte i ;
		parametersTypes = new Class < ? > [ parameters.length ] ;
		for ( i = 0 ; i < parameters.length ; i ++ )
			parametersTypes [ i ] = parameters [ i ].getClass () ; 
		res = observers.iterator ().next ().getClass ().getMethod ( methodName , parametersTypes ) ;
		return res ;
	} 
	
	private void effectiveNotification ( Method m , Object ... args ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		for ( T t : observers )
			m.invoke ( t , args ) ;
	}
	
}