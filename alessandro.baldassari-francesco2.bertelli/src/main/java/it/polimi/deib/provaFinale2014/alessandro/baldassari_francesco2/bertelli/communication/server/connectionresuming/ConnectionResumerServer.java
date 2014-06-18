package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.connectionresuming;

import java.io.IOException ;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.SuspendedClientHandlerBuffer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

/***/
public abstract class ConnectionResumerServer < T > implements Runnable
{
	
	/***/
	private boolean on ;
	
	/***/
	private BlockingQueue < Couple < Integer , T > > requests ;

	/***/
	private SuspendedClientHandlerBuffer suspendedClientHandlerBuffer ;
	
	/***/
	public ConnectionResumerServer ( SuspendedClientHandlerBuffer suspendedClientHandlerBuffer ) 
	{
		this.suspendedClientHandlerBuffer = suspendedClientHandlerBuffer ;
		on = false ;
		requests = new LinkedBlockingQueue < Couple < Integer , T > > () ;
	} 
	
	/***/
	public static ConnectionResumerServer < ? > newSocketServer ( SuspendedClientHandlerBuffer suspendedClientHandlerBuffer ) 
	{
		return new SocketConnectionResumerServer ( suspendedClientHandlerBuffer ) ;
 	}
	
	/***/
	public static ConnectionResumerServer < ? > newRMIServer (  SuspendedClientHandlerBuffer suspendedClientHandlerBuffer ) 
	{
		return new RMIConnectionResumerServerImpl(suspendedClientHandlerBuffer);
	}
	
	/***/
	public abstract void connect () throws IOException ;
	
	/***/
	protected ClientHandler < T > getHandler ( Integer key ) 
	{
		ClientHandler res ;
		res = suspendedClientHandlerBuffer.getClientHandler ( key ) ;
		return res ;
	}
	
	/***/
	protected boolean isUp () 
	{
		return on ;
	}
	
	/***/
	protected void tryResumeMe ( Couple < Integer , T > req ) 
	{
		requests.offer ( req ) ;
	}
	
	/***/
	protected abstract void resumeAlgo ( Couple < Integer , T > req ) throws IOException ;
	
	/***/
	@Override
	public void run ()
	{
		Couple < Integer , T > x ;
		on = true ;
		while ( on ) 
		{
			try 
			{
				x = requests.take () ;
				resumeAlgo ( x ) ;
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				on = false ;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
}
