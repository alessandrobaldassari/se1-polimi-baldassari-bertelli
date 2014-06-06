package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WithReflectionObservableSupport;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

public class MatchConnectionLoosingController implements ConnectionLoosingManager , Runnable
{
	
	/***/
	public static final long WAITING_TIME = 60 * Utilities.MILLISECONDS_PER_SECOND ;

	public static final long SUSPENSION_TIME = 60 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/***/
	private WithReflectionObservableSupport < ConnectionLoosingManagerObserver > support ;
	
	/***/
	private Collection < Suspendable > managedHandlers ;
	
	public MatchConnectionLoosingController () 
	{
		support = new WithReflectionObservableSupport < ConnectionLoosingManagerObserver > () ;
		managedHandlers = new ArrayList < Suspendable > () ; 
	}
		
	@Override
	public void run() 
	{
		while ( true ) ;
	}

	/***/
	@Override
	public boolean manageConnectionLoosing ( Suspendable looser , boolean pingAlreadyTested ) 
	{
		boolean res ;
		if ( looser.isSuspended () == false )
		{
			try 
			{
				support.notifyObservers( "onBeginSuspensionControl" , looser ) ;
				if ( ! pingAlreadyTested )
					Thread.sleep ( SUSPENSION_TIME ) ;
				looser.suspend();
				res = false ;
				support.notifyObservers( "onEndSuspensionControl" , false ) ;
			} 
			catch (NoSuchMethodException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (SecurityException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (IllegalAccessException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (IllegalArgumentException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (InvocationTargetException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (InterruptedException e) 
			{
				res = true ;
			}
			try {
				support.notifyObservers( "onEndSuspensionControl" , res ) ;
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver ( ConnectionLoosingManagerObserver newObserver) 
	{
		support.addObserver ( newObserver ) ;
	}

	@Override
	public void removeObserver ( ConnectionLoosingManagerObserver oldObserver ) 
	{
		support.removeObserver ( oldObserver ) ;
	}	
		
}
