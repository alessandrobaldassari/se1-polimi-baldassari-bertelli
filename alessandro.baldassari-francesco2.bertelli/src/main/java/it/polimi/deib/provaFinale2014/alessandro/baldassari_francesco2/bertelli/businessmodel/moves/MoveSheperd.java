package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

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
	 * @throws MoveNotAllowedException if the Sheperd wants to move where he already is.
	 * @throws IllegalArgumentException if the sheperdToMove or the roadWhereGo parameter is null.
	 */ 
	public MoveSheperd ( Sheperd sheperdToMove , Road roadWhereGo ) throws MoveNotAllowedException 
	{
		if ( sheperdToMove != null && roadWhereGo != null )
		{
			if ( sheperdToMove.getPosition().equals ( roadWhereGo ) == false && roadWhereGo.getElementContained () == null )
			{
				this.sheperdToMove = sheperdToMove ;
				this.roadWhereGo = roadWhereGo ;
			}
			else
				throw new MoveNotAllowedException ( "" ) ;
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
	 * @throws WrongStateMethodCallException 
	 * @throws RuntimeException if something goes wrong due to some architecture things.
	 * 
	 * @PRECONDITIONS :
	 *  1. match != null
	 * @EXCEPTIONAL_POSTCONDITIONS
	 *  1. TooFewMoneyException && sheperd.money < 1 && sheperd.whereIs not adjacent whereToGo.
	 * @POSTCONDITIONS
	 *  1. sheperd.pos == whereToGo.
	 *  2. sheperd.whereWasBefore.content instanceof Fence.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException, WrongStateMethodCallException  
	{
		Road whereTheSheperdIsNow ;
		whereTheSheperdIsNow = null ;
		if ( match != null )
		{
			try 
			{
				System.out.println ( "MOVE SHEPERD : INIZIO " ) ;
				System.out.println ( "MOVE SHEPERD : RECUPERO POSIZIONE PASTORE " ) ;		
				whereTheSheperdIsNow = sheperdToMove.getPosition () ; 
				System.out.println ( "MOVE SHEPERD : POSIZIONE PASTORE RECUPERATA : " + whereTheSheperdIsNow ) ;		
				// se la regione dove il pastore vuole andare non è adiacente a dove sta ora, fallo pagare.				
				if ( CollectionsUtilities.contains ( sheperdToMove.getPosition().getAdjacentRoads() , roadWhereGo ) == false )
				{
					System.out.println ( "MOVE SHEPERD : REGIONE DESTINAZIONE NON ADIACENTE " ) ;		
					System.out.println ( "MOVE SHEPERD : INIZIO PAGAMENTO." ) ;		
					sheperdToMove.getOwner().pay ( MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) ;
					match.getBank ().receiveMoney ( MONEY_TO_PAY_IF_ROADS_NON_ADJACENT ) ;
					System.out.println ( "MOVE SHEPERD : FINE PAGAMENTO." ) ;		
				}
				// effectively move the sheperd
				System.out.println ( "MOVE SHEPERD : INIZIO MOVIMENTO PASTORE." ) ;		
				sheperdToMove.moveTo ( roadWhereGo ) ;
				roadWhereGo.setElementContained ( sheperdToMove ) ;
				System.out.println ( "MOVE SHEPERD : FINE MOVIMENTO PASTORE" ) ;		
				// place a fence where the Sheperd was before.
				whereTheSheperdIsNow.setElementContained ( match.getBank ().getAFence ( FenceType.NON_FINAL ) ) ;
				System.out.println ( "MOVE SHEPERD : RECINTO MESSO NELLA REGIONE DI PARTENZA." ) ;		
				System.out.println ( "MOVE SHEPERD : FINE " ) ;
			} 
			catch ( TooFewMoneyException e ) 
			{
				throw new MoveNotAllowedException ( "Too few money to move there!" ) ;
			}
			catch ( NoMoreFenceOfThisTypeException e1 ) 
			{
				try
				{
					whereTheSheperdIsNow.setElementContained ( match.getBank ().getAFence ( FenceType.FINAL ) ) ;
				} 
				catch (NoMoreFenceOfThisTypeException e) 
				{
					throw new RuntimeException ( e ) ;
				}
			}		
		}
		else
			throw new IllegalArgumentException ( "The parameter match has to be not null." ) ;
	}

}
