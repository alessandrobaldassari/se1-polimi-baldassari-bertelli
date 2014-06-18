package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.TimeConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerWantsToExitGameException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.RMIGUIUpdaterServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.ConnectionLoosingManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;

/**
 * This class is the MasterServer, the core component of the Server part of the System
 * Architecture.
 * It is the one that sums all the inbound connections, bounds them to the GameController
 * instances and manages also the situation where just one Player wants to play ( and cannot ). 
 */
public class MatchLauncherServer implements MatchPlayerAdder , Runnable , MatchStarter 
{
	
	/**
	 * The queue the technical networks input servers will use to add players requests. 
	 */
	private final BlockingQueue < ClientHandler < ? > > queue;

	/***/
	private ConnectionLoosingManager connectionLoosingManager ;

	/**
	 * The GameController object associated to the Match that is currently starting. 
	 */
	private MatchController currentMatchController ;
	
	/**
	 * A Server to manage the gui messages with the client.
	 */
	private RMIGUIUpdaterServer currentGuiServer ;
	
	/***/
	private MatchLauncherSession session ;
	
	/**
	 * A flag indicating if this MasterServer is on or not.
	 */
	private boolean inFunction ;
	
	/**
	 * @throws IOException if something goes wrong with the creation of the member objects. 
	 */
	public MatchLauncherServer ( ConnectionLoosingManager connectionLoosingManager ) throws IOException  
	{
		this.connectionLoosingManager = connectionLoosingManager ;
		queue = new LinkedBlockingQueue < ClientHandler < ? > > () ; 
		session = null ;
		currentMatchController = null ;
		currentGuiServer = null ;
		inFunction = false ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * Essentially it consists of an infinite loop which has to maintain this Runnable alive. 
	 */
	@Override
	public void run () 
	{
		ClientHandler < ? > newClientHandler = null ;
		inFunction = true ;
		System.out.println ( "MASTER SERVER : INIZIO FUNZIONAMENTO" ) ;
		while  ( inFunction )
		{
			try 
			{
				System.out.println ( "MASTER SERVER : ATTENDENDO CLIENT" ) ;
				newClientHandler = queue.take () ;
				System.out.println ( "MASTER SERVER : CLIENT ACCETTATO" ) ;
				synchronized ( this )
				{
					// se è il primo giocatore, avvia una nuova partita
					if ( session == null )
						initializeNewSession () ;
				}
				addNewPlayer ( newClientHandler ) ;
				// notify the others of the new ?
			}
			catch ( InterruptedException e ) 
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : INTERRUPTED BY " + Thread.currentThread() ) ;
			} 
		}
	}
	
	/***/
	private synchronized void addNewPlayer ( ClientHandler < ? > newClientHandler ) 
	{
		String name ;
		name = null ;
		try
		{
			System.out.println ( "MASTER SERVER : CHIEDENDO NOME AL CLIENT" ) ;
			// chiedi il nome
			name = newClientHandler.requestName () ;
			System.out.println ( "MASTER SERVER : NOME RICEVUTO DAL CLIENT, nome = " + name ) ;
			// assicurati di prendere un nome corretto
			while ( name.trim ().isEmpty () || session.containsName ( name ) )
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
			currentMatchController.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler , connectionLoosingManager ) ) ;					
			// notify other players of the new one.
			notifyAll () ;
		}
		catch ( WrongMatchStateMethodCallException e )
		{
			System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : WrongMatchStateMethodCallException GENERATED" ) ;
			// may be the Game Controller is late, give this Player another change to enter.
			if ( e.getActualState () == MatchState.CREATED ) 
			{
				try 
				{
					Thread.sleep ( TimeConstants.MATCH_LAUNCHER_SERVER_WAITING_TIME ) ;
					currentMatchController.addPlayer ( new NetworkCommunicantPlayer ( name, newClientHandler , connectionLoosingManager ) ) ;
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
				// notify the user...
				System.out.println ( "UNEXPECTED FLOW, THIS SITUATION SHOULD NEVER HAPPEN." ) ;
				throw new RuntimeException ( e ) ;
			}
		} 
		catch ( IOException e ) 
		{
			System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - RUN : IOEXCEPTION " + e.getMessage() ) ;	
		} 
		catch ( PlayerWantsToExitGameException e )
		{
			System.out.println ( "MATCH_LAUNCHER_COMUNICATION_CONTROLLER - RUN : PLAYER_WANTS_TO_EXIT_GAME_EXCEPTION" ) ;
		}
	}
	
	/**
	 * Helper method that creates a new GameController and starts it. 
	 */
	private void initializeNewSession () 
	{
		// to move if we want to break down the executor at the end.
		Executor fixedExecutor ;
		try 
		{
			currentGuiServer = new RMIGUIUpdaterServer () ;
			currentMatchController = new MatchController ( this , Collections. <GameMapObserver>singleton( currentGuiServer ) ) ;
			session = new MatchLauncherSession () ;
			fixedExecutor = Executors.newFixedThreadPool ( 2 ) ;
			connectionLoosingManager.addObserver ( currentMatchController ) ;
			fixedExecutor.execute ( currentMatchController ) ;
			fixedExecutor.execute ( currentGuiServer ) ;
		}
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public void addPlayer ( ClientHandler < ? > newClientHandler ) 
	{
		try 
		{
			queue.put ( newClientHandler ) ;
		}
		catch ( InterruptedException e ) 
		{
			System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - ADD_PLAYER : THREAD " + Thread.currentThread() + " INTERRUPTED HERE" ) ;
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
				c.notifyMatchWillNotStart ( PresentationMessages.MATCH_WILL_NOT_START_MESSAGE ) ;
			}
			catch ( IOException e ) 
			{
				System.out.println ( "MATCH_LAUNCHER_COMMUNICATION_CONTROLLER - NOTIFY_FAIL_START_MATCH : IOEXCEPTION WITH THE CLIENT HANDLER " + c ) ;		
			}
		clearMatchEnvironment () ;
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
				guiHandler = currentGuiServer.addClient () ;
				currentMatchController.addPlayerObserver ( currentGuiServer.getPlayerObserver ( guiHandler  ) , c.getUID() );
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
