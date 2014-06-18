package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlauncherserver;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

import java.util.Collection;
import java.util.LinkedList;

class MatchLauncherSession 
{

	/**
	 * A Collection containing the handlers of the Client which are currently connecting to this Server. 
	 */
	private Collection < ClientHandler < ? > > clientHandlers ;
	
	/**
	 * The name of the clients connected with the current Game Controller 
	 */
	private Collection < String > clientNames ;

	/***/
	public MatchLauncherSession () 
	{
		clientHandlers = new LinkedList < ClientHandler < ? > > () ;
		clientNames = new LinkedList < String > () ;
	}
	
	public void addHandler ( ClientHandler < ? > clientHandler ) 
	{
		clientHandlers.add ( clientHandler ) ;
	}
	
	public Iterable < ClientHandler < ? > > getClientHandlers () 
	{
		return clientHandlers ;
	}
	
	public void addName ( String name ) 
	{
		clientNames.add ( name ) ;
	}
	
	public boolean containsName ( String name ) 
	{
		return clientNames.contains ( name ) ;
	}
	
}
