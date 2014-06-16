package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.AlreadyInFinalPhaseException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.factory.MoveExecutor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/***/
class TurnationPhaseManager implements TurnNumberClock
{

	private Match match ;
	
	private LambEvolver lambEvolver ;
	
	/**
	 * A field indicating in which turn number the game is.
	 * 0 means the match has not began yet.
	 * The access to this field will be permitted only during the TURNATION phase of the game. 
	 */
	private int turnNumber ;
	
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
	 * This methods implements the core phase of the Game, the time when every player
	 * makes his moves.
	 * It is implemented as a cycle, which, until the Game is finished asks every player
	 * to do his moves, providing him the tools to do this. 
	 * @throws WorkflowException if an unexpected message occurs. 
	 */ 
	public void turnationPhase () throws WorkflowException 
	{
		MoveExecutor moveFactory ;
		MoveSelector selector ;
		MoveSelection selection ;
		BlackSheep blackSheep ;
		Wolf wolf ;
		MarketPhaseManager marketManager ;
		byte moveIndex ;
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
				System.out.println ( "GAME CONTROLLER - TURNATION PHASE - NUMERO TURNO : " + turnNumber ) ;
				try 
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PECORA NERA PROVA A SCAPPARE " ) ;
					blackSheep.escape () ;
					playersGenericNotification ( "La pecora nera scappa !!!" );
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PECORA NERA SI MUOVE " ) ;
				}
				catch ( CharacterDoesntMoveException e1 ) 
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PECORA NERA NON SI MUOVE " ) ;
				}
				// if all the Users left the game, the Match will finish.
				if ( match.getNumberOfPlayers () == 0 )
					throw new WorkflowException () ;
				for ( Player currentPlayer : match.getPlayers() )
				{		
					if ( currentPlayer.isSuspended () == false )
					{
						System.out.println ( "GAME CONTROLLER - TURNATION PHASE - TURNO DEL PLAYER : " + currentPlayer.getName () ) ;
						// if the last iteration brought us to the final phase, the turnation phase has to finish
						if ( match.isInFinalPhase () )
							break ;
						try
						{
							moveFactory = moveFactoryGenerator ( currentPlayer ) ;
							for ( moveIndex = 0 ; moveIndex < GameConstants.NUMBER_OF_MOVES_PER_USER_PER_TURN ; moveIndex ++ )
							{	
								selector = new MoveSelector ( moveFactory.getAssociatedSheperd () , generateCardPriceMap () ) ;
								try 
								{
									System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - CHIEDENDO DI FARE UNA MOSSA " ) ;				
									selection = currentPlayer.doMove ( selector , match.getGameMap () ) ;
									if ( selection != null )
									{
										System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - MOSSA SCELTA " + selection.getSelectedType().toString() ) ;									
										// effectively execute the move.
										execMove ( moveFactory , selection, match ) ;
										System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " HA ESEGUITO LA MOSSA." ) ;									
									}
									else
									{
										// if a user does not want to do any move, it means that he does not want to play anymore; remove him from the match...
										match.removePlayer ( currentPlayer ) ;
										playersGenericNotification ( "Ehy boys, " + currentPlayer.getName() + " ci ha lasciati..." + Utilities.CARRIAGE_RETURN + "Peggio per lui !" ) ;
										break ;
									}
								} 
								catch ( MoveNotAllowedException e ) 
								{
									// the user tried to do an invalid move; give him another chance
									System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - ERRORE DURANTE L'ESECUZIONE DELLA MOSSA." ) ;									
									currentPlayer.genericNotification ( "Non puoi fare questa mossa, scegline un'altra!\n" + e.getMessage () ) ;
									moveIndex -- ;								
								} 
							}
						}
						catch ( TimeoutException t ) 
						{
							playersGenericNotification ( "The game is going to continue without " + currentPlayer.getName() + Utilities.CARRIAGE_RETURN + "May be he will come back later..." );
						}
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
				System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PRIMA DELLA FASE DI MARKET." ) ;															
				marketManager = new MarketPhaseManager ( match.getPlayers() ) ;
				marketManager.marketPhase();
				System.out.println ( "GAME CONTROLLER - TURNATION PHASE - DOPO LA FASE DI MARKET." ) ;															
				try 
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - IL LUPO PROVA A SCAPPARE " ) ;															
					wolf.escape () ;
					playersGenericNotification ( "Il lupo scappa scappa !!!" );
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - IL LUPO PROVA E' SCAPPATO " ) ;															
				}
				catch (CharacterDoesntMoveException e) 
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - IL LUPO NON E' RIUSCITO A SCAPPARE " ) ;															
				}
				if ( match.isInFinalPhase () )
					gamePlaying = false ;
			}
		}
		catch ( WrongStateMethodCallException e2 ) 
		{
			// system error, this should never happen - stop everything.
			System.out.println ( "MATCH_CONTROLLER - TURNATION PHASE : WOLF OR BLACK_SHEEP NOT FOUND AT THE BEGINNING." ) ;
			throw new WorkflowException () ;
		}
	}
	
	/***/
	private Map < RegionType , Integer > generateCardPriceMap () 
	{
		Map < RegionType , Integer > res ;
		int price ;
		res = new HashMap < RegionType , Integer > () ;
		for ( RegionType r : RegionType.allTheTypesExceptSheepsburg() )
		{
			try 
			{
				price = match.getBank().getPeekCardPrice ( r ) ;
				res.put ( r , price ) ;
			}
			catch (NoMoreCardOfThisTypeException e) {}
		}
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public int getTurnNumber () throws WrongMatchStateMethodCallException
	{
		int res ;
		if ( match.getMatchState () == MatchState.TURNATION )
			res = turnNumber ;
		else
			throw new WrongMatchStateMethodCallException ( match.getMatchState () ) ;
		return res ;
	}
	
	
	/**
	 * This method effectively execute a Move selected by the User.
	 * 
	 * @param exec the object that will execute the move.
	 * @param selection a reference to the move the User has choosedn.
	 * @param match the Match over which operate.
	 * @throws MoveNotAllowedException if the execution of the move is not allowed ( probably due to business rules ).
	 */
	private void execMove ( MoveExecutor exec , MoveSelection selection , Match match ) throws MoveNotAllowedException
	{
		List < Serializable > params ;
		params = CollectionsUtilities.newListFromIterable ( selection.getParams() ) ; 
		int mapUID ;
		switch ( selection.getSelectedType() )
		{	
			case BREAK_DOWN :
				mapUID =  ( ( Animal ) params.get(0) ).getPosition().getUID () ;
				exec.executeBreakdown ( match , findAnimalByUID ( match.getGameMap().getRegionByUID ( mapUID ) , ( ( Animal ) params.get(0) ).getUID () ) ) ;
			break ;
			case BUY_CARD :
				exec.executeBuyCard ( match , (RegionType) params.get(0) ) ;
			break ;
			case MATE :
				mapUID = ( ( Region ) params.get ( 0 ) ).getUID () ;
				exec.executeMate ( match , match.getGameMap ().getRegionByUID ( mapUID ) ) ;
			break ;
			case MOVE_SHEEP :
				mapUID = ( ( Region ) params.get ( 1 ) ).getUID () ;
				exec.executeMoveSheep ( match , ( Ovine ) params.get(0) , match.getGameMap ().getRegionByUID ( mapUID ) ); 
			break ;
			case MOVE_SHEPERD :
				mapUID = ( (Road) params.get (0 ) ).getUID () ;
				exec.executeMoveSheperd ( match, match.getGameMap ().getRoadByUID ( mapUID ) ); 
			break ;
		}
	}
	
	/***/
	private Animal findAnimalByUID ( Region location , int uid )
	{
		Animal res ;
		res = null ;
		for ( Animal a : location.getContainedAnimals () )
			if ( a.getUID () == uid )
			{
				res = a ;
				break ;
			}
		return res ;
	}
	
	/**
	 * Helper method that allows the System to choose a Sheperd for a Player's turn ( eventually aking him who ),
	 * and then create a MoveFactory for this User to play.
	 * 
	 * @param currentPlayer the player for who a MoveFactory has to be built.
	 * @return the created GameMoveFactory
	 * @throws TimeoutException if the currentPlayer has to choose between some Sheperds and
	 * 	       does not answer before a timeout.
	 */
	private MoveExecutor moveFactoryGenerator ( Player currentPlayer ) throws TimeoutException 
	{
		MoveExecutor res ;
		Sheperd choosenSheperd ;
		System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR : INIZIO" ) ;
		if ( match.getNumberOfPlayers () == 2 )
		{
			System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR - CHIDENDO AL PLAYER : " + currentPlayer.getName () + " DI SCEGLIERE UN PASTORE" ) ;					
			choosenSheperd = currentPlayer.chooseSheperdForATurn ( currentPlayer.getSheperds () ) ;
			for ( Sheperd s : currentPlayer.getSheperds() )
				if ( s.getUID () == choosenSheperd.getUID () )
				{
					choosenSheperd = s ;
					break ;
				}
			System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR - IL PLAYER : " + currentPlayer.getName () + " HA SCELTO IL PASTORE " + choosenSheperd.getName () ) ;									
			res = MoveExecutor.newInstance ( choosenSheperd , this , lambEvolver ) ;
		}
		else
			res = MoveExecutor.newInstance ( currentPlayer.getSheperds ().iterator ().next () , this , lambEvolver ) ;
		System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR : END" ) ;
		return res ;
	}
	
	private void playersGenericNotification ( String msg )
	{
		for ( Player p : match.getPlayers() )
			if ( p.isSuspended() == false )
				p.genericNotification ( msg ) ;
	}
	
}
