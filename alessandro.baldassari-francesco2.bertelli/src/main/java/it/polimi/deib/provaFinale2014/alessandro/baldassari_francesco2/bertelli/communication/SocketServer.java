package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable
{

	private final int TCP_LISTENING_PORT = 3333 ;
	
	private final ServerSocket serverSocket ;

	private boolean inFunction ;
	
	SocketServer ( MasterServer masterServer ) throws IOException 
	{
		serverSocket = new ServerSocket ( TCP_LISTENING_PORT  ) ;
		inFunction = false ;
	}
	
	public void run () 
	{
		Socket client ;
		inFunction = true ;
		while ( inFunction ) 
		{
			try 
			{
				client = serverSocket.accept () ;
		
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public 
	
}
