package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.Client;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

public abstract class ResumeConnectionServer < T , Q extends Client > implements Runnable
{

	private Map < Q , ClientHandler < T > > associations ;
	
	private boolean on ;
	
	private BlockingQueue < Couple < Q , T > > requests ;
	
	public ResumeConnectionServer () 
	{
		on = false ;
		associations = new LinkedHashMap < Q , ClientHandler < T > > () ;
		requests = new LinkedBlockingQueue < Couple < Q , T > > () ;
	} 
	
	public void addAssociation ( Q client , ClientHandler < T > handler ) 
	{
		associations.put ( client , handler ) ;
	}
	
	protected ClientHandler < T > getAssociations ( Q clientKey ) 
	{
		return associations.get ( clientKey ) ;
	}
	
	protected boolean isUp () 
	{
		return on ;
	}
	
	/***/
	protected void tryResumeMe ( Couple < Q , T > req ) 
	{
		requests.offer ( req ) ;
	}
	
	/***/
	protected abstract void resumeAlgo ( Couple < Q , T > req ) ;
	
	/***/
	@Override
	public void run ()
	{
		Couple < Q , T > x ;
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
				on = false ;
			}
		}
	}
	
}
