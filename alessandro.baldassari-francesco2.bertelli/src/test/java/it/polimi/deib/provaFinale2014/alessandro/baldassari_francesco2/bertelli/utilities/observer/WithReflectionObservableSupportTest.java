package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

public class WithReflectionObservableSupportTest 
{

	/***/
	private WithReflectionObservableSupport < Observer > w ;
	
	/***/
	@Before
	public void setUp () 
	{
		w = new WithReflectionObservableSupport < Observer > () ;
	}
	
	/***/
	@Test
	public void addObserver () 
	{
		Observer o ;
		o = new DummyObserver () ;
		w.addObserver ( o ) ;
		assertTrue ( w.containsObserver ( o ) ) ;
	}
	
	/***/
	@Test
	public void removeObserver () 
	{
		Observer o ;
		o = new DummyObserver () ;
		w.addObserver ( o ) ;
		assertTrue ( w.containsObserver ( o ) ) ;
		w.removeObserver ( o ) ;
		assertFalse ( w.containsObserver ( o ) ) ;
	}
	
	/***/
	@Test
	public void containtsObserver () 
	{
		Observer o1 ;
		Observer o2 ;
		o1 = new DummyObserver () ;
		o2 = new DummyObserver () ;
		w.addObserver ( o1 ) ;
		assertTrue ( w.containsObserver ( o1 ) ) ;
		assertFalse ( w.containsObserver ( o2 ) ) ;
	}
	
	/***/
	@Test
	public void notifyObservers1 () 
	{
		DummyObserver o ;
		o = new DummyObserver () ;
		w.addObserver ( o ) ;
		try {
			w.notifyObservers ( "setDummyInt" , 4 ) ;
		} 
		catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (MethodInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue ( o.getDummyInt() == 4 ) ;
	}
	
	@Test ( expected = MethodInvocationException.class )
	public void notifyObservers2 () throws MethodInvocationException 
	{
		DummyObserver o ; 
		o = new DummyObserver () ;
		w.addObserver ( o ) ;
		
			w.notifyObservers ( "nonExistingMethod" , new Object () ) ;
	}
	
}
