package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.ServerEnvironment;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandServerApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.TimeConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerWantsToExitGameException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.NetworkCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.RMIGUIMapServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.BackendResumeConnectionServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingControllerImpl;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RequestAccepterServer;

/**
 * This class is the MasterServer, the core component of the Server part of the System
 * Architecture.
 * It is the one that sums all the inbound connections, bounds them to the GameController
 * instances and manages also the situation where just one Player wants to play ( and cannot ). 
 */
public class MatchLauncherCommunicationController implements NetworkCommunicationController , MatchAdderCommunicationController , MatchStartCommunicationController , Serializable
{
		
	/**
	 * An instance of this class to implement the Singleton pattern. 
	 */
	private static MatchLauncherCommunicationController instance ;
	
	/**
	 * A SocketServer object to intercept inbound socket connections.
	 */
	private transient RequestAccepterServer socketServer ;
	
	/**
	 * A RMIServer object to intercept inbound RMI connections. 
	 */
	private transient RequestAccepterServer rmiServer ;
	
	/**
	 * A component associated with the current MatchController to manage eventually connection loosing. 
	 */
	private transient ConnectionLoosingController connectionLoosingController ;
	
	/***/
	private transient BackendResumeConnectionServer resumeConnectionServer ;
	
	/**
	 * A Server to manage the gui messages with the client, one for all sessions.
	 */
	private transient RMIGUIMapServer guiServer ;
	
	/**
	 * The queue the technical networks input servers will use to add players requests. 
	 */
	private final BlockingQueue < ClientHandler < ? > > queue;
	
	/**
	 * A flag indicating if this MasterServer is on or not.
	 */
	private boolean inFunction ;
	
	private MatchLauncherSession session ;
	
	/**
	 * @throws IOException if something goes wrong with the creation of the member objects. 
	 */
	protected MatchLauncherCommunicationController () throws IOException  
	{
		socketServer = RequestAccepterServer.newSocketServer ( this ) ; 
		rmiServer = RequestAccepterServer.newRMIServer ( this , ServerEnvironment.getInstance ().getLocalhostIPAddress () , ServerEnvironment.RMI_REQUEST_ACCEPT_SERVER_PORT ) ; 
		guiServer = new RMIGUIMapServer () ;
		queue = new LinkedBlockingQueue < ClientHandler < ? > > () ; 
		connectionLoosingController = new ConnectionLoosingControllerImpl () ;
		resumeConnectionServer = new BackendResumeConnectionServer ( connectionLoosingController ) ;
		inFunction = false ;
		session = null ;
		SheeplandServerApp.getInstance().executeRunnable ( resumeConnectionServer ) ;
		SheeplandServerApp.getInstance().executeRunnable ( guiServer ) ;
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
		SheeplandServerApp.getInstance().executeRunnable ( socketServer ) ;
		SheeplandServerApp.getInstance().executeRunnable ( rmiServer ) ;
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
					// se è il primo giocatore, avvia una nuova partita
					if ( session == null )
						createAndLaunchNewGameController () ;
					System.out.println ( "MASTER SERVER : CHIEDENDO NOME AL CLIENT" ) ;
					// chiedi il nome
					name = newClientHandler.requestName () ;
					System.out.println ( "MASTER SERVER : NOME RICEVUTO DAL CLIENT, nome = " + name ) ;
					// assicurati di prendere un nome corretto
					while ( session.containsName ( name ) )
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
					// registra il nuovo giocatore
					session.addHandler ( newClientHandler ) ;
					session.addName ( name ) ;
					newClientHandler.notifyNameChoose ( true , "Nome ammesso." ) ;
					System.out.println ( "CLIENT DI NOME " + name + " NOTIFICATO CHE IL SUO NOME E' CORRETTO" ) ;
					System.out.println ( "MASTER SERVER : PLAYER DI NOME " + name + "AGGIUNTO ALLA PARTITA" ) ;
					session.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler , connectionLoosingController ) ) ;					
					notifyAll () ;
				} 
			}
			catch ( WrongMatchStateMethodCallException e )
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : WrongMatchStateMethodCallException GENERATED" ) ;
				// may be the Game Controller is late, give this Player another change to enter.
				if ( e.getActualState () == MatchState.CREATED ) 
				{
					try 
					{
						Thread.sleep ( TimeConstants.MATCH_LAUNCHER_COMMUNICATION_CONTROLLER_WAITING_TIME ) ;
						session.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler , connectionLoosingController ) ) ;
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
				System.out.println ( "MATCH_LAUNCHER_COMUNICATION_CONTROLLER - RUN : PLAYER_WANTS_TO_EXIT_GAME_EXCEPTION" ) ;
			}
		}
	}
	
	/**
	 * Helper method that creates a new GameController and starts it. 
	 */
	private void createAndLaunchNewGameController () 
	{
		session = new MatchLauncherSession ( this , guiServer ) ;
		connectionLoosingController.addObserver ( session.getMatchController () ) ;
		SheeplandServerApp.getInstance().executeRunnable ( session.getMatchController() ); 
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
		for ( ClientHandler < ? > c : session.getClientHandlers () )
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
		String guiHandler ;
		System.out.println ( "MASTER SERVER : notifyFinishAddingPlayers" ) ;
		for ( ClientHandler < ? > c : session.getClientHandlers () )
			try 
			{
				c.uidNotification () ;
				guiHandler = guiServer.addClient () ;
				session.addPlayerObserver ( guiServer.getPlayerObserver ( guiHandler  ) , c.getUID() );
				c.sendGuiConnectorNotification ( guiHandler ) ;
				c.notifyMatchStart();
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
		inFunction = false ;
	}
	
	/**
	 * Helper method to remove all the resources associated with a Match that is no longer managed by
	 * this Controller. 
	 */
	private void clearMatchEnvironment () 
	{
		session = null ;
	}
	
}
