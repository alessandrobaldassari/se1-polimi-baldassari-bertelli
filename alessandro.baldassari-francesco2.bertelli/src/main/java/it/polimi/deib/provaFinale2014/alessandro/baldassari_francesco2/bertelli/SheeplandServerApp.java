package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SheeplandServerApp 
{

	private static SheeplandServerApp instance ;
	
	private ExecutorService executorService ;
	
	private SheeplandServerApp ()
	{
		executorService = Executors.newCachedThreadPool () ;
	}
	
	public synchronized static SheeplandServerApp getInstance () 
	{
		if ( instance == null )
			instance = new SheeplandServerApp () ;
		return instance ;
	}
	
	public void executeRunnable ( Runnable runnable ) 
	{
		executorService.execute ( runnable ) ;
	}
	
	public void shutdown () 
	{
		executorService.shutdown () ;
	}
	
}
