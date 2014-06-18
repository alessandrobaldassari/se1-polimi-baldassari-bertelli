package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.TimeConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the ConnectionLoosingController interface.
 * It is a point of reference for the ClientHandlers that may loose the connection with their clients; in
 * this sense, ClientHandlers ( and in particular Players, where ClientHandlers are contained ) submit 
 * themselves to this component that will be used by the Servers that will try to re-establish connection. 
 */
public class ConnectionLoosingServer implements ConnectionLoosingManager , SuspendedClientHandlerBuffer , Runnable
{

	/**
	 * The ClientHandlers that registered to this Controller to eventually partecipate to connection 
	 * resuming operations. 
	 */
	private Map < Integer , ClientHandler < ? > > suspendedhandlers ;
	
	/**
	 * An object to implement the Observer pattern. 
	 */
	private WithReflectionObservableSupport < ConnectionLoosingManagerObserver > support ;
	
	/***/
	public ConnectionLoosingServer () 
	{
		super () ;
		support = new WithReflectionObservableSupport < ConnectionLoosingManagerObserver > () ;
		suspendedhandlers = new HashMap < Integer , ClientHandler < ? > > () ; 
	}
	
	/**
	 * Return the ClientHandler associated with the clientHandlerUID parameter if contained in this component.
	 * 
	 * @param clientHandlerUID the UID of the ClientHandler the caller is looking for.
	 * @return the value associated with the clientHandlerUID if it exists, null if the parameter is
	 *         unknown w.r.t this Controller.
	 */
	public synchronized ClientHandler < ? > getClientHandler ( Integer clientHandlerUID ) 
	{
		return suspendedhandlers.get ( clientHandlerUID ) ;
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
	public synchronized boolean manageConnectionLoosing ( Suspendable looser , ClientHandler < ? > connector , boolean pingAlreadyTested ) 
	{
		boolean res ;
		if ( looser.isSuspended () == false )
		{
			try 
			{
				support.notifyObservers( "onBeginSuspensionControl" , looser ) ;
				if ( ! pingAlreadyTested )
					Thread.sleep ( TimeConstants.CONNECTION_LOOSING_SERVER_SUSPENSION_TIME ) ;
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

	@Override
	public void run () 
	{
		while ( true )
		{
			try 
			{
				Thread.sleep(60000);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

}

