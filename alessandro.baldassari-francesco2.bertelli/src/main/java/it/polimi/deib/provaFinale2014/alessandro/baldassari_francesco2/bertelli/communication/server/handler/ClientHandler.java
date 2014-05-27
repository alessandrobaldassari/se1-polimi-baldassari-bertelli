package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;

public interface ClientHandler 
{

	public String requestName () throws IOException ;

	public void notifyMatchWillNotStart () ;
	
	public void requestMove () ;
	
	public void dispose () throws IOException ;
	
	public interface ClientHandlerObserver 
	{
		
		public void onNameRequested () ;
		
		public void onNotifyMatchWillNotStart () ;
		
	}
	
}
