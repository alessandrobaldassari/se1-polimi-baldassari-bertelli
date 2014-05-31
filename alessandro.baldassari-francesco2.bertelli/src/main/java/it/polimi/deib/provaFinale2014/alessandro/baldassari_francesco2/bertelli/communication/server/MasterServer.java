package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RequestAccepterServer;

/**
 * This class is the MasterServer, the core component of the Server part of the System
 * Architecture.
 * It is the one that sums all the inbound connections, bounds them to the GameController
 * instances and manages also the situation where just one Player wants to play ( and cannot ). 
 */
class MasterServer implements NetworkCommunicationController , MatchAdderCommunicationController , MatchStartCommunicationController
{
	
	private static final long GAME_CONTROLLER_WAITING_DELAY = 1000L ;
	
	/**
	 * The queue the technical networks input servers will use to add players requests. 
	 */
	private final BlockingQueue < ClientHandler > queue;
	
	/**
	 * A SocketServer object to intercept inbound socket connections.
	 */
	private RequestAccepterServer socketServer ;
	
	/**
	 * A RMIServer object to intercept inbound RMI connections. 
	 */
	private RequestAccepterServer rmiServer ;
	
	/**
	 * The GameController object associated to the Match that is currently starting. 
	 */
	private GameController currentGameController ;
	
	/**
	 * An ExecutorService object to manage all the threads this object creates. 
	 */
	private ExecutorService threadExecutor ;
	
	/**
	 * A Collection containing the handlers of the Client which are currently connecting to this Server. 
	 */
	private Collection < ClientHandler > currentClientHandlers ;
	
	/**
	 * A flag indicating if this MasterServer is on or not.
	 */
	private boolean inFunction ;
	
	/**
	 * @throws IOException if something goes wrong with the creation of the member objects. 
	 */
	MasterServer () throws IOException  
	{
		final String LOCALHOST_ADDRESS = InetAddress.getLocalHost ().getHostAddress () ;
		socketServer = RequestAccepterServer.newSocketServer ( this ) ; 
		rmiServer = RequestAccepterServer.newRMIServer ( this , LOCALHOST_ADDRESS , RMIServer.SERVER_PORT ) ; 
		queue = new LinkedBlockingQueue < ClientHandler > () ; 
		currentGameController = null ;
		threadExecutor = Executors.newCachedThreadPool () ;
		currentClientHandlers = new LinkedList < ClientHandler > () ;
		inFunction = false ;
	}
	
	/**
	 * The run method of this Runnable object.
	 * Essentially it consists of an infinite loop which has to maintain this Runnable alive. 
	 */
	@Override
	public void run () 
	{
		ClientHandler newClientHandler = null ;
		String name ;
		threadExecutor.submit ( socketServer ) ;
		threadExecutor.submit ( rmiServer ) ;
		inFunction = true ;
		while  ( inFunction )
		{
			try 
			{
				System.out.println ( "MASTER_SERVER : WAITING FOR REQUESTS" ) ;
				newClientHandler = queue.take () ;
				if ( currentGameController == null )
					createAndLaunchNewGameController () ;
				System.out.println ( "MASTER_SERVER : REQUEST CATCH" ) ;
				System.out.println ( "MASTER_SERVER : ASKING_NAME " ) ;
				name = newClientHandler.requestName () ;
				currentGameController.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler ) ) ;
				System.out.println ( "MASTER_SERVER : NAME_CATCH " + name ) ;
			} 
			catch ( WrongStateMethodCallException e )
			{
				if ( e.getActualState () == MatchState.CREATED ) // may be the Game Controller is late, give this Player another change to enter.
				{
					try 
					{
						Thread.sleep ( GAME_CONTROLLER_WAITING_DELAY ) ;
					} 
					catch ( InterruptedException e1 ) 
					{
						e1.printStackTrace();
					}
					addPlayer ( newClientHandler ) ;
				}
				else
					throw new RuntimeException ( e ) ;
			} 
			catch ( IOException e ) 
			{
				e.printStackTrace();
			} 
			catch ( InterruptedException e ) 
			{
				e.printStackTrace () ; 
			}
			
		}
	}
	
	/**
	 * Helper method that creates a new GameController and starts it. 
	 */
	private void createAndLaunchNewGameController () 
	{
		currentGameController = new GameController ( this ) ;
		threadExecutor.submit ( currentGameController ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public void addPlayer ( ClientHandler newClientHandler ) 
	{
		try 
		{
			queue.put ( newClientHandler ) ;
		}
		catch ( InterruptedException e ) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void notifyFailStartMatch () 
	{
		for ( ClientHandler c : currentClientHandlers )
			try 
			{
				c.notifyMatchWillNotStart ( ClientHandler.MATCH_WILL_NOT_START_MESSAGE );
			}
			catch ( IOException e ) 
			{
				e.printStackTrace();
			}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void notifyFinishAddingPlayers () 
	{
	
		currentGameController = null ;
		currentClientHandlers.clear () ;
	}
	
}
