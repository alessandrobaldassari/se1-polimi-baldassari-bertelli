package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

/**
 * This class is the MasterServer, the core component of the Server part of the System
 * Architecture.
 * It is the one that sums all the inbound connections, bounds them to the GameController
 * instances and manages also the situation where just one Player wants to play ( and cannot ). 
 */
class MasterServer implements NetworkCommunicationController , MatchAdderCommunicationController , MatchStartCommunicationController
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
		final String LOCALHOST_ADDRESS = InetAddress.getLocalHost ().getHostAddress () ;
		final int SOCKET_SERVER_PORT = 3333 ;
		socketServer = new SocketServer ( SOCKET_SERVER_PORT , this ) ;
		rmiServer = new RMIServerImpl ( LOCALHOST_ADDRESS , RMIServer.SERVER_PORT , this ) ;
		queue = new LinkedBlockingQueue < ClientHandler > () ; 
		currentGameController = null ;
		threadExecutor = Executors.newCachedThreadPool () ;
		inFunction = false ;
	}
	
	/**
	 * The run method of this Runnable object.
	 * Essentially it consists of an infinite loop which has to maintain this Runnable alive. 
	 */
	@Override
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
			registry = LocateRegistry.createRegistry ( RMIServer.SERVER_PORT ) ;
			stub = ( RMIServer ) UnicastRemoteObject.exportObject ( rmiServer , 0 ) ;
			registry.rebind ( RMIServer.LOGICAL_SERVER_NAME , stub ) ;
			while  ( inFunction )
			{
				newClientHandler = queue.poll();
				while (newClientHandler == null) 
					newClientHandler = queue.poll();
				if ( currentGameController == null )
					createAndLaunchNewGameController () ;
				try 
				{
					name = newClientHandler.requestName () ;
					System.out.println ( "NAMENANEMANEMNE " + name ) ;
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
	 * AS THE SUPER'S ONE.
	 */
	public synchronized void addPlayer ( ClientHandler newClientHandler ) 
	{
		try 
		{
			queue.put ( newClientHandler ) ;
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	public synchronized void notifyFailStartMatch () 
	{
		// notify the rejected player.
	}
	
	/**
	 * AS THE SUPER'S ONE. 
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
