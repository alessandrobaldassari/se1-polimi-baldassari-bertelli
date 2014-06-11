package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

/***/
public abstract class ObservableFrameworkedWithGridBagLayoutPanel < T extends Observer > extends FrameworkedWithGridBagLayoutPanel implements Observable < T > 
{

	/***/
	private WithReflectionObservableSupport < T > s ;
	
	/***/
	public ObservableFrameworkedWithGridBagLayoutPanel () 
	{
		super () ;
		s = new WithReflectionObservableSupport < T > () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver ( T newObserver ) 
	{
		s.addObserver ( newObserver ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void removeObserver ( T oldObserver ) 
	{
		s.addObserver ( oldObserver ) ;
	}

	protected void notifyObservers ( String methodName , Object ... args ) throws MethodInvocationException
	{
		s.notifyObservers ( methodName , args ) ;
	}
	
}
