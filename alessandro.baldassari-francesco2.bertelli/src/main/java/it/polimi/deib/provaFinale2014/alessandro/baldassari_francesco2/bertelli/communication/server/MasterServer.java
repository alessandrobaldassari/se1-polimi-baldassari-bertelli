package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameController.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

/**
 * This class is the MasterServer, the core component of the Server part of the System
 * Architecture.
 * It is the one that sums all the inbound connections, bounds them to the GameController
 * instances and manages also the situation where just one Player wants to play ( and cannot ). 
 */
public class MasterServer implements Runnable
{

	/**
	 * A SocketServer object to intercept inbound socket connections.
	 */
	private SocketServer socketServer ;
	
	/**
	 * A RMIServer object to intercept inbound RMI connections. 
	 */
	private RMIServer rmiServer ;
	
	/**
	 * The GameController object associated to the Match that is currently starting. 
	 */
	private GameController currentGameController ;
	
	/**
	 * An ExecutorService object to manage all the threads this object creates. 
	 */
	private ExecutorService threadExecutor ;
	
	/**
	 * A flag indicating if this MasterServer is on or not.
	 */
	private boolean inFunction ;
	
	/**
	 * @throws IOException if something goes wrong with the creation of the member objects. 
	 */
	MasterServer () throws IOException  
	{
		socketServer = new SocketServer ( this ) ;
		rmiServer = new RMIServerImpl ( this ) ;
		currentGameController = null ;
		threadExecutor = Executors.newCachedThreadPool () ;
		inFunction = false ;
	}
	
	/**
	 * The run method of this Runnable object.
	 * Essentially it consists of an infinite loop which has to maintain this Runnable alive. 
	 */
	public void run () 
	{
		inFunction = true ;
		threadExecutor.submit ( socketServer ) ;
		createAndLaunchNewGameController () ;
		System.out.println ( "Master Server : Listening" ) ;
		while  ( inFunction ) ;
	}

	/**
	 * This method is called by a technical server to notify that a Player contacted him
	 * and asked him to add a Player to a Match.
	 * It asks the clientHandler that the technical server passed to it to request the user
	 * his name; if everything goes well, then add the player 
	 */
	public synchronized void addPlayer ( ClientHandler newClientHandler ) 
	{
		String name ;
		try 
		{
			System.out.println ( "Server wants to request the name" );
			name = newClientHandler.requestName () ;
			currentGameController.addPlayerAndCheck ( new NetworkCommunicantPlayer ( name ) ) ;
		} 
		catch ( WrongStateMethodCallException e ) 
		{
			if ( e.getActualState () == MatchState.CREATED )
				addPlayer ( newClientHandler ) ;
			else
				throw new RuntimeException ( e ) ;
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public synchronized void notifyFailure () 
	{
		createAndLaunchNewGameController () ;
	}
	
	/***/
	public synchronized void notifyFinishAddingPlayers () 
	{
		createAndLaunchNewGameController () ;
	}
	
	private void createAndLaunchNewGameController () 
	{
		currentGameController = new GameController ( this ) ;
		threadExecutor.submit ( currentGameController ) ;
	}
	
}
