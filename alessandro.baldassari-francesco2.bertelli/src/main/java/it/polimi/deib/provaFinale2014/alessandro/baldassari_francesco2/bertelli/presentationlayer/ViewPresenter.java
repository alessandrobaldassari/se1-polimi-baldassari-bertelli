package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Terminable;

/***/
public abstract class ViewPresenter 
{

	/***/
	private Terminable client ;
	
	/***/
	public ViewPresenter ( Terminable client ) 
	{
		if ( client != null )
			this.client = client ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	protected void terminateClient () 
	{
		client.terminate () ;
	}
	
	/***/
	public abstract void startApp () ;
	
}
