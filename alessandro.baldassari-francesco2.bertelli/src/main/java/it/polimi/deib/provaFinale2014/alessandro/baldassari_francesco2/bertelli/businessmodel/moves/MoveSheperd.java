package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;

public class MoveSheperd extends ExecutableGameMove
{

	private static final int MONEY_TO_PAY_IF_ROADS_NON_ADJACENT = 1 ;
	private Sheperd sheperdToMove ;
	private Road roadWhereGo ;
	
	MoveSheperd ( Sheperd sheperdToMove , Road roadWhereGo ) 
	{
		if ( sheperdToMove != null && roadWhereGo != null && roadWhereGo.getElementContained () == null )
		{
			this.sheperdToMove = sheperdToMove ;
			this.roadWhereGo = roadWhereGo ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException  
	{
		Road whereTheSheperdIsNow ;
		whereTheSheperdIsNow = sheperdToMove.getPosition () ; 
		if ( whereTheSheperdIsNow.getAdjacentRoads().contains ( roadWhereGo ) == false )
		{
			try 
			{
				sheperdToMove.getOwner().pay ( MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) ;
				match.getBank ().receiveMoney ( MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) ;
			} 
			catch ( TooFewMoneyException e ) 
			{
				e.printStackTrace ();
				throw new MoveNotAllowedException () ;
			}
		}
		sheperdToMove.moveTo ( roadWhereGo ) ;
		roadWhereGo.setElementContained ( sheperdToMove ) ;
		try 
		{
			whereTheSheperdIsNow.setElementContained ( match.getBank ().getAFence ( FenceType.NON_FINAL ) ) ;
		}
		catch ( NoMoreFenceOfThisTypeException e ) 
		{
			e.printStackTrace();
			try 
			{
				whereTheSheperdIsNow.setElementContained ( match.getBank ().getAFence ( FenceType.FINAL ) ) ;
			} 
			catch ( NoMoreFenceOfThisTypeException e1 ) 
			{
				e1.printStackTrace () ;
				throw new RuntimeException () ;
			}				
		}
	}

}
