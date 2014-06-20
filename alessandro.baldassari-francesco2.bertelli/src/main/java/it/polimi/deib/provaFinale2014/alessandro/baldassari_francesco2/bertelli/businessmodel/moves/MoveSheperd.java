package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This class models the MoveSheperd Game Move.
 * In this context a Sheperd wants to move from one Road to another. 
 */
public class MoveSheperd extends GameMove
{
	
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
	 * @throws MoveNotAllowedException if the Sheperd wants to move where he already is.
	 * @throws WorkflowException 
	 * @throws IllegalArgumentException if the sheperdToMove or the roadWhereGo parameter is null.
	 */ 
	public MoveSheperd ( Sheperd sheperdToMove , Road roadWhereGo ) throws MoveNotAllowedException, WorkflowException 
	{
		if ( sheperdToMove != null && roadWhereGo != null )
		{
			if ( ! sheperdToMove.getPosition().equals ( roadWhereGo ) )
				if ( roadWhereGo.getElementContained () == null )
					try 
					{
						if ( ! ( ! MapUtilities.areAdjacents ( sheperdToMove.getPosition () , roadWhereGo ) && sheperdToMove.getOwner().getMoney () < GameConstants.MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) )
						{
							this.sheperdToMove = sheperdToMove ;
							this.roadWhereGo = roadWhereGo ;
						}
						else
							throw new MoveNotAllowedException ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;
					}
					catch (WrongStateMethodCallException e) 
					{
						throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ; 
					}
				else
					throw new MoveNotAllowedException ( " : road already occupied" ) ;
			else
				throw new MoveNotAllowedException ( ": not a real move." ) ;
		}
		else
			throw new IllegalArgumentException ( " : null parameters not allowed." ) ;
	}
	
	/**
	 * The effective-algorithm class method.
	 * It controls if this move is ok with the business rules, and if so performs the move
	 * and then place a Fence in the Road where the moving Sheperd was before.
	 * 
	 * @param match the Match object where to perform this action.
	 * @throws MoveNotAllowedException if something goes wrong with the logic.
	 * @throws WrongStateMethodCallException 
	 * @throws WorkflowException if there are no more fences in the bank 
	 * 
	 * @PRECONDITIONS :
	 *  1. match != null
	 * @POSTCONDITIONS
	 *  1. sheperd.pos == whereToGo.
	 *  2. sheperd.whereWasBefore.content instanceof Fence.
	 *  3. whereToGo contains sheperd.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException , WorkflowException  
	{
		Road whereTheSheperdIsNow ;
		whereTheSheperdIsNow = null ; 
		if ( match != null )
		{
			try 
			{
				whereTheSheperdIsNow = sheperdToMove.getPosition () ; 
				// se la regione dove il pastore vuole andare non è adiacente a dove sta ora, fallo pagare.				
				if ( ! MapUtilities.areAdjacents ( sheperdToMove.getPosition () , roadWhereGo ) )
				{
					sheperdToMove.getOwner().pay ( GameConstants.MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) ;
					match.getBank ().receiveMoney ( GameConstants.MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) ;
				}
				// effectively move the sheperd
				whereTheSheperdIsNow.setElementContained(null); 
				roadWhereGo.setElementContained ( sheperdToMove ) ;
				sheperdToMove.moveTo ( roadWhereGo ) ;
				// place a fence where the Sheperd was before.
				whereTheSheperdIsNow.setElementContained ( match.getBank ().getAFence ( FenceType.NON_FINAL ) ) ;
			} 
			catch ( TooFewMoneyException e ) 
			{
				// The constructor should have detected this...
				throw new WorkflowException ( e , PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;
			}
			catch ( NoMoreFenceOfThisTypeException e1 ) 
			{
				try
				{
					whereTheSheperdIsNow.setElementContained ( match.getBank ().getAFence ( FenceType.FINAL ) ) ;
				} 
				catch (NoMoreFenceOfThisTypeException e) 
				{
					// In this situation the game should be finished already...
					throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
				}
			} 
			catch (WrongStateMethodCallException e)
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ; 
			}		
		}
		else
			throw new IllegalArgumentException ( "The parameter match has to be not null." ) ;
	}

}
