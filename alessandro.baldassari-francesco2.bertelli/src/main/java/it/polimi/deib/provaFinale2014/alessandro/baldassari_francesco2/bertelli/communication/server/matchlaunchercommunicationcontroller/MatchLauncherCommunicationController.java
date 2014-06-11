package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.PlayerWantsToExitGameException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.NetworkCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.guimap.SocketGUIMapServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingControllerImpl;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.RMIResumerConnectionServerImpl;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ResumeConnectionServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.SocketResumeConnectionServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIRequestAcceptServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RequestAccepterServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

/**
 * This class is the MasterServer, the core component of the Server part of the System
 * Architecture.
 * It is the one that sums all the inbound connections, bounds them to the GameController
 * instances and manages also the situation where just one Player wants to play ( and cannot ). 
 */
public class MatchLauncherCommunicationController implements NetworkCommunicationController , MatchAdderCommunicationController , MatchStartCommunicationController
{
	
	/**
	 * The time this Controller has to wait w.r.t the Game Controller during the Match starting. 
	 */
	public static final long WAITING_TIME = 15 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/**
	 * An instance of this class to implement the Singleton pattern. 
	 */
	private static MatchLauncherCommunicationController instance ;
	
	/**
	 * The queue the technical networks input servers will use to add players requests. 
	 */
	private final BlockingQueue < ClientHandler < ? > > queue;
	
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
	private MatchController currentGameController ;
	
	/**
	 * An ExecutorService object to manage all the threads this object creates. 
	 */
	private ExecutorService threadExecutor ;
	
	/**
	 * A Collection containing the handlers of the Client which are currently connecting to this Server. 
	 */
	private Collection < ClientHandler < ? > > currentClientHandlers ;
	
	/**
	 * The name of the clients connected with the current Game Controller 
	 */
	private Collection < String > currentClientNames ;
	
	/**
	 * A component associated with the current GameController to manage eventually connection loosing. 
	 */
	private ConnectionLoosingController connectionLoosingController ;
	
	/**
	 * A flag indicating if this MasterServer is on or not.
	 */
	private boolean inFunction ;

	/***/
	private ResumeConnectionServer < ? > socketResumeConnectionServer ;
	
	/***/
	private ResumeConnectionServer < ? > rmiResumeConnectionServer ;
	
	/***/
	private SocketGUIMapServer guiServer ;
	
	/**
	 * @throws IOException if something goes wrong with the creation of the member objects. 
	 */
	protected MatchLauncherCommunicationController () throws IOException  
	{
		final String LOCALHOST_ADDRESS = InetAddress.getLocalHost ().getHostAddress () ;
		socketServer = RequestAccepterServer.newSocketServer ( this ) ; 
		rmiServer = RequestAccepterServer.newRMIServer ( this , LOCALHOST_ADDRESS , RMIRequestAcceptServer.SERVER_PORT ) ; 
		threadExecutor = Executors.newCachedThreadPool () ;
		queue = new LinkedBlockingQueue < ClientHandler < ? > > () ; 
		currentGameController = null ;
		currentClientHandlers = new LinkedList < ClientHandler < ? > > () ;
		currentClientNames = new LinkedList < String > () ;
		connectionLoosingController = new ConnectionLoosingControllerImpl () ;
		socketResumeConnectionServer = new SocketResumeConnectionServer ( connectionLoosingController ) ; 
		rmiResumeConnectionServer = new RMIResumerConnectionServerImpl ( connectionLoosingController ) ;
		inFunction = false ;
		threadExecutor.execute ( socketResumeConnectionServer ) ;
		threadExecutor.execute ( rmiResumeConnectionServer ) ;
	}
	
	/**
	 * Singleton method for this class
	 * 
	 * @return the only existing of this class.
	 * @throws IOException if an instance can not be created.
	 */
	public static synchronized MatchLauncherCommunicationController getInstance () throws IOException
	{
		if ( instance == null )
			instance = new MatchLauncherCommunicationController () ;
		return instance ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * Essentially it consists of an infinite loop which has to maintain this Runnable alive. 
	 */
	@Override
	public void run () 
	{
		ClientHandler < ? > newClientHandler = null ;
		String name ;
		threadExecutor.submit ( socketServer ) ;
		threadExecutor.submit ( rmiServer ) ;
		inFunction = true ;
		name = null ;
		System.out.println ( "MASTER SERVER : INIZIO FUNZIONAMENTO" ) ;
		while  ( inFunction )
		{
			try 
			{
				System.out.println ( "MASTER SERVER : ATTENDENDO CLIENT" ) ;
				newClientHandler = queue.take () ;
				synchronized ( this )
				{
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
					currentClientHandlers.add ( newClientHandler ) ;
					currentClientNames.add ( name ) ;
					newClientHandler.notifyNameChoose ( true , "Nome ammesso." ) ;
					System.out.println ( "CLIENT DI NOME " + name + " NOTIFICATO CHE IL SUO NOME E' CORRETTO" ) ;
					System.out.println ( "MASTER SERVER : PLAYER DI NOME " + name + "AGGIUNTO ALLA PARTITA" ) ;
					currentGameController.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler , connectionLoosingController ) ) ;
					notifyAll () ;
				} 
			}
			catch ( WrongMatchStateMethodCallException e )
			{
				// may be the Game Controller is late, give this Player another change to enter.
				if ( e.getActualState () == MatchState.CREATED ) 
				{
					try 
					{
						Thread.sleep ( WAITING_TIME ) ;
						currentGameController.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler , connectionLoosingController ) ) ;
					} 
					catch ( InterruptedException e1 ) 
					{
						System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : INTERRUPTED BY " + Thread.currentThread() ) ;
					}
					catch ( WrongMatchStateMethodCallException e1 ) 
					{
						System.out.println ( "THE GAME CONTROLLER HAS TOO LOW SPEED, SOMETHING MAY BE KO." ) ;
						throw new RuntimeException ( e1 ) ;
					}
					finally 
					{
						notifyAll () ;
					}
				}
				else
				{
					System.out.println ( "UNEXPECTED FLOW, THIS SITUATION SHOULD NEVER HAPPEN." ) ;
					throw new RuntimeException ( e ) ;
				}
			} 
			catch ( IOException e ) 
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : IOEXCEPTION " + e.getMessage() ) ;	
			} 
			catch ( InterruptedException e ) 
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : INTERRUPTED BY " + Thread.currentThread() ) ;
			} 
			catch ( PlayerWantsToExitGameException e )
			{
				// notify others ?
			}
		}
	}
	
	/**
	 * Helper method that creates a new GameController and starts it. 
	 */
	private void createAndLaunchNewGameController () 
	{
		currentGameController = new MatchController ( this ) ;
		try {
			guiServer = new SocketGUIMapServer () ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentClientHandlers.clear () ;
		currentClientNames.clear () ;
		connectionLoosingController.addObserver ( currentGameController ) ;
		threadExecutor.submit ( currentGameController ) ;
		threadExecutor.submit ( guiServer ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public synchronized void addPlayer ( ClientHandler < ? > newClientHandler ) 
	{
		try 
		{
			queue.put ( newClientHandler ) ;
		}
		catch ( InterruptedException e ) 
		{
			System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - ADD_PLAYER : THREAD " + Thread.currentThread() + " INTERRUPTED HERE" ) ;
		}
		finally 
		{
			notifyAll () ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void notifyFailStartMatch () 
	{
		System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - NOTIFY_FAIL_START_MATCH : NOTIFICA CLIENTS CHE IL MATCH NON PARTIRA'" ) ;
		for ( ClientHandler < ? > c : currentClientHandlers )
			try 
			{
				c.notifyMatchWillNotStart ( ClientHandler.MATCH_WILL_NOT_START_MESSAGE ) ;
			}
			catch ( IOException e ) 
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - NOTIFY_FAIL_START_MATCH : IOEXCEPTION WITH THE CLIENT HANDLER " + c ) ;		
			}
		clearMatchEnvironment () ;
		notifyAll () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void notifyFinishAddingPlayers () 
	{
		System.out.println ( "MASTER SERVER : NOTIFICA AI CLIENT CHE IL MATCH STA PARTENDO" ) ;
		for ( ClientHandler < ? > c : currentClientHandlers )
			try 
			{
				c.uidNotification () ;
				c.sendGuiConnectorNotification ( guiServer.getPort () ) ;
				c.notifyMatchStart () ;
			}
			catch ( IOException e ) 
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - NOTIFY FINISH ADDING PLAYERS : IOEXCEPTION WITH THE CLIENT HANDLER " + c ) ; 				
			}
		clearMatchEnvironment () ;
		notifyAll () ;
	}
	
	/**
	 * Shut down this Controller. 
	 */
	public void shutDown () 
	{
		threadExecutor.shutdownNow () ;
		inFunction = false ;
	}
	
	/**
	 * Helper method to remove all the resources associated with a Match that is no longer managed by
	 * this Controller. 
	 */
	private void clearMatchEnvironment () 
	{
		currentGameController = null ;
		connectionLoosingController = null ;
		guiServer = null ;
		currentClientHandlers.clear () ;
		currentClientNames.clear () ;
	}
	
}
