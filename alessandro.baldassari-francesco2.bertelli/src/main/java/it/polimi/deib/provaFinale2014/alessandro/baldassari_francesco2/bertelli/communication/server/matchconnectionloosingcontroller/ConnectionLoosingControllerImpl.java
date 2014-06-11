package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

import java.util.HashMap;
import java.util.Map;

/***/
public class ConnectionLoosingControllerImpl implements ConnectionLoosingController
{

	/***/
	private Map < Integer , ClientHandler > suspendedhandlers ;
	
	/***/
	private WithReflectionObservableSupport < ConnectionLoosingManagerObserver > support ;
	
	/***/
	public ConnectionLoosingControllerImpl () 
	{
		super () ;
		support = new WithReflectionObservableSupport < ConnectionLoosingManagerObserver > () ;
		suspendedhandlers = new HashMap < Integer , ClientHandler > () ; 
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver ( ConnectionLoosingManagerObserver newObserver ) 
	{
		support.addObserver ( newObserver ) ;
	}

	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public void removeObserver ( ConnectionLoosingManagerObserver oldObserver ) 
	{
		support.removeObserver ( oldObserver ) ;
	}
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public boolean manageConnectionLoosing ( Suspendable looser , ClientHandler < ? > connector , boolean pingAlreadyTested ) 
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
				suspendedhandlers.put ( connector.getUID() , connector ) ;
				support.notifyObservers( "onEndSuspensionControl" , false ) ;
			} 
			catch (SecurityException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (IllegalArgumentException e1) 
			{
				looser.suspend();
				res = false ;
			}
			catch (InterruptedException e) 
			{
				res = true ;
			} 
			catch (MethodInvocationException e ) 
			{
				looser.suspend();
				res = false ;
			}
			try 
			{
				support.notifyObservers( "onEndSuspensionControl" , res ) ;
			} 
			catch ( SecurityException e ) 
			{
				e.printStackTrace();
			} 
			catch ( IllegalArgumentException e )
			{
				e.printStackTrace();
			} 
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}			
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}

	public ClientHandler getClientHandler ( int clientHandlerUID ) 
	{
		return suspendedhandlers.get ( clientHandlerUID ) ;
	}
	
}

