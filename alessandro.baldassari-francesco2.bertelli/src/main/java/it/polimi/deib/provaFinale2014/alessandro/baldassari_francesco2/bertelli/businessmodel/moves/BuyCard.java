package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;

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
	 * @throws MoveNotAllowedException if some business rules are broken by the parameters
	 * @throws IllegalArgumentException if the buyer or the buyingCardType parameter is null.
	 */
	public BuyCard ( Sheperd buyer , RegionType buyingCardType ) throws MoveNotAllowedException
	{
		if ( buyer != null && buyingCardType != null ) 
		{
			if ( buyingCardType == buyer.getPosition().getFirstBorderRegion ().getType () || buyingCardType == buyer.getPosition().getSecondBorderRegion ().getType () )
			{ 
				this.buyer = buyer ;
				this.buyingCardType = buyingCardType ;
			}
			else
				throw new MoveNotAllowedException ( "BUY_CARD : THE PLAYER CAN NOT BUY A CARD OF THIS TYPE DUE TO HIS CURRENT POSITION." ) ;
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
	 * 
	 * @PRECONDITIONS:
	 *  1. match != null
	 * @EXCEPTIONS_POSTCONDITIONS
	 *  1. bank.cards [ buying_card_type ] is empty && NoMoreCardOfThisTypeException raised
	 *  2. bank.peekcardprice [ buying_card_type ] > buyer ( in ).money && TooFewMoneyException
	 * @POSTCONDITIONS
	 *  3. AFTER bank.cards [ buying_card_type ].peek.price = PREV bank.cards [ buying_card_type ].peek.price + 1
	 *  4. buyer.has ( PREV bank.cards [ buying_card_type ] )
	 *  5. AFTER buyer.money == PREV buyer.money - bank.cards [ buying_card_type ].peek.price
	 *    
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException 
	{
		SellableCard theCard ;
		int price ;
		try 
		{
			price = match.getBank().getPeekCardPrice ( buyingCardType ) ;
			buyer.getOwner ().pay ( price ) ;
			theCard = match.getBank ().sellACard ( price , buyingCardType ) ;
			theCard.setOwner ( buyer.getOwner () ) ; 
			buyer.getOwner().addCard ( theCard ) ;
		} 
		catch ( NoMoreCardOfThisTypeException e ) 
		{
			throw new MoveNotAllowedException ( "BREAK_DOWN - EXECUTE : NO MORE CARD OF THIS TYPE." ) ;
		} 
		catch ( TooFewMoneyException e ) 
		{
			throw new MoveNotAllowedException ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;
		} 
		catch ( CardPriceNotRightException e ) 
		{
			throw new RuntimeException ( e ) ;
		}	
	}
	
}


