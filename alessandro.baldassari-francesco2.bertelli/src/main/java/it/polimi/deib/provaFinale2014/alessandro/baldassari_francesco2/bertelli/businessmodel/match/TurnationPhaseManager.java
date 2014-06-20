package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.AlreadyInFinalPhaseException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.Mate;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveExecutor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/***/
class TurnationPhaseManager implements TurnNumberClock
{

	/***/
	private Match match ;
	
	/***/
	private LambEvolver lambEvolver ;
	
	/**
	 * A field indicating in which turn number the game is.
	 * 0 means the match has not began yet.
	 * The access to this field will be permitted only during the TURNATION phase of the game. 
	 */
	private int turnNumber ;
	
	/**
	 * @param match the Match upon which execute the operations.
	 * @param lambEvolver a LambEvolver to manage eventually born Lambs. 
	 * @throws IllegalArgumentException if the match or the lambEvolver parameter is null.
	 */
	public TurnationPhaseManager ( Match match , LambEvolver lambEvolver ) 
	{
		if ( match != null && lambEvolver != null )
		{
			this.match = match ;
			this.lambEvolver = lambEvolver ;
			turnNumber = 0 ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public int getTurnNumber () 
	{
		int res ;
		if ( match.getMatchState () == MatchState.TURNATION )
			res = turnNumber ;
		else
			res = 0 ;
		return res ;
	}
	
	/**
	 * This methods implements the core phase of the Game, the time when every player
	 * makes his moves.
	 * It is implemented as a cycle, which, until the Game is finished asks every player
	 * to do his moves, providing him the tools to do this. 
	 * 
	 * @throws WorkflowException if an unexpected message occurs. 
	 */ 
	public void turnationPhase () throws WorkflowException 
	{
		BlackSheep blackSheep ;
		Wolf wolf ;
		MarketPhaseManager marketManager ;
		boolean gamePlaying ;
		try 
		{
			System.out.println ( "GAME CONTROLLER - TURNATION PHASE : INIZIO " ) ;
			blackSheep = MapUtilities.findBlackSheepAtStart ( match.getGameMap () ) ;
			wolf = MapUtilities.findWolfAtStart ( match.getGameMap () ) ;
			gamePlaying = true ;
			while ( gamePlaying )
			{
				turnNumber ++ ;
				try 
				{
					blackSheep.escape () ;
					playersGenericNotification ( "La pecora nera scappa !!!" );
				}
				catch ( CharacterDoesntMoveException e1 ) 
				{
					playersGenericNotification ( "La pecora nera non si muove !!!" );					
				}
				// let the Players play.
				allPlayersTurn () ;
				// if all the Users left the game, the Match will finish.
				if ( match.getNumberOfPlayers () == 0 )
					throw new WorkflowException ( "Too few players to continue !" ) ;
				// manage the market phase.
				marketManager = new MarketPhaseManager ( match.getPlayers() ) ;
				marketManager.marketPhase();
				try 
				{
					wolf.escape () ;
					playersGenericNotification ( "Il lupo scappa scappa !!!" );
				}
				catch (CharacterDoesntMoveException e) 
				{
					playersGenericNotification ( "Il lupo non scappa !!!" );
				}
				// if the last iteration brought us to the final phase, the turnation phase has to finish
				if ( match.isInFinalPhase () )
					gamePlaying = false ;
			}
		}
		catch ( WrongStateMethodCallException e2 ) 
		{
			// system error, this should never happen - stop everything.
			System.err.println ( "MATCH_CONTROLLER - TURNATION PHASE : WOLF OR BLACK_SHEEP NOT FOUND AT THE BEGINNING." ) ;
			throw new WorkflowException ( e2 , Utilities.EMPTY_STRING ) ;
		}
	}
	
	/**
	 * This method manage a Players turn.
	 * During a turn, each Player can do 3 moves.
	 * If one is not corretct, it looses it.
	 * 
	 * @throws WorkflowException if a serious error occcurs so the match can not go away.
	 */
	private void allPlayersTurn () throws WorkflowException
	{
		MoveExecutor moveFactory ;
		MoveSelector selector ;
		MoveSelection selection ;
		Sheperd choosenSheperd ;
		byte moveIndex ;
		boolean breakPlayer ;
		// for each Player
		for ( Player currentPlayer : match.getPlayers() )
		{		
			// if he is with us.
			if ( ! currentPlayer.isSuspended () )			
				try
				{
					// choose a sheperd for this Player.
					choosenSheperd = chooseSheperd ( currentPlayer ) ;
					// generate a new MoveExecutor for this turn.
					moveFactory = MoveExecutor.newInstance ( choosenSheperd , this , lambEvolver ) ;
					selector = new MoveSelector ( moveFactory.getAssociatedSheperd () ) ;
					breakPlayer = false ;
					for ( moveIndex = 0 ; moveIndex < GameConstants.NUMBER_OF_MOVES_PER_USER_PER_TURN && ! breakPlayer ; moveIndex ++ )
						try 
						{
							fillMoveSelectorWithAvailableParameters ( selector , choosenSheperd ) ;
							selector.setMovesAllowedDueToRuntimeRules () ;
							playersGenericNotification ( "Carissimo, per questo turno è la tua mossa # " + moveIndex ) ;
							selection = currentPlayer.doMove ( selector , match.getGameMap () ) ;
							if ( selection != null )
							{
								// effectively execute the move.
								execMove ( moveFactory , selection, match , selector ) ;
								currentPlayer.genericNotification ( PresentationMessages.MOVE_SUCCEED_MESSAGE ) ;
							}
							else
							{
								// if a user does not want to do any move, it means that he does not want to play anymore; remove him from the match...
								match.removePlayer ( currentPlayer ) ;
								playersGenericNotification ( "Ehy boys, " + currentPlayer.getName() + " ci ha lasciati..." + Utilities.CARRIAGE_RETURN + "Peggio per lui !" ) ;
								breakPlayer = true ;
							}
						} 
						catch ( MoveNotAllowedException e ) 
						{
							// the user tried to do an invalid move; give him another chance
							System.err.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - ERRORE DURANTE L'ESECUZIONE DELLA MOSSA." ) ;									
							currentPlayer.genericNotification ( "Mossa fallita!\n" + e.getMessage() ) ;
						}
				}
				catch ( TimeoutException t ) 
				{
					playersGenericNotification ( "The game is going to continue without " + currentPlayer.getName() + Utilities.CARRIAGE_RETURN + "May be he will come back later..." );
				}
			if ( match.isInFinalPhase () == false && match.getBank().hasAFenceOfThisType ( FenceType.NON_FINAL ) == false )
				try 
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - ENTRO NELLA FASE FINALE " ) ;															
					playersGenericNotification ( "Final Phase !!!" );
					match.enterFinalPhase () ;
				} 
				catch ( AlreadyInFinalPhaseException e ) {}
		}
	}
	
	/**
	 * Helper method that allows the System to choose a Sheperd for a Player's turn ( eventually asking him who ),
	 * 
	 * @param currentPlayer the player about who choose the Sheperd.
	 * @return the choosen Sheperd
	 * @throws TimeoutException if the currentPlayer has to choose between some Sheperds and
	 * 	       does not answer before a timeout.
	 */
	private Sheperd chooseSheperd ( Player currentPlayer ) throws TimeoutException 
	{
		System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR : INIZIO" ) ;
		Sheperd choosenSheperd ;
		if ( match.getNumberOfPlayers () == 2 )
		{
			choosenSheperd = currentPlayer.chooseSheperdForATurn ( currentPlayer.getSheperds () ) ;
			choosenSheperd = Utilities.lookForIdentifier ( currentPlayer.getSheperds() , choosenSheperd.getUID () ) ;
		}
		else	// the only Sheperd he has.
			choosenSheperd = CollectionsUtilities.newListFromIterable ( currentPlayer.getSheperds() ).get ( 0 ) ;
		System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR : END\nRETURN : " + choosenSheperd ) ;
		return choosenSheperd ;
	}
	
	/**
	 * @throws WorkflowException 
	 */
	private void fillMoveSelectorWithAvailableParameters ( MoveSelector moveSelector , Sheperd sh ) throws WorkflowException 
	{
		try 
		{
			moveSelector.setAvailableMoney ( sh.getOwner().getMoney () ) ;
			moveSelector.setAvailableRoadsForMoveSheperd ( findAvailableRoadsForMoveSheperd ( sh ) ) ;
			moveSelector.setAvailableRegionsForMoveSheep ( findAvailableRegionsForMoveSheep ( sh ) ) ;
			moveSelector.setAvailableRegionsForBuyCard ( findAvailableRegionsForBuyCard ( sh ) ) ;
			moveSelector.setAvailableRegionsForMate ( findAvailableRegionsForMate ( sh ) ) ;
			moveSelector.setAvailableRegionsForBreakdown ( findAvailableRegionsForBreakdown ( sh ) ) ;
		} 
		catch ( WrongStateMethodCallException e ) 
		{
			throw new WorkflowException ( e , Utilities.EMPTY_STRING );
		}
		
	}
	
	/**
	 * @throws WorkflowException 
	 */
	private Collection < Road > findAvailableRoadsForMoveSheperd ( Sheperd sh ) throws WorkflowException  
	{
		Collection < Road > res ;
		try 
		{
			if ( sh.getOwner().getMoney () >= 1 )
				res = CollectionsUtilities.newCollectionFromIterable( match.getGameMap().getFreeRoads () ) ;
			else
			{
				res = new LinkedList < Road > () ;
				for ( Road road : sh.getPosition().getAdjacentRoads () )
					if ( road.getElementContained () == null )
						res.add ( road ) ;
			}
		} 
		catch (WrongStateMethodCallException e) 
		{
			throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
		}
		return res ;
	}
	
	/***/
	private Collection < Region > findAvailableRegionsForMoveSheep ( Sheperd sh ) 
	{
		Collection < Region > res ;
		res = new ArrayList < Region > ( 2 ) ;
		if ( MapUtilities.ovineCount ( sh.getPosition().getFirstBorderRegion () ) > 0 )
			res.add ( sh.getPosition().getFirstBorderRegion() ) ;
		if ( MapUtilities.ovineCount ( sh.getPosition().getSecondBorderRegion() ) > 0 )
			res.add ( sh.getPosition().getSecondBorderRegion() ) ;
		return res ;
	}
	
	/**
	 * @throws WorkflowException 
	 */
	private Map < RegionType , Integer > findAvailableRegionsForBuyCard ( Sheperd sh ) throws WorkflowException  
	{
		Map < RegionType , Integer > res ;
		RegionType r ;
		int price ;
		try
		{
			res = new HashMap < Region.RegionType , Integer> ( 2 ) ;
			try
			{
				r =  sh.getPosition().getFirstBorderRegion ().getType () ;
				price = match.getBank().getPeekCardPrice ( r ) ;
				if ( sh.getOwner().getMoney() >= price )
					res.put ( r , price ) ;
			} 
			catch (NoMoreCardOfThisTypeException e) {}
			try
			{
				r =  sh.getPosition().getSecondBorderRegion ().getType () ;
				price = match.getBank().getPeekCardPrice ( r ) ;
				if ( sh.getOwner().getMoney() >= price )
					res.put ( r , price ) ;
			} 
			catch (NoMoreCardOfThisTypeException e) {}
			}
		catch (WrongStateMethodCallException e) 
		{
			throw new WorkflowException ( e ,Utilities.EMPTY_STRING ) ;
		}
		return res ;
	}
	
	/***/
	private Collection < Region > findAvailableRegionsForMate ( Sheperd sh ) 
	{
		Collection < Region > res ;
		Region r ;
		res = new ArrayList < Region > ( 2 ) ;
		r = sh.getPosition().getFirstBorderRegion() ;
		if ( Mate.canMateDueToSexReasons ( r ) )
			res.add(r);
		r = sh.getPosition().getSecondBorderRegion() ;
		if ( Mate.canMateDueToSexReasons ( r ) )
			res.add(r);
		return res ;
	}
	
	/***/
	private Collection < Region > findAvailableRegionsForBreakdown ( Sheperd sh ) 
	{
		Collection < Region > res ;
		Region r ;
		int c ;
		res = new ArrayList < Region > ( 2 ) ;
		r = sh.getPosition().getFirstBorderRegion();
		c = MapUtilities.ovineCount ( r ) ;
		if ( c >= 2 )
			res.add(r);
		else
			if ( c == 1 && MapUtilities.extractAdultOvinesExceptBlackSheep ( r.getContainedAnimals () ).size() == 0 ) 
				res.add(r) ;
		r = sh.getPosition().getSecondBorderRegion();
		c = MapUtilities.ovineCount ( r ) ;
		if ( c >= 2 )
			res.add(r);
		else
			if ( c == 1 && MapUtilities.extractAdultOvinesExceptBlackSheep ( r.getContainedAnimals () ).size() == 0 )
				res.add(r);
		return res;
	}
	
	/**
	 * This method effectively execute a Move selected by the User.
	 * 
	 * @param exec the object that will execute the move.
	 * @param selection a reference to the move the User has choosedn.
	 * @param match the Match over which operate.
	 * @throws MoveNotAllowedException if the execution of the move is not allowed ( probably due to business rules ).
	 * @throws WrongStateMethodCallException 
	 * @throws WorkflowException 
	 */
	private void execMove ( MoveExecutor exec , MoveSelection selection , Match match , MoveSelector sel ) throws MoveNotAllowedException, WorkflowException
	{
		List < Serializable > params ;
		params = CollectionsUtilities.newListFromIterable ( selection.getParams() ) ; 
		int mapUID ;
		try 
		{
			switch ( selection.getSelectedType() )
			{	
				case BREAK_DOWN :
					mapUID =  ( ( Animal ) params.get(0) ).getPosition().getUID () ;
					exec.executeBreakdown ( match , MapUtilities.findAnimalByUID ( match.getGameMap().getRegionByUID ( mapUID ) , ( ( Animal ) params.get(0) ).getUID () ) ) ;
					break ;
				case BUY_CARD :
					exec.executeBuyCard ( match , ( RegionType ) params.get(0) ) ;
				break ;
				case MATE :
					mapUID = ( ( Region ) params.get ( 0 ) ).getUID () ;
					exec.executeMate ( match , match.getGameMap ().getRegionByUID ( mapUID ) ) ;
				break ;
				case MOVE_SHEEP :
					mapUID = ( ( Region ) params.get ( 1 ) ).getUID () ;
					exec.executeMoveSheep ( match , ( Ovine ) MapUtilities.findAnimalByUID ( ( ( Ovine ) params.get(0) ).getPosition() , ( ( Ovine ) params.get(0) ).getUID() ) , match.getGameMap ().getRegionByUID ( mapUID ) ); 
				break ;
				case MOVE_SHEPERD :
					mapUID = ( ( Road ) params.get ( 0 ) ).getUID () ;
					exec.executeMoveSheperd ( match, match.getGameMap ().getRoadByUID ( mapUID ) ); 
				break ;
				default :
					throw new MoveNotAllowedException ( "Scusa, ma questa mossa proprio non l'avevamo mai sentita..." ) ;
			}
		}
		catch ( MoveNotAllowedException m )
		{
			throw new MoveNotAllowedException ( Utilities.EMPTY_STRING , m ) ;
		}
		finally 
		{
			if ( selection.getSelectedType() != null )
				sel.updateSelection ( selection.getSelectedType () ) ;
		}
		System.out.println ( "TURNATION_PHASE_MANAGER - execMove : END" ) ;
	}
	
	/***/
	private void playersGenericNotification ( String msg )
	{
		for ( Player p : match.getPlayers() )
			if ( p.isSuspended() == false )
				p.genericNotification ( msg ) ;
	}
	
}
