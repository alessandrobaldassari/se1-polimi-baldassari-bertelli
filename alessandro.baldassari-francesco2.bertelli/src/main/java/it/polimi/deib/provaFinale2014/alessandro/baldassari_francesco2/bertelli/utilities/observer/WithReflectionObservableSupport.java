package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/***/
public class WithReflectionObservableSupport < T extends Observer > implements Serializable
{

	/***/
	private Collection < T > observers ;
 
	/***/
	private Map < Couple < String , Class [] > , Method > bufferedMethods ;
	
	/***/
	public WithReflectionObservableSupport () 
	{
		observers = new Vector < T > () ;
		bufferedMethods = new HashMap < Couple < String , Class [] > , Method > () ;
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

	/***/
	public boolean containsObserver ( T t ) 
	{
		return observers.contains ( t ) ;
	}
	
	/***/
	public void notifyObservers ( String methodName , Object ... args ) throws MethodInvocationException
	{
		Method m ;
		System.err.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc " + methodName);
		System.err.println ( "WITH_REFLECTION_OBSERVABLE_SUPPORT - NOTIFY_OBSERVERS : INIZIO\nPARAMETERS\n- methodName : " + methodName + "\n-args : " +Utilities.generateArrayStringContent(args) );
		if ( ! observers.isEmpty() )
		{
			try 
			{
				System.err.println ( "notify not empty" ) ;
				m = obtainMethod ( methodName , args ) ;
				System.err.println ( "preso " + m ) ;
				effectiveNotification ( m , args ) ;
				System.err.println ( "finito " + methodName ) ;
			}
			catch ( NoSuchMethodException e ) 
			{
				e.printStackTrace();
				throw new MethodInvocationException ( methodName , e ) ;
			}
			catch ( SecurityException e ) 
			{
				e.printStackTrace();
				throw new MethodInvocationException ( methodName , e ) ;
			} 
			catch ( IllegalAccessException e ) 
			{
				e.printStackTrace();
				throw new MethodInvocationException ( methodName , e ) ;
			}
			catch ( IllegalArgumentException e ) 
			{
				e.printStackTrace();
				throw new MethodInvocationException ( methodName , e ) ;
			} 
			catch ( InvocationTargetException e ) 
			{
				e.printStackTrace();
				throw new MethodInvocationException ( methodName , e ) ;
			}
			System.out.println ( "WITH_REFLECTION_OBSERVABLE_SUPPORT - NOTIFY_OBSERVERS : END ( " + methodName + " )" ) ;
		}
	}
	
	/***/
	private Method obtainMethod ( String methodName , Object ... parameters ) throws NoSuchMethodException, SecurityException 
	{
		Class < ? > [] parametersTypes ;
		Method res ; 
		parametersTypes = Utilities.getTypes ( parameters ) ;
		res = observers.iterator ().next ().getClass ().getMethod ( methodName , parametersTypes ) ;
		return res ;
	} 
	
	/***/
	private void effectiveNotification ( Method m , Object ... args ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		for ( T t : observers )
			m.invoke ( t , args ) ;
	}
	
}