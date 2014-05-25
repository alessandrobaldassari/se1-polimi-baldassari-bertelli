package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.SocketClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable
{

	private final int TCP_LISTENING_PORT = 3333 ;
	
	private final ServerSocket serverSocket ;

	private boolean inFunction ;
	
	private MasterServer masterServer ;
	
	SocketServer ( MasterServer masterServer ) throws IOException 
	{
		this.masterServer = masterServer ;
		serverSocket = new ServerSocket ( TCP_LISTENING_PORT  ) ;
		inFunction = false ;
	}
	
	public void run () 
	{
		ClientHandler clientHandler ;
		Socket client ;
		inFunction = true ;
		System.out.println ( "Socket Server : Listening" ) ;
		while ( inFunction ) 
		{
			try 
			{
				client = serverSocket.accept () ;
				System.out.println ( "Socket Server : Connection Accepted" ) ;
				clientHandler = new SocketClientHandler ( client ) ;
				System.out.println ( "Socket Server : Pre Adding Player" ) ;
				masterServer.addPlayer ( clientHandler ) ;
			}
			catch ( IOException e ) 
			{
				e.printStackTrace () ;
			}
		}
	}
	 
	
}
