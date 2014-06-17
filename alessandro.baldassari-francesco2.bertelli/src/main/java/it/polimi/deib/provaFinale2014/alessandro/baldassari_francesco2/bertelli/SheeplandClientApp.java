package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SheeplandClientApp 
{

	private static SheeplandClientApp instance ;
	
	private ImagesHolder imagesHolder ;
	
	private ExecutorService executorService ;
	
	private SheeplandClientApp () 
	{
		imagesHolder = new ImagesHolder () ;
		executorService = Executors.newCachedThreadPool () ;
	}
	
	public synchronized static SheeplandClientApp getInstance () 
	{
		if ( instance == null )
			instance = new SheeplandClientApp () ;
		return instance ;
	}
	
	public ImagesHolder getImagesHolder () 
	{
		return imagesHolder ;
	}

	public void executeRunnable ( Runnable r ) 
	{
		executorService.execute ( r ) ;
	}
	
}
