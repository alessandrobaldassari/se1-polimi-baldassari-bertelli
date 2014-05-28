package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

/**
 * This class models the MoveSheperd Game Move.
 * In this context a Sheperd wants to move from one Road to another. 
 */
public class MoveSheperd extends GameMove
{

	/**
	 * The amount of value the Sheperd has to pay if the Road where he wants to go is 
	 * not adjacent to the Road where he is now. 
	 */
	private static final int MONEY_TO_PAY_IF_ROADS_NON_ADJACENT = 1 ;
	
	/**
	 * The Sheperd who wants to make this move. 
	 */
	private Sheperd sheperdToMove ;
	
	/**
	 * The Road where the Sheperd wants to go. 
	 */
	private Road roadWhereGo ;
	
	/**
	 * @param sheperdToMove the Sheperd who wants to make this move. 
	 * @param roadWhereGo the Road where the Sheperd wants to go. 
	 * @throws IllegalArgumentException if the sheperdToMove or the roadWhereGo parameter is null.
	 */
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
	
	/**
	 * The effective-algorithm class method.
	 * It controls if this move is ok with the business rules, and if so performs the move
	 * and then place a Fence in the Road where the moving Sheperd was before.
	 * 
	 * @param match the Match object where to perform this action.
	 * @throws MoveNotAllowedException if something goes wrong with the logic.
	 * @throws RuntimeException if something goes wrong due to some architecture things.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException  
	{
		Road whereTheSheperdIsNow ;
		whereTheSheperdIsNow = sheperdToMove.getPosition () ; 
		if ( CollectionsUtilities.contains ( whereTheSheperdIsNow.getAdjacentRoads () , roadWhereGo ) == false )
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
				throw new RuntimeException ( e1 ) ;
			}				
		}
	}

}
