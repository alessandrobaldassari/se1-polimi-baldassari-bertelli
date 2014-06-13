package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/***/
public class Application 
{

	/***/
	private static Application instance ;
	
	/***/
	private ExecutorService executorService ;
	
	private Application () 
	{
		executorService = Executors.newCachedThreadPool () ;
	}
	
	/***/
	public static Application getInstance () 
	{
		if ( instance == null )
			instance = new Application () ;
		return instance ;
	}
	
	/**
	 * @param runnable
	 */
	public void executeRunnable ( Runnable runnable )
	{
		executorService.execute ( runnable ) ;
	}
	
	/**
	 * @param 
	 */
	public < T > Future < T > executeCallable ( Callable < T > callable ) 
	{
		Future < T > res ;
		res = executorService.submit ( callable ) ;
		return res ;
	}
	
}
