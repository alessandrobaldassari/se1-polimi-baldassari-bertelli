package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerInfoViewModel extends WithReflectionAbstractObservable < PlayerInfoViewModelObserver >
{

	private Map < RegionType , Integer > cards ;
	
	private int moneyReserve ;

	public PlayerInfoViewModel () 
	{
		super () ;
		cards = new LinkedHashMap < RegionType , Integer > () ;
		moneyReserve = 0 ;
	}
	
	public void receiveMoney ( int amount ) 
	{
		moneyReserve += amount ;
		try 
		{
			notifyObservers ( "onMoneyReserveChanged" , amount , true ) ;
		}
		catch (MethodInvocationException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void pay ( int amount ) 
	{
		moneyReserve -= amount ;
		try 
		{
			notifyObservers ( "onMoneyReserveChanged" , amount , false ) ;
		}
		catch (MethodInvocationException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addCard ( RegionType type )
	{
		cards.put ( type , cards.get ( type ) + 1 ) ;
		try 
		{
			notifyObservers ( "onCardsChanged" , type , cards.get ( type ) ) ;
		}
		catch (MethodInvocationException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void removeCard ( RegionType type ) 
	{
		cards.put ( type , cards.get ( type ) - 1 ) ;
		try 
		{
			notifyObservers ( "onCardsChanged" , type , cards.get ( type ) ) ;
		}
		catch (MethodInvocationException e) 
		{
			e.printStackTrace();
		}
	}
	
}