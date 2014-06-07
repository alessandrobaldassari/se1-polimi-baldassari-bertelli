package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

public class ClientHandlerConnector < T > 
{

	private T conn ;
	
	public ClientHandlerConnector ( T conn ) 
	{
		if ( conn != null )
			this.conn = conn ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public T getConnector () 
	{
		return conn ;
	}
	
	
	
}
