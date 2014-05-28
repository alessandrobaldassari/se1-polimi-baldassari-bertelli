package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
	 * The queue the technical networks input servers will use to add players requests. 
	 */
	private final BlockingQueue<ClientHandler> queue;
	
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
		queue = new LinkedBlockingQueue<ClientHandler>(); 
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
		RMIServer stub ;
		Registry registry ;
		String name ;
		ClientHandler newClientHandler;
		try 
		{
			inFunction = true ;
			threadExecutor.submit ( socketServer ) ;
			stub = ( RMIServer ) UnicastRemoteObject.exportObject ( rmiServer , 0 ) ;
			registry = LocateRegistry.createRegistry ( RMIServer.RMI_SERVER_PORT ) ;
			registry.rebind ( RMIServer.SERVER_NAME , stub ) ;
			System.out.println ( "Master Server : Listening" ) ;
			while  ( inFunction )
			{
				newClientHandler = queue.poll();
				while (newClientHandler == null) 
					newClientHandler = queue.poll();
				if ( currentGameController == null )
					createAndLaunchNewGameController () ;
				try 
				{
					System.out.println ( "Server wants to request the name" );
					name = newClientHandler.requestName () ;
					currentGameController.addPlayerAndCheck ( new NetworkCommunicantPlayer ( name, newClientHandler ) ) ;
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
		} 
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is called by a technical server to notify that a Player contacted him
	 * and asked him to add a Player to a Match.
	 * It asks the clientHandler that the technical server passed to it to request the user
	 * his name; if everything goes well, then add the player 
	 */
	public synchronized void addPlayer ( ClientHandler newClientHandler ) 
	{
		try {
			queue.put ( newClientHandler ) ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that manages the situation where a GameController notifies that 
	 * a timeout expires and there is a number of Player < 2, so the match will not start.
	 * The methods cancel the Match and notify the added Client that his request has been
	 * rejected.
	 */
	public synchronized void notifyFailure () 
	{
		// notify the rejected player.
	}
	
	/**
	 * Method that manages the situation where the current GameController notifies that
	 * it has reached the situation where a Match can start.
	 * Standing the contract of the GameController class, it will start the workflow 
	 * automatically, so the only thing this method has to do is to set the 
	 * currentGameController property to null.
	 */
	public synchronized void notifyFinishAddingPlayers () 
	{
		currentGameController = null ;
	}
	
	/**
	 * Helper method that creates a new GameController and starts it. 
	 */
	private void createAndLaunchNewGameController () 
	{
		currentGameController = new GameController ( this ) ;
		threadExecutor.submit ( currentGameController ) ;
	}
	
}
