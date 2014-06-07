package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.SocketClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

public class SocketResumeConnectionServer extends ResumeConnectionServer < Socket , SocketClient > 
{

	public SocketResumeConnectionServer () 
	{
		super () ;
		Executor exec ;
		Runnable r ;
		exec = Executors.newSingleThreadExecutor () ;
		r = new ListenerServer () ;
		exec.execute ( r ) ;
	}
	
	@Override
	protected void resumeAlgo ( SocketClient s ) {
		// TODO Auto-generated method stub
		
	}
	
	private class ListenerServer implements Runnable 
	{

		@Override
		public void run () 
		{
			Socket in ;
			while ( isUp () )
			{
				
			}
		}		
		
	}
	
}
