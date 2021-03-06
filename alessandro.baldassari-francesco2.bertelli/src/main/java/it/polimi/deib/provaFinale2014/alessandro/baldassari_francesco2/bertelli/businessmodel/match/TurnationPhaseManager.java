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
import java.util.logging.Level;
import java.util.logging.Logger;

/***/
class TurnationPhaseManager implements TurnNumberClock
{

	// ATTRIBUTES
	
	/**
	 * The Match object where operate. 
	 */
	private Match match ;
	
	/**
	 * A LambEvolver object to manage eventually born Lambs. 
	 */
	private LambEvolver lambEvolver ;
	
	/**
	 * A field indicating in which turn number the game is.
	 * 0 means the match has not began yet.
	 * The access to this field will be permitted only during the TURNATION phase of the game. 
	 */
	private int turnNumber ;
	
	// METHODS
	
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
			throw new WorkflowException ( e2 , Utilities.EMPTY_STRING ) ;
		}
		catch ( TimeoutException t ) 
		{
			
		}
	}
	
	/**
	 * This method manage a Players turn.
	 * During a turn, each Player can do 3 moves.
	 * If one is not corretct, it looses it.
	 * 
	 * @throws WorkflowException if a serious error occurs so the match can not go away.
	 * @throws TimeoutException if an operation takes too much time to occur.
	 */
	private void allPlayersTurn () throws WorkflowException, TimeoutException
	{
		Iterable < Player > players ;
		MoveExecutor moveExecutor ;
		Sheperd choosenSheperd ;
		// for each Player
		players = CollectionsUtilities.newCollectionFromIterable ( match.getPlayers() );
		for ( Player currentPlayer : players )
		{		
			// if he is with us.
			if ( ! currentPlayer.isSuspended () )			
				try
				{
					// choose a sheperd for this Player.
					choosenSheperd = chooseSheperd ( currentPlayer ) ;
					// generate a new MoveExecutor for this turn.
					moveExecutor = MoveExecutor.newInstance ( choosenSheperd , this , lambEvolver ) ;
					singlePlayerMoves ( currentPlayer , choosenSheperd , moveExecutor ) ;
					currentPlayer.genericNotification ( "Bene, per questa sessione hai finito.\nOra calmati, guarda le pecore e lascia fare agli altri il loro turno di gioco!" );
					// if all the Users left the game, the Match will finish.
					if ( match.getNumberOfPlayers () == 1 )
						throw new WorkflowException ( "Too few players to continue !" ) ;
				}
				catch ( TimeoutException t ) 
				{
					playersGenericNotification ( "The game is going to continue without " + currentPlayer.getName() + Utilities.CARRIAGE_RETURN + "May be he will come back later..." );
				}
			if ( match.isInFinalPhase () == false && match.getBank().hasAFenceOfThisType ( FenceType.NON_FINAL ) == false )
				try 
				{
					playersGenericNotification ( "Final Phase !!!" );
					match.enterFinalPhase () ;
				} 
				catch ( AlreadyInFinalPhaseException e ) {}
		}
	}
	
	/**
	 * Helper methods that let the User do his moves.
	 * 
	 * @param currentPlayer the Player that is currently doing moves.
	 * @param choosenSheperd the choosen Sheperd.
	 * @param moveExecutor the component that will effectively execute moves.
	 * @throws WorkflowException if an unexpected exception occurs.
	 * @throws TimeoutException if the User takes too much time to do a Move.
	 */
	private void singlePlayerMoves ( Player currentPlayer , Sheperd choosenSheperd , MoveExecutor moveExecutor ) throws WorkflowException, TimeoutException 
	{
		MoveSelector selector ;
		MoveSelection selection ;
		int moveIndex ;
		boolean  breakPlayer ;
		breakPlayer = false ;
		for ( moveIndex = 0 ; moveIndex < GameConstants.NUMBER_OF_MOVES_PER_USER_PER_TURN && ! breakPlayer ; moveIndex ++ )
			try 
			{
				selector = new MoveSelector ( moveExecutor.getAssociatedSheperd () ) ;
				fillMoveSelectorWithAvailableParameters ( selector , choosenSheperd ) ;
				selector.setMovesAllowed ( moveExecutor ) ;
				playersGenericNotification ( "Carissimo, per questo turno è la tua mossa # " + moveIndex ) ;
				selection = currentPlayer.doMove ( selector , match.getGameMap () ) ;
				if ( selection != null )
				{
					// effectively execute the move.
					execMove ( moveExecutor , selection, match ) ;
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
				Logger.getGlobal().log ( Level.INFO , Utilities.EMPTY_STRING , e ) ;
				currentPlayer.genericNotification ( "Mossa fallita!\n" + e.getMessage() ) ;
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
	 * This method fills a MoveSelector with the data available to the User for a Selection.
	 * 
	 * @param moveSelector the MoveSelector object to fill.
	 * @param sh the Sheperd choosen by the User for this turn.
	 * @throws WorkflowException if un unexpected method occurs.
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
	 * Find the Regions available for a MoveSheperd move.
	 * 
	 * @param sh the Sheperd that could move.
	 * @return a Collection < Road > containing all the Region available for a MoveSheperd move.
	 * @throws WorkflowException if un unexpected error occurs
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
	
	/**
	 * Find all the Regions available for a MoveSheep move.
	 * 
	 * @param sh the Sheperd that will perform the move.
	 * @return a Collection < Region > containing the Regions available for a MoveSheep move.
	 */
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
	 * Find the RegionType available for a BuyCard move.
	 * 
	 * @param sh the Sheperd that will perform the move.
	 * @return a Map containing, for each available RegionType, the associated price of a Card of that type ( Bank's price ).
	 * @throws WorkflowException if an unexpected error occurs.
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
				if ( r != RegionType.SHEEPSBURG )
				{
					price = match.getBank().getPeekCardPrice ( r ) ;
					if ( sh.getOwner().getMoney() >= price )
						res.put ( r , price ) ;
				}
			} 
			catch (NoMoreCardOfThisTypeException e) {}
			try
			{
				r =  sh.getPosition().getSecondBorderRegion ().getType () ;
				if ( r != RegionType.SHEEPSBURG )
				{
					price = match.getBank().getPeekCardPrice ( r ) ;
					if ( sh.getOwner().getMoney() >= price )
						res.put ( r , price ) ;
				}
			} 
			catch (NoMoreCardOfThisTypeException e) {}
		}
		catch (WrongStateMethodCallException e) 
		{
			throw new WorkflowException ( e ,Utilities.EMPTY_STRING ) ;
		}
		return res ;
	}
	
	/**
	 * Find all the Regions available for a Mate move.
	 * 
	 * @param sh the Sheperd that will perform the move.
	 * @return a Collection < Region > containing all the Regions available for this move. 
	 */
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
	
	/**
	 * Find all the Regions available for a Breakdown move.
	 * 
	 * @params sh the Sheperd that will perform the move.
	 * @return a Collection < Region > containing the Regions available for the move.
	 */
	private Collection < Region > findAvailableRegionsForBreakdown ( Sheperd sh ) 
	{
		Collection < Region > res ;
		Region r ;
		int c ;
		res = new ArrayList < Region > ( 2 ) ;
		r = sh.getPosition().getFirstBorderRegion();
		c = MapUtilities.extractOvinesExceptBlackSheep ( r.getContainedAnimals() ).size();
		if ( c >= 1 )
			res.add(r);
		r = sh.getPosition().getSecondBorderRegion();
		c = MapUtilities.extractOvinesExceptBlackSheep ( r.getContainedAnimals() ).size();
		if ( c >= 1 )
			res.add(r);
		return res;
	}
	
	/**
	 * This method effectively execute a Move selected by the User.
	 * 
	 * @param exec the object that will execute the move.
	 * @param selection a reference to the move the User has choosen.
	 * @param match the Match over which operate.
	 * @throws MoveNotAllowedException if the execution of the move is not allowed ( probably due to business rules ). 
	 * @throws WorkflowException if an unexpected error occurs.
	 */
	private void execMove ( MoveExecutor exec , MoveSelection selection , Match match ) throws MoveNotAllowedException, WorkflowException
	{
		List < Serializable > params ;
		params = CollectionsUtilities.newListFromIterable ( selection.getParams() ) ; 
		int mapUID ;
		try 
		{
			switch ( selection.getSelectedType() )
			{	
				case BREAK_DOWN :
					mapUID =  ( ( Animal ) params.get( 0 ) ).getPosition().getUID () ;
					exec.executeBreakdown ( match , MapUtilities.findAnimalByUID ( match.getGameMap().getRegionByUID ( mapUID ) , ( ( Animal ) params.get(0) ).getUID () ) ) ;
					break ;
				case BUY_CARD :
					exec.executeBuyCard ( match , RegionType.valueOf ( ( ( RegionType ) params.get(0 ) ).name() )  ) ;
				break ;
				case MATE :
					mapUID = ( ( Region ) params.get ( 0 ) ).getUID () ;
					exec.executeMate ( match , match.getGameMap ().getRegionByUID ( mapUID ) ) ;
				break ;
				case MOVE_SHEEP :
					mapUID = ( ( Region ) params.get ( 1 ) ).getUID () ;
					exec.executeMoveSheep ( match , 
					( Ovine ) MapUtilities.findAnimalByUID ( match.getGameMap().getRegionByUID( ( ( Ovine ) params.get(0) ).getPosition().getUID() ) ,( ( Ovine ) params.get(0) ).getUID() ) 
					, match.getGameMap ().getRegionByUID ( mapUID ) ); 
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
		System.out.println ( "TURNATION_PHASE_MANAGER - execMove : END" ) ;
	}
	
	/**
	 * Notify the Users with a Message.
	 * 
	 * @param msg the message to notify.
	 * @throws TimeoutException if too long time is taken for the operation
	 */
	private void playersGenericNotification ( String msg ) throws TimeoutException
	{
		for ( Player p : match.getPlayers() )
			if ( ! p.isSuspended() )
				p.genericNotification ( msg ) ;
	}
	
}
