package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;

import java.util.Collection;
import java.util.LinkedList;

public abstract class Player 
{

	private Collection < Card > cards ;
	private String name ;
	private int money ;
	
	public Player ( String name , int money ) 
	{
		if ( name != null && money > 0 )
		{
			this.name = name ;
			this.money = money ;
			cards = new LinkedList < Card > () ;
		}
		else 
			throw new IllegalArgumentException () ;
	}
	
	public String getName () 
	{
		return name ;
	}
	
	public int getMoney () 
	{
		return money ;
	}
	
	public void buyCard ( Card buyedCard ) 
	{
		cards.add ( buyedCard ) ;
	}
	
	public void sellCard ( Card cardToSell , int price )
	{
		
	}
	
	public void receiveMoney ( int amountToAdd ) 
	{
		if ( amountToAdd >= 0 )
			money = money + amountToAdd ;
		else 
			throw new IllegalArgumentException () ;
	}
	
	public void breakDownOvine ( Ovine ovine ) {}
	
	public abstract void chooseCardsEligibleForSelling () ;
	
}
