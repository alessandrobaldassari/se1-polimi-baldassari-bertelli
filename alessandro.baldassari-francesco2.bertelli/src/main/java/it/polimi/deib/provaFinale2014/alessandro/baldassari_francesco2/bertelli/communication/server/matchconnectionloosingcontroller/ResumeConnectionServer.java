package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.Client;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

public abstract class ResumeConnectionServer < T , Q extends Client > implements Runnable
{

	private Map < Client , ClientHandler > associations ;
	
	private boolean on ;
	
	private BlockingQueue < Q > requests ;
	
	private Q toWorkOn ;
	
	public ResumeConnectionServer () 
	{
		on = false ;
		requests = new LinkedBlockingQueue < Q > () ;
	}
	
	protected boolean isUp () 
	{
		return on ;
	}
	
	/***/
	protected void tryResumeMe ( Q client ) 
	{
		requests.offer ( client ) ;
	}
	
	protected abstract void resumeAlgo ( Q q ) ;
	
	@Override
	public void run ()
	{
		on = true ;
		while ( on ) 
		{
			try 
			{
				toWorkOn = requests.take () ;
				
			}
			catch (InterruptedException e) 
			{
				on = false ;
			}
		}
	}
	
}
