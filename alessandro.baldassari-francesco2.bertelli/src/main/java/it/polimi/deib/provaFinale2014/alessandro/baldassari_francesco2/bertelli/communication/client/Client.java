package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import java.io.IOException;

public interface Client extends Runnable
{

	public void technicalConnect () throws IOException ;
		
	public void register ( String name ) ;
	
	public void doMove ()  ;
	
	interface ClientObserver 
	{
		
		public void onRegister ( String name ) ;
		
		public void onDoMove () ;
		
	}
	
}
