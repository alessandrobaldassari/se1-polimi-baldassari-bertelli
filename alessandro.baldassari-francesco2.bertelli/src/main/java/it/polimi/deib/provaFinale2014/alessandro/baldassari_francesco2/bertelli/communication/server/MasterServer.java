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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
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
	
	/***/
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
	
	/***/
	private Collection < String > currentClientNames ;
	
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
		currentClientNames = new LinkedList < String > () ;
		inFunction = false ;
	}
	
	/**
	 * AS THE SUPER0'S ONE.
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
		System.out.println ( "MASTER SERVER : INIZIO FUNZIONAMENTO" ) ;
		while  ( inFunction )
		{
			try 
			{
				System.out.println ( "MASTER SERVER : ATTENDENDO CLIENT" ) ;
				newClientHandler = queue.take () ;
				System.out.println ( "MASTER SERVER : CLIENT ACCETTATO" ) ;
				if ( currentGameController == null )
					createAndLaunchNewGameController () ;
				System.out.println ( "MASTER SERVER : CHIEDENDO NOME AL CLIENT" ) ;
				name = newClientHandler.requestName () ;
				System.out.println ( "MASTER SERVER : NOME RICEVUTO DAL CLIENT, nome = " + name ) ;
				while ( currentClientNames.contains ( name ) )
				{
					System.out.println ( "MASTER SERVER : NOME " + name + " GIA' IN USO" ) ;
					System.out.println ( "MASTER SERVER : NOTIFICA AL CLIENT DI NOME + " + name + " NOME GIA' IN USO" ) ;
					newClientHandler.notifyNameChoose ( false , "Nome già in uso." ) ;					
					System.out.println ( "MASTER SERVER : CHIEDENDO NOME AL CLIENT" ) ;
					name = newClientHandler.requestName () ;
					System.out.println ( "MASTER SERVER : NOME RICEVUTO DAL CLIENT, nome = " + name ) ;
				}
				System.out.println ( "MASTER SERVER : NOME + " +name + " NON IN USO" ) ;
				System.out.println ( "MASTER SERVER : AGGIUNGENDO PLAYER ALLA PARTITA DI NOME " + name ) ;
				currentGameController.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler ) ) ;
				System.out.println ( "MASTER SERVER : PLAYER DI NOME " + name + "AGGIUNTO ALLA PARTITA" ) ;
				currentClientHandlers.add ( newClientHandler ) ;
				currentClientNames.add ( name ) ;
				System.out.println ( "MASTER SERVER : NOTIFICA AL CLIENT DI NOME " + name + "CHE IL SUO NOME E' CORRETTO" ) ;
				newClientHandler.notifyNameChoose ( true , null ) ;
			} 
			catch ( WrongMatchStateMethodCallException e )
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
		notifyFinishAddingPlayers () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void notifyFinishAddingPlayers () 
	{
		for ( ClientHandler c : currentClientHandlers )
			try 
			{
				c.notifyMatchStart () ;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		currentGameController = null ;
		currentClientHandlers.clear () ;
		currentClientNames.clear () ;
	}
	
}
