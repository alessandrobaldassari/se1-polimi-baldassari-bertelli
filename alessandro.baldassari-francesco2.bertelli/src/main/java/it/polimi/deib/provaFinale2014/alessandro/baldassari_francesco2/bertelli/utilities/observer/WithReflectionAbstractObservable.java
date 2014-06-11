package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;

import java.lang.reflect.InvocationTargetException;

/***/
public class WithReflectionAbstractObservable < T extends Observer > extends AbstractObservable < T > 
{

	/***/
	private WithReflectionObservableSupport < T > observableSupport ;
	
	/***/
	public WithReflectionAbstractObservable () 
	{
		observableSupport = new WithReflectionObservableSupport < T > () ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public void addObserver ( T newObserver ) 
	{
		observableSupport.addObserver ( newObserver ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void removeObserver ( T oldObserver ) 
	{
		observableSupport.removeObserver ( oldObserver ) ;
	}

	/***/
	protected void notifyObservers ( String methodName , Object ... args ) throws MethodInvocationException 
	{
		try 
		{
			observableSupport.notifyObservers ( methodName , args );
		} 
		catch (NoSuchMethodException e) 
		{
			throw new MethodInvocationException ( methodName , e) ;
		}
		catch ( SecurityException e ) 
		{
			throw new MethodInvocationException ( methodName , e) ;
		}
		catch ( IllegalAccessException e ) 
		{
			throw new MethodInvocationException ( methodName , e) ;
		}
		catch ( IllegalArgumentException e ) 
		{
			throw new MethodInvocationException ( methodName , e) ;
		}
		catch ( InvocationTargetException e ) 
		{
			throw new MethodInvocationException ( methodName , e) ;
		}
	}
	
}
