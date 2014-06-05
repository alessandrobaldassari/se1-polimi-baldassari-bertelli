package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

/**
 * This class models the BuyCard action, which is the situation where a Player wants to 
 * buy a Card from the Bank. 
 */
public class BuyCard extends GameMove
{

	/**
	 * The Sheperd who required to do this move. 
	 */
	private Sheperd buyer ;
	
	/**
	 * The RegionType the buyer is interested in. 
	 */
	private RegionType buyingCardType ;
	
	/**
	 * @param buyer the Sheperd who required to do this move.
	 * @param buyingCardType the RegionType the buyer is interested in.
	 * @throws IllegalArgumentException if the buyer or the buyingCardType parameter is null.
	 */
	BuyCard ( Sheperd buyer , RegionType buyingCardType )
	{
		if ( buyer != null && buyingCardType != null ) 
		{
			this.buyer = buyer ;
			this.buyingCardType = buyingCardType ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * The effective-algorithm method for this class.
	 * It first determines if the buyer can buy this the Card of the choosen type,
	 * then, if it is able to, perform the actual buying / selling operation.
	 * 
	 * @param match the Match on which the action is performed.
	 * @throws NoMoreCardOfThisTypeException if the Bank has no more Card of the requested type.
	 * @throws TooFewMoneyException if the buyer has not enough money to buy the wanted Card.
	 * @throws CardPriceNotRightException if the buyer tries to buy a wrong price for the wanted Card.
	 * @throws MoveNotAllowedException if the Player is not in a position from which he can buy the wanted Card.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException 
	{
		SellableCard theCard ;
		int price ;
		if ( canThisBuyerBuyThisCard () )
		{
			try 
			{
				price = match.getBank().getPeekCardPrice ( buyingCardType ) ;
				buyer.getOwner ().pay ( price ) ;
				theCard = match.getBank ().sellACard ( price , buyingCardType ) ;
				buyer.getOwner().getSellableCards ().add ( theCard ) ;
			} 
			catch ( NoMoreCardOfThisTypeException e ) 
			{
				e.printStackTrace();
				throw new MoveNotAllowedException () ;
			} 
			catch ( TooFewMoneyException e ) 
			{
				e.printStackTrace();
				throw new MoveNotAllowedException () ;
			} 
			catch ( CardPriceNotRightException e ) 
			{
				e.printStackTrace();
				throw new RuntimeException () ;
			}
		}
		else
			throw new MoveNotAllowedException () ;
	}

	/***/
	private boolean canThisBuyerBuyThisCard () 
	{
		Road buyerPosition ;
		boolean res ;
		buyerPosition = buyer.getPosition () ;
	    res = ( buyingCardType == buyerPosition.getFirstBorderRegion ().getType () || buyingCardType == buyerPosition.getSecondBorderRegion ().getType () ) ;
	    return res ;
	}
	
}
