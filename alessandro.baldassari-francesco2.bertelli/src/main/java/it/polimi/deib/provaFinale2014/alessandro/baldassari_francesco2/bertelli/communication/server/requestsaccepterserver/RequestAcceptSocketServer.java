package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerConnector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.SocketClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller.MatchAdderCommunicationController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is a front end Socket Server to accept in the Game Clients that uses a Socket-based
 * protocol.
 * It accepts requests, creates ClientHandlers for them and then send these Handlers to its
 * matchAdderCommunicationController .
 */
public class RequestAcceptSocketServer extends RequestAccepterServer
{

	/**
	 * The IP Address of the physical Server where this SocketServer is deployed. 
	 */
	public static final String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The port where this Server Socket will be listening.
	 */
	public static final int TCP_LISTENING_PORT = 3333 ;
	
	/**
	 * The ServerSocket object that will be used to listen for inbound Socket connections. 
	 */
	private final ServerSocket serverSocket ;
	
	/**
	 * @param matchAdderCommunicationController the matchAdderCommunicationController field value.
	 */
	RequestAcceptSocketServer ( MatchAdderCommunicationController matchAdderCommunicationController ) throws IOException 
	{
		super ( matchAdderCommunicationController ) ;
		serverSocket = new ServerSocket ( TCP_LISTENING_PORT  ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void acceptRequest () 
	{
		ClientHandler clientHandler ;
		Socket client ;
		try 
		{
			System.out.println ( "SOCKET_SERVER : ACCETTO RICHIESTA " ) ;
			client = serverSocket.accept () ;
			System.out.println ( "SOCKET_SERVER : RICHIESTA ACCETTATA" ) ;
			clientHandler = new SocketClientHandler ( new ClientHandlerConnector < Socket > ( client ) ) ;
			System.out.println ( "SOCKET_SERVER : CREO CLIENT HANDLER PER RICHIESTA" ) ;			
			submitToMatchAdderCommunicationController ( clientHandler ) ;
			System.out.println ( "SOCKET_SERVER : SOTTOMETTO CLIENT HANDLER AL MATCH ADDER COMMUNICATION CONTROLLER" ) ;
			System.out.println ( "SOCKET_SERVER : FINE GESTIONE RICHIESTA" ) ;
		}
		catch ( IOException e ) 
		{
			// manage errors
		}	
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalStart () throws IOException
	{
		if ( serverSocket.isClosed () )
			throw new IOException () ;
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
