package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
 * This class manages the Market Phase of the Game. 
 */
class MarketPhaseManager 
{

	// ATTRIBUTES
	
	/**
	 * The List of Players over which operate. 
	 */
	private Iterable < Player > players ;
	
	// METHODS
	
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
	 * 
	 * @throws {@link WorkflowException} if any error occurs during the exection.
	 */
	public void marketPhase () throws WorkflowException
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
				System.err.println ( "MARKET : PLAYER " + currentPlayer.getName() + " PRIMA DELLA SOSP." ) ;
				if ( ! currentPlayer.isSuspended () )
					try
					{					
						// generate a List containing all the Cards this Player can buy
						sellableCards = generateGettableCardList ( currentPlayer ) ;
						System.err.println ( "MARKET : PLAYER " + currentPlayer.getName() + " PRIMA DELLA IF " + sellableCards ) ; 
						if ( ! sellableCards.isEmpty () )
						{
							System.err.println ( "MARKET : PLAYER " + currentPlayer.getName() + " DENTRO LA IF " ) ; 
							// ask the User which Cards he wants to buy
							receivedSellableCards = currentPlayer.chooseCardToBuy ( sellableCards ) ;
							// if the user selected some cards...
							amount = 0 ;
							for ( SellableCard s : receivedSellableCards )
								amount = amount + s.getSellingPrice () ;
							// if he has enough money
							if ( amount > 0 )
							{
								if ( amount <= currentPlayer.getMoney () )
									transferCards ( receivedSellableCards , currentPlayer ) ;
								else
									throw currentPlayer.new TooFewMoneyException () ;
							}
						}
						else
							currentPlayer.genericNotification ( "Sorry, ma per questa fase non ci sono carte che tu possa acquistare\nMagari al prossimo turno..." );
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
					catch (WrongStateMethodCallException e) 
					{
						throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
					}
			}
		}
		catch ( TimeoutException t ) 
		{
			for ( Player p : players )
				try 
				{
					p.genericNotification ( "One of the players is not connected yet, so this market phase can not go away." + Utilities.CARRIAGE_RETURN + "It's not our fault, it's his !!!" );
				} 
				catch (TimeoutException e) {}
		}
	}
	
	/**
	 * This method transfer the Cards contained in the receivedSellableCards to the buyer Player.
	 * 
	 * @param receivedSellableCards the Cards to transfer.
	 * @param buyer the Player that will receive the Cards
	 * @throws WorkflowException if any error occurs during the execution of this method.
	 */
	private void transferCards ( Iterable < SellableCard > receivedSellableCards , Player buyer ) throws WorkflowException 
	{
		Player seller ;
		SellableCard transferredCard ;
		for ( SellableCard s : receivedSellableCards )
		{
			try 
			{
				// find the seller.
				seller = findPlayerByName ( s.getOwner().getName () ) ;
				// find the Card to transfer
				transferredCard = seller.getCard ( s.getUID () ) ;
				// remove the Card from the Seller and pay him.
				seller.removeCard ( transferredCard ) ; 
				seller.receiveMoney ( transferredCard.getSellingPrice () ) ;
				// add the Card to the Buyer and make him pay.
				buyer.addCard ( transferredCard ) ;
				buyer.pay ( transferredCard.getSellingPrice() ) ;
				transferredCard.setOwner ( buyer ) ;
				// for this turn already selled, rest !
				transferredCard.setSellable ( false ) ; 
			} 
			catch (WrongStateMethodCallException e) 
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
			}
			catch (TooFewMoneyException e) 
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
			} 
			catch (NotSellableException e) 
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
			}
			catch (SellingPriceNotSetException e) 
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
			}
		}
	}
	
	/**
	 * Find and returns a Player with the name n.
	 * 
	 * @param n the name of the Player to find.
	 * @return the Player with name n, null if not found.
	 */
	private Player findPlayerByName ( String n ) 
	{
		Player res ;
		res  = null ;
		for ( Player p : players )
			if ( p.getName().compareToIgnoreCase ( n ) == 0 )
			{
				res = p ;
				break ;
			}
		return res ;
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
		// for each player
		for ( Player p : players )
			// if it's not the buyer and he's with us
			if ( ! p.equals ( buyer ) && ! p.isSuspended () )
				for ( SellableCard s : p.getSellableCards () )
						// if p marked this Card as sellable
						if ( s.isSellable () )
							res.add ( s ) ;
		return res ;
	}

}

