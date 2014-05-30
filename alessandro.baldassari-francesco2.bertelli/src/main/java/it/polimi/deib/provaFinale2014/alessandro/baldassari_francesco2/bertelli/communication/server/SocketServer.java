package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.SocketClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  
 */
public class SocketServer implements Runnable
{

	/**
	 * The port where this Server Socket will be listening.
	 */
	public final int TCP_LISTENING_PORT ;
	
	/**
	 * The ServerSocket object that will be used to listen for inbound Socket connections. 
	 */
	private final ServerSocket serverSocket ;

	/**
	 * The object used by this Server to communicate that a new Client has come 
	 */
	private final MatchAdderCommunicationController matchAdderCommunicationController ;
	
	/**
	 * A boolean flag indicating if this SocketServer is on. 
	 */
	private boolean inFunction ;
	
	/**
	 * @param tcpListeningPort 
	 */
	SocketServer ( int tcpListeningPort , MatchAdderCommunicationController matchAdderCommunicationController ) throws IOException 
	{
		if ( tcpListeningPort >= 0 && matchAdderCommunicationController != null )
		{
			TCP_LISTENING_PORT = tcpListeningPort ;
			this.matchAdderCommunicationController = matchAdderCommunicationController ;
			serverSocket = new ServerSocket ( TCP_LISTENING_PORT  ) ;
			inFunction = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This method implements this SocketServer lifecycle.
	 * It listen for inbound connections; when receives one, it creates an handler for it
	 * and then submit this handler to the matchAdderCommunicationController.
	 */
	public void run () 
	{
		ClientHandler clientHandler ;
		Socket client ;
		inFunction = true ;
		while ( inFunction ) 
		{
			try 
			{
				client = serverSocket.accept () ;
				System.out.println ( "Socket Server : Connection Accepted" ) ;
				clientHandler = new SocketClientHandler ( client ) ;
				System.out.println ( "Socket Server : Pre Adding Player" ) ;
				matchAdderCommunicationController.addPlayer ( clientHandler ) ;
			}
			catch ( IOException e ) 
			{
				e.printStackTrace () ;
			}
		}
	}
	 
	
}
