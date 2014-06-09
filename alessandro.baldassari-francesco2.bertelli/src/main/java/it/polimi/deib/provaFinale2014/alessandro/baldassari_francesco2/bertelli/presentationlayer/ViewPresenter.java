package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer;

import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Terminable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This component is represents the entity responsible for the App presentation layer. 
 */
public abstract class ViewPresenter implements CommunicationProtocolResponser
{

	/**
	 * A reference to terminate the network client thread when the App will be closed. 
	 */
	private Terminable client ;
	
	/***/
	public ViewPresenter () 
	{
		client = null ;
	}
	
	/**
	 * @param client 
	 */
	public void setClientToTerminate ( Terminable client ) throws WriteOncePropertyAlreadSetException 
	{
		if ( this.client == null )
			this.client = client ;
		else
			throw new WriteOncePropertyAlreadSetException ( "CLIENT" ) ;
	}
	
	/**
	 * Subclasses has to implement this method as a startpoint for the App. 
	 */
	public abstract void startApp () ;
	
	/***/
	public void stopApp () 
	{	
		try 
		{
			onTermination () ;
			terminateClient () ;
			System.exit ( 0 ) ;
		}
		catch ( WrongStateMethodCallException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( IOException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
	}
	
	protected abstract void onTermination () throws IOException ;
	
	/**
	 * @throws WrongStateMethodCallException 
	 */
	private void terminateClient () throws WrongStateMethodCallException 
	{
		if ( client != null )
			client.terminate () ;
		else
			throw new WrongStateMethodCallException () ;
	}
	
}
