package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import java.io.IOException ;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

/***/
public abstract class ResumeConnectionServer < T > implements Runnable
{
	
	/***/
	private boolean on ;
	
	/***/
	private BlockingQueue < Couple < Integer , T > > requests ;

	/***/
	private ConnectionLoosingController connectionLoosingController ;
	
	/***/
	public ResumeConnectionServer ( ConnectionLoosingController connectionLoosingController ) 
	{
		on = false ;
		requests = new LinkedBlockingQueue < Couple < Integer , T > > () ;
		this.connectionLoosingController = connectionLoosingController ;
	} 
	
	/***/
	public abstract void connect () throws IOException ;
	
	/***/
	protected ClientHandler < T > getHandler ( Integer key ) 
	{
		ClientHandler res ;
		res = connectionLoosingController.getClientHandler ( key ) ;
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
