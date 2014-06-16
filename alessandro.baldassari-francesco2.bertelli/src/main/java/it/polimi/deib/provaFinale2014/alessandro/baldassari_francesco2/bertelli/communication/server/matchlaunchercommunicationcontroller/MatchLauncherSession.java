package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.RMIGUIMapServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

class MatchLauncherSession 
{

	/**
	 * The GameController object associated to the Match that is currently starting. 
	 */
	private MatchController matchController ;
	
	/**
	 * A Collection containing the handlers of the Client which are currently connecting to this Server. 
	 */
	private Collection < ClientHandler < ? > > clientHandlers ;
	
	/**
	 * The name of the clients connected with the current Game Controller 
	 */
	private Collection < String > clientNames ;

	/***/
	public MatchLauncherSession ( MatchLauncherCommunicationController matchLauncherCommunicationController , RMIGUIMapServer guiMapServer ) 
	{
		matchController = new MatchController ( matchLauncherCommunicationController , Collections.<GameMapObserver>singleton ( guiMapServer ) ) ;
		clientHandlers = new LinkedList < ClientHandler < ? > > () ;
		clientNames = new LinkedList < String > () ;
	}
	
	public void addPlayer ( Player player ) throws WrongMatchStateMethodCallException 
	{
		matchController.addPlayer ( player ) ; 
	}
	
	/***/
	public MatchController getMatchController () 
	{
		return matchController ;
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
