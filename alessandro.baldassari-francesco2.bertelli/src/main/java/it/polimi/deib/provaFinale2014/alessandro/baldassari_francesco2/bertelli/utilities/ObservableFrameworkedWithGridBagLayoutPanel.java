package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.lang.reflect.InvocationTargetException;

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
		try 
		{
			s.notifyObservers ( methodName , args ) ;
		} 
		catch ( NoSuchMethodException e ) 
		{
			e.printStackTrace () ;
			throw new MethodInvocationException ( methodName ) ;
		} 
		catch (SecurityException e) 
		{
			e.printStackTrace () ;
			throw new MethodInvocationException ( methodName ) ;
		}
		catch ( IllegalAccessException e ) 
		{
			e.printStackTrace () ;
			throw new MethodInvocationException ( methodName ) ;
		}
		catch ( IllegalArgumentException e ) 
		{
			e.printStackTrace () ;
			throw new MethodInvocationException ( methodName ) ;
		} 
		catch (InvocationTargetException e) 
		{
			e.printStackTrace () ;
			throw new MethodInvocationException ( methodName ) ;
		}
	}
	
}
