package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
 * This class manages the Market Phase of the Game. 
 */
class MarketPhaseManager 
{

	/**
	 * The List of Players over which operate. 
	 */
	private Iterable < Player > players ;
	
	/**
	 * @param players the List of Players over which operate.
	 * @throws IllegalArgumentException if the players parameter is null.
	 */
	public MarketPhaseManager ( Iterable < Player > players ) 
	{
		if ( players != null )
			this.players = players ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This method implements the Market phase of the Game.
	 * The code is straightforward and highly tighted with the business rules.
	 * It uses some private helper methods. 
	 */
	public void marketPhase () 
	{
		Collection < SellableCard > sellableCards ;
		Iterable < SellableCard > receivedSellableCards ;
		int amount ;
		try
		{
			for ( Player currentPlayer : players )
				if ( ! currentPlayer.isSuspended () )
				currentPlayer.chooseCardsEligibleForSelling () ;
			for ( Player currentPlayer : players )
			{
				try
				{					
					amount = 0 ;
					// generate a List containing all the Cards this Player can buy
					sellableCards = generateGettableCardList ( currentPlayer ) ;
					// ask the User which Cards he wants to buy
					receivedSellableCards = currentPlayer.chooseCardToBuy ( sellableCards ) ;
					for ( SellableCard s : receivedSellableCards )
						amount = amount + s.getSellingPrice () ;
					// if he has enough money 
					if ( amount <= currentPlayer.getMoney () )
						transferCards ( receivedSellableCards , currentPlayer ) ;
					else
						throw currentPlayer.new TooFewMoneyException () ;
				}
				catch ( NotSellableException n ) 
				{
					currentPlayer.genericNotification ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;					
				}
				catch ( SellingPriceNotSetException e )
				{
					currentPlayer.genericNotification ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;										
				}
				catch ( TooFewMoneyException t ) 
				{
					currentPlayer.genericNotification ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;					
				}
			}
		}
		catch ( TimeoutException t ) 
		{
			for ( Player p : players )
				p.genericNotification ( "One of the players is not connected yet, so this market phase can not go away." + Utilities.CARRIAGE_RETURN + "It's not our fault, it's his !!!" );
		}
	}
	
	/**
	 * 
	 */
	private void transferCards ( Iterable < SellableCard > receivedSellableCards , Player buyer ) throws TooFewMoneyException, NotSellableException, SellingPriceNotSetException 
	{
		for ( SellableCard s : receivedSellableCards )
		{
			s.getOwner().getSellableCards().remove ( s ) ;
			s.getOwner().receiveMoney ( s.getSellingPrice () ) ;
			buyer.getSellableCards().add ( s ) ; 
			buyer.pay ( s.getSellingPrice() ) ;
			s.setOwner ( buyer ) ;
		}
	}
	
	/**
	 * This method generate a Collection containing all the Cards ( owned buy Match Players ) which
	 * the Player passed buy parameter is able to buy.
	 * 
	 * @param buyer the Player who is going to buy some cards.
	 * @return a Collection < SellableCard > containing all the Cards owned by other Players than
	 * 		   the one passed by parameter which the buyer Player is able to buy. 
	 */
	private Collection < SellableCard > generateGettableCardList ( Player buyer ) 
	{
		Collection < SellableCard > res ;
		res = new LinkedList < SellableCard > () ;
		for ( Player p : players )
			if ( buyer.equals ( buyer ) )
				for ( SellableCard s : p.getSellableCards () )
						if ( s.isSellable () )
							res.add ( s ) ;
		return res ;
	}

}

