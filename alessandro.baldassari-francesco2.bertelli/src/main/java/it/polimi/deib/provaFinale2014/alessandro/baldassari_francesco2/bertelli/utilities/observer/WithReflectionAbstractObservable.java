package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

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
	protected synchronized void notifyObservers ( String methodName , Object ... args ) throws MethodInvocationException 
	{
		System.err.println ( "WITH_REFLECTION_ABSTRACT_OBSERVABLE - NOTIFY_OBSERVERS : BEGINS\nPARAMETERS : \n- methodName : " +methodName + "\n- args : " + Utilities.generateArrayStringContent ( args ) ) ;
		observableSupport.notifyObservers ( methodName , args );
		System.err.println ( "WITH_REFLECTION_ABSTRACT_OBSERVABLE - NOTIFY_OBSERVERS : END ( " + methodName + " ) " ) ;
	}
	
}
