package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerInfoViewModel extends WithReflectionAbstractObservable < PlayerInfoViewModelObserver > implements PlayerObserver
{

	private Map < RegionType , Integer > cards ;
	
	private int moneyReserve ;

	public PlayerInfoViewModel () 
	{
		super () ;
		cards = new LinkedHashMap < RegionType , Integer > () ;
		for ( RegionType r : RegionType.allTheTypesExceptSheepsburg() )
			cards.put ( r , 0 );
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

	@Override
	public void onPay ( Integer paymentAmount, Integer moneyYouHaveNow ) 
	{
		pay ( moneyYouHaveNow ) ;
	}

	@Override
	public void onGetPayed ( Integer paymentAmount, Integer moneyYouHaveNow) 
	{
		receiveMoney ( moneyYouHaveNow ) ;
	}

	@Override
	public void onCardAdded ( RegionType cardType ) 
	{
		addCard ( cardType ) ;
	}

	@Override
	public void onCardRemoved ( RegionType cardType ) 
	{
		removeCard ( cardType ) ;
	}
	
}