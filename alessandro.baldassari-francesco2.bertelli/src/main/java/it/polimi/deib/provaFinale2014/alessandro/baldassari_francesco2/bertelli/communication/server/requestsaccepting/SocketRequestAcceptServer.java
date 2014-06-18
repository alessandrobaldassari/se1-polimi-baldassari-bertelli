package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.ServerEnvironment;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerConnector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.SocketClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlauncherserver.MatchPlayerAdder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class is a front end Socket Server to accept in the Game Clients that uses a Socket-based
 * protocol.
 * It accepts requests, creates ClientHandlers for them and then send these Handlers to its
 * matchAdderCommunicationController .
 */
public class SocketRequestAcceptServer extends RequestAccepterServer
{
	
	/**
	 * The ServerSocket object that will be used to listen for inbound Socket connections. 
	 */
	private final ServerSocket serverSocket ;
	
	/**
	 * @param matchAdderCommunicationController the matchAdderCommunicationController field value.
	 */
	protected SocketRequestAcceptServer ( MatchPlayerAdder matchAdderCommunicationController ) throws IOException 
	{
		super ( matchAdderCommunicationController ) ;
		serverSocket = new ServerSocket ( ServerEnvironment.SOCKET_REQUEST_ACCEPT_SERVER_TCP_PORT  ) ;
	}

	/**
	 * Return the IP address where this Server is deployed.
	 * 
	 * @return the IP address where this Server is deployed.
	 * @throws UnknownHostException if something goes wrong with the address reading.
	 */
	public static String getServerIPAddress () throws UnknownHostException 
	{
		return ServerEnvironment.getInstance().getLocalhostIPAddress () ;
	}
	
	/**
	 * Return the TCP port where this Server is listening. 
	 * 
	 * @return the TCP port where this Server is listening.
	 */
	public static int getServerPort () 
	{
		return ServerEnvironment.SOCKET_REQUEST_ACCEPT_SERVER_TCP_PORT ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void acceptRequest () 
	{
		Socket client ;
		client = null ;
		try 
		{
			System.out.println ( "SOCKET_SERVER - ACCEPT REQUEST : ACCETTO RICHIESTA " ) ;
			client = serverSocket.accept () ;
			System.out.println ( "SOCKET_SERVER - ACCEPT REQUEST : RICHIESTA ACCETTATA" ) ;
			manageRequest ( client ) ;
			System.out.println ( "SOCKET_SERVER : ACCEPT REQUEST FINE GESTIONE RICHIESTA" ) ;
		}
		catch ( IOException e ) 
		{
			System.out.println ( "SOCKET_SERVER - ACCEPT REQUEST : ERRORE DI IO" ) ;
			if ( client != null ) 
				try 
				{
					System.out.println ( "SOCKET_SERVER : ACCEPT REQUEST : RIPROVO A GESTIRE LA RICHIESTA" ) ;
					manageRequest ( client ) ;
				}
				catch (IOException e1) 
				{
					System.out.println ( "SOCKET_SERVER : ACCEPT REQUEST : ANCORA ERRORE, ABBANDONO." ) ;
				}
		}	
	}

	/**
	 * Helper method to create a Client Handler for a Request and registering it to a Controller.
	 * 
	 * @throws IOException if something goes wrong with the creation of the Client Handler.
	 */
	private void manageRequest ( Socket client ) throws IOException 
	{
		ClientHandler < ? > clientHandler ;		
		System.out.println ( "SOCKET_SERVER - MANAGE REQUEST : CREO CLIENT HANDLER PER RICHIESTA" ) ;			
		clientHandler = new SocketClientHandler ( new ClientHandlerConnector < Socket > ( client ) ) ;
		System.out.println ( "SOCKET_SERVER : MANAGE REQUEST : SOTTOMETTO CLIENT HANDLER AL MATCH ADDER COMMUNICATION CONTROLLER" ) ;
		submitToMatchAdderCommunicationController ( clientHandler ) ;
		System.out.println ( "SOCKET_SERVER : MANAGE REQUEST : FINE" ) ;		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalStart () throws IOException
	{
		if ( serverSocket.isClosed () )
			throw new IOException () ;
		System.out.println ( "SOCKET_REQUEST_ACCEPT_SERVER - TECHNICAL START : SERVER UP" ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void lifeLoopImplementation () 
	{
		acceptRequest () ;
	}

}
