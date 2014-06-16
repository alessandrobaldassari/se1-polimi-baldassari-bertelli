package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import java.io.IOException;

public class BackendResumeConnectionServer implements Runnable 
{

	private ResumeConnectionServer socket ;
	
	private ResumeConnectionServer rmi ;
	
	public BackendResumeConnectionServer ( ConnectionLoosingController connectionLoosingController ) throws IOException 
	{
		if ( connectionLoosingController != null )
		{
			socket = new SocketResumeConnectionServer ( connectionLoosingController ) ;
			rmi = new RMIResumerConnectionServerImpl ( connectionLoosingController ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	@Override
	public void run () 
	{
		try 
		{
			socket.connect();
			rmi.connect();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		while ( true ) ;
	} 
	
}
