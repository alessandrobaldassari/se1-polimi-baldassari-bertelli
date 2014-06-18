package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.ServerEnvironment;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.connectionresuming.ConnectionResumerServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.ConnectionLoosingServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching.MatchLauncherServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RequestAccepterServer;

/**
 * This interface represents a Component that is responsible for the Server Network Communication. 
 */
public class JSheeplandServer implements Runnable 
{
	
	/**
	 * An instance of this class to implement the Singleton pattern.
	 */
	private static JSheeplandServer instance ;
	
	/***/
	private MatchLauncherServer matchLauncherCommunicationController ; 

	/**
	 * A SocketServer object to intercept inbound socket connections.
	 */
	private RequestAccepterServer socketServer ;
	
	/**
	 * A RMIServer object to intercept inbound RMI connections. 
	 */
	private RequestAccepterServer rmiServer ;
	
	/**
	 * A component associated with the current MatchController to manage eventually connection loosing. 
	 */
	private ConnectionLoosingServer connectionLoosingServer ;
	
	/***/
	private ConnectionResumerServer socketConnectionResumerServer ;
	
	/***/
	private ConnectionResumerServer rmiConnectionResumerServer ;
	
	/***/
	private Executor fixedExecutorService ;
		
	/***/
	private boolean isActive ;
	
	/**
	 * @throws IOException 
	 */
	private JSheeplandServer () throws IOException 
	{
		connectionLoosingServer = new ConnectionLoosingServer () ;
		socketConnectionResumerServer = ConnectionResumerServer.newSocketServer ( connectionLoosingServer ) ;
		rmiConnectionResumerServer = ConnectionResumerServer.newRMIServer ( connectionLoosingServer ) ;
		matchLauncherCommunicationController = new MatchLauncherServer ( connectionLoosingServer ) ; 
		socketServer = RequestAccepterServer.newSocketServer ( matchLauncherCommunicationController ) ; 
		rmiServer = RequestAccepterServer.newRMIServer ( matchLauncherCommunicationController , ServerEnvironment.getInstance ().getLocalhostIPAddress () , ServerEnvironment.RMI_REQUEST_ACCEPT_SERVER_PORT ) ; 
		fixedExecutorService = Executors.newFixedThreadPool ( 6 ) ;
	}
	
	/**
	 * @throws IOException */
	public static synchronized JSheeplandServer getInstance () throws IOException 
	{
		if ( instance == null )
			instance = new JSheeplandServer () ;
		return instance ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void run() 
	{
		try 
		{
			// active this Server
			isActive = true ;
			// launchers the threads
			socketConnectionResumerServer.connect();
			rmiConnectionResumerServer.connect();
			fixedExecutorService.execute ( connectionLoosingServer ) ;
			fixedExecutorService.execute ( matchLauncherCommunicationController ) ;
			fixedExecutorService.execute ( socketConnectionResumerServer ) ;
			fixedExecutorService.execute ( rmiConnectionResumerServer ) ;
			fixedExecutorService.execute ( socketServer ) ;
			fixedExecutorService.execute ( rmiServer ) ;
			while ( isActive )
			{
				try 
				{	
					Thread.sleep(60000);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace () ;
				}
			}
		}
		catch (IOException e1 ) 
		{
			e1.printStackTrace();
		}
		
	}
	
}
