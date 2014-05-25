package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication;

import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;

public class MasterServer implements Runnable
{

	private SocketServer socketServer ;
	private RMIServer rmiServer ;
	private boolean inFunction ;
	private GameController currentGameController ;
	
	MasterServer () throws IOException  
	{
		socketServer = new SocketServer ( this ) ;
		rmiServer = new RMIServerImpl ( this ) ;
		inFunction = false ;
	}
	
	public void run () 
	{
		new Thread ( socketServer ).start () ;
		currentGameController = new GameController ( this ) ;
		inFunction = true ;
		new Thread ( currentGameController ).start () ;
		while  ( inFunction ) 
		{
			
		}
	}

	public synchronized void addPlayer ( ClientHandler newClientHandler ) 
	{
		currentGameController.addPlayerAndCheck (  ) ;
	}
	
	public synchronized void notifyFailure () 
	{
		
	}
	
}
