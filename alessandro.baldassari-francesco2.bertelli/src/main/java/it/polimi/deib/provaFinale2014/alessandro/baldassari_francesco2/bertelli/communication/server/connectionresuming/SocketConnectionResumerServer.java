package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.connectionresuming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.ServerEnvironment;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerConnector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.SuspendedClientHandlerBuffer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

/***/
public class SocketConnectionResumerServer extends ConnectionResumerServer < Socket > 
{

	/***/
	public SocketConnectionResumerServer ( SuspendedClientHandlerBuffer suspendedClientHandlerBuffer )  
	{
		super ( suspendedClientHandlerBuffer ) ;
		
	}
	
	/***/
	@Override
	public void connect () throws IOException 
	{
		Executor exec ;
		Runnable r ;
		exec = Executors.newSingleThreadExecutor () ;
		r = new ListenerServer () ;
		exec.execute ( r ) ;
	}
	
	/***/
	@Override
	protected void resumeAlgo ( Couple < Integer , Socket > req  ) throws IOException 
	{
		ClientHandler < Socket > handler ;
		ObjectOutputStream ois ;
		handler = getHandler ( req.getFirstObject() ) ;
		ois = new ObjectOutputStream ( req.getSecondObject().getOutputStream () ) ;
		if ( handler != null )
		{
			handler.rebind ( new ClientHandlerConnector < Socket > ( req.getSecondObject() ) ) ;
			ois.writeUTF("OK");
			handler.sendResumeSucceedNotification () ;
		}
		else
			ois.writeUTF("KO"); 
	}
	
	/***/
	private class ListenerServer implements Runnable 
	{

		/***/
		private ServerSocket ss ;
		
		/***/
		public ListenerServer () throws IOException 
		{
			ss = new ServerSocket ( ServerEnvironment.SOCKET_RESUME_CONNECTION_SERVER_TCP_PORT ) ;
		}
		
		/***/
		@Override
		public void run () 
		{
			ObjectInputStream ois ;
			Socket in ;
			int inUID ;
			while ( isUp () )
			{
				try
				{
					in = ss.accept () ;
					ois = new ObjectInputStream ( in.getInputStream () ) ;
					inUID = ois.readInt();
					tryResumeMe ( new Couple < Integer , Socket > ( inUID , in ) ) ;
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}		
		
	}
	
}
