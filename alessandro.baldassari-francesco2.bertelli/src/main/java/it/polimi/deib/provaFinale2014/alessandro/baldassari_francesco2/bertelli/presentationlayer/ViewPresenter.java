package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Terminable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/***/
public abstract class ViewPresenter implements CommunicationProtocolResponser
{

	/***/
	private Terminable client ;
	
	/***/
	public ViewPresenter () 
	{
		client = null ;
	}
	
	public void setClientToTerminate ( Terminable client ) throws WriteOncePropertyAlreadSetException 
	{
		if ( this.client == null )
			this.client = client ;
		else
			throw new WriteOncePropertyAlreadSetException ( "CLIENT" ) ;
	}
	
	/**
	 * @throws WrongStateMethodCallException 
	 */
	protected void terminateClient () throws WrongStateMethodCallException 
	{
		if ( client != null )
			client.terminate () ;
		else
			throw new WrongStateMethodCallException () ;
	}
	
	/***/
	public abstract void startApp () ;
	
}
