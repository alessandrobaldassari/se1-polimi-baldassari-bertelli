package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;


public class SheeplandClientApp 
{

	private static SheeplandClientApp instance ;
	
	private ImagesHolder imagesHolder ;
	
	private SheeplandClientApp () 
	{
		imagesHolder = new ImagesHolder () ;
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
	
}
