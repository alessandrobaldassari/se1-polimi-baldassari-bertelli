package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;

public class BuyCard extends ExecutableGameMove
{

	private Sheperd buyer ;
	private RegionType buyingCardType ;
	
	BuyCard ( Sheperd buyer , RegionType buyingCardType )
	{
		if ( buyer != null && buyingCardType != null ) 
		{
			this.buyer = buyer ;
			this.buyingCardType = buyingCardType ;
		}
		else
			throw new RuntimeException () ;
	}
	
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException 
	{
		Road buyerPosition ;
		Card theCard ;
		int price ;
		buyerPosition = buyer.getPosition () ;
		if ( buyingCardType == buyerPosition.getFirstBorderRegion ().getType () || buyingCardType == buyerPosition.getSecondBorderRegion ().getType () )
		{
			try 
			{
				price = match.getBank().getPeekCardPrice ( buyingCardType ) ;
				buyer.getOwner ().pay ( price ) ;
				theCard = match.getBank ().sellACard ( price , buyingCardType ) ;
				buyer.getOwner().getCards ().add ( theCard ) ;
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

	
	
}
