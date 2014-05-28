package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.awt.Color;
import java.io.IOException;

public interface ClientHandler 
{

	public String requestName () throws IOException ;

	public Color requestSheperdColor( Iterable <Color> availableColors) throws IOException;
	
	public void chooseCardsEligibleForSelling(Iterable <SellableCard> sellablecards) throws IOException;
	
	public void notifyMatchWillNotStart (String message) throws IOException ;
	
	public void requestMove () ;
	
	public void genericNotification(String message) throws IOException;
	
	public void dispose () throws IOException ;
	
	
	
	public interface ClientHandlerObserver 
	{
		
		public void onNameRequested () ;
		
		public void onNotifyMatchWillNotStart () ;
		
	}
	
}
