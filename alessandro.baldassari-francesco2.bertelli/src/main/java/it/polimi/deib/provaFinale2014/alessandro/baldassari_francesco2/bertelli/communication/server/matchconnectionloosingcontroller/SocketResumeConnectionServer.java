package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.SocketClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerConnector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

public class SocketResumeConnectionServer extends ResumeConnectionServer < Socket , SocketClient > 
{

	public static final int PORT = 3331 ;
	
	public SocketResumeConnectionServer () throws IOException 
	{
		super () ;
		Executor exec ;
		Runnable r ;
		exec = Executors.newSingleThreadExecutor () ;
		r = new ListenerServer () ;
		exec.execute ( r ) ;
	}
	
	@Override
	protected void resumeAlgo ( Couple < SocketClient , Socket > req  ) 
	{
		ClientHandler < Socket > handler ;
		handler = getAssociations ( req.getFirstObject() ) ;
		if ( handler != null )
		{
			try 
			{
				handler.rebind ( new ClientHandlerConnector < Socket > ( req.getSecondObject() ) ) ;
				handler.sendResumeSucceedNotification () ;
			}
			catch (IOException e) 
			{}
		}
	}
	
	private class ListenerServer implements Runnable 
	{

		private ServerSocket ss ;
		
		public ListenerServer () throws IOException 
		{
			ss = new ServerSocket ( PORT ) ;
		}
		
		@Override
		public void run () 
		{
			ObjectInputStream ois ;
			Socket in ;
			SocketClient c ;
			while ( isUp () )
			{
				try
				{
					in = ss.accept () ;
					ois = new ObjectInputStream ( in.getInputStream () ) ;
					c = (SocketClient) ois.readObject () ;
					tryResumeMe ( new Couple < SocketClient , Socket > ( c , in ) ) ;
				}
				catch (IOException e) {} 
				catch (ClassNotFoundException e) {}
			}
		}		
		
	}
	
}
