package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.AlreadyInFinalPhaseException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingController.ConnectionLoosingManagerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller.MatchStartCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * This class is a core component of the Server Architecture of the System and also
 * of the whole System.
 * It manages the whole lifecycle of the Match.
 * Essentially, a GameController object is the runner of the GameMatch object;
 * it calls all the lifecycle methods of the Game, including the Players ( which may
 * be on the Client side in the case of a distributed scenario ).
 * The only true state variable of a GameController object is the GameMatch object;
 * so, every GameController, is potentially poolable.
 */
public class MatchController implements Runnable , TurnNumberClock , ConnectionLoosingManagerObserver , Serializable
{
	
	/**
	 * The Timer value about the time to wait before begin a Match. 
	 */
	public static final long DELAY = 45 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/**
	 * Static variable to generate id's for the Match objects here created. 
	 */
	private static int lastMatchIdentifierUID = -1 ;
	
	/**
	 * The component this GameController will invoke to notify that the Initialization phase of this
	 * match is finished ( either well or bad ) and this GameController is ready to continue his
	 * lifecycle alone. 
	 */
	private final MatchStartCommunicationController matchStartCommunicationController ;
	
	/**
	 * The match that this GameController will manage. 
	 */
	private Match match ;
	
	/**
	 * The match identifier associated with the Match managed with this GameController. 
	 */
	private Identifiable < Match > matchIdentifier ;
	
	/**
	 * A standard Timer object to mange the timer at the beginning of a Match.
	 * It's a business rule. 
	 */
	private transient final Timer timer;
	
	/**
	 * An AnimalsFactory object to create all the Animal objects who play in the Game. 
	 */
	private AnimalFactory animalsFactory ;
	
	/**
	 * A component to mange the evolution of Lambs. 
	 */
	private LambEvolver lambEvolver ;
	
	/**
	 * A thread-safe queue object to offer the ADD PLAYER to decouple the adding Player operation
	 * from the Match object.
	 * Potentially useful also to avoid thead lock acquisition problems. 
	 */
	private BlockingQueue < Player > tempBlockingQueue ;
	
	/**
	 * A field indicating in which turn number the game is.
	 * 0 means the match has not began yet.
	 * The access to this field will be permitted only during the TURNATION phase of the game. 
	 */
	private int turnNumber ;
		
	/**
	 * @param matchStartCommunicationController the value for the matchStartCommunicationController field.
	 * @throws IllegalArgumentException if the parameter passed is null.
	 */
	public MatchController ( MatchStartCommunicationController matchStartCommunicationController ) 
	{
		if ( matchStartCommunicationController != null )
		{
			this.matchStartCommunicationController = matchStartCommunicationController ;
			timer = new Timer();
			tempBlockingQueue = new LinkedBlockingQueue < Player > () ;
			turnNumber = 0 ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onConnectionRetrieved ( Suspendable retrievedElem ) 
	{
		for ( Player p : match.getPlayers() )
			if ( p.isSuspended () == false )
				p.genericNotification ( retrievedElem + " is with us again!" );
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onBeginSuspensionControl ( Suspendable pendant ) 
	{
		for ( Player p : match.getPlayers() )
			if ( p.equals ( pendant ) == false )
				p.genericNotification ( "We all wait a few time to retrieve a pendant player..." );
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onEndSuspensionControl ( Boolean suspendedRetrieved ) 
	{
		if ( suspendedRetrieved )
			for ( Player p : match.getPlayers() )
				p.genericNotification ( "We are all ok again." + Utilities.CARRIAGE_RETURN + "The show can go on!" );
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
	 * This is the logically second method of the Game Controller lifecycle.
	 * Obviously is not called by this GameController itself; otherwise, other 
	 * components will call it to add new Players.
	 * It enqueue the newPlayer parameter to a queue that will be checked by another
	 * method that will dequeue it and add it to the Match object 
	 * 
	 * @param newPlayer the Player to add to the this GameController.
	 * @throws WrongMatchStateMethodCallException if this method is not called during the WAIT_FOR_PLAYERS
	 *         phase.
	 * 
	 */
	public void addPlayer ( Player newPlayer ) throws WrongMatchStateMethodCallException
	{
		if ( match.getMatchState () == Match.MatchState.WAIT_FOR_PLAYERS )
			tempBlockingQueue.offer ( newPlayer ) ;
		else
			throw new WrongMatchStateMethodCallException ( match.getMatchState () ) ;
	}
	
	/**
	 * The run method of this GameController, which implements the Game Workflow, using
	 * appropriate private helper methods that describe each game phase.
	 */
	public void run () 
	{
		String endMessage ;
		endMessage = null ;
		try 
		{
			System.out.println ( "GAME CONTROLLER : PRIMA DELLA CREATING PHASE" ) ;		
			creatingPhase () ;
			System.out.println ( "GAME CONTROLLER : PRIMA DELLA WAIT FOR PLAYER " ) ;
			waitForPlayersPhase () ;
			System.out.println ( "GAME CONTROLLER : PRIMA DELLA INITIALIZATION PHASE" ) ;
			initializationPhase () ; 
			System.out.println ( "GAME CONTROLLER : PRIMA DELLA TURNATION PHASE" ) ;
			turnationPhase () ;
			System.out.println ( "GAME CONTROLLER : PRIMA DELLA RESULTS CALCULATION PHASE" ) ;
			resultsCalculationPhase () ;
			endMessage = PresentationMessages.BYE_MESSAGE ;
		}
		catch (WorkflowException e) 
		{
			System.out.println ( "MATCH_CONTROLLER - RUN : WORKFLOW_EXCEPTION GENERATED" ) ;
			endMessage = PresentationMessages.UNEXPECTED_ERROR_MESSAGE ;
		}
		finally 
		{
			matchFinishingProcedure ( endMessage ) ;
		}
	}
	
	/**
	 * The first method executed by this controller during it's logical lifecycle.
	 * It creates all the resources necessary to the game and starts a Timer
	 * to wait for all players to add in. 
	 * 
	 * @throws RuntimeException if the AnimalFactory Singleton mechanism fails.
	 */
	private void creatingPhase () throws WorkflowException  
	{
		GameMap gameMap ;
		Bank bank;
		try 
		{
			lastMatchIdentifierUID ++ ;
			matchIdentifier = new MatchIdentifier ( lastMatchIdentifierUID ) ;
			animalsFactory = AnimalFactory.newAnimalFactory ( matchIdentifier ) ;
			lambEvolver = new LambEvolverImpl ( animalsFactory ) ;
			gameMap = GameMapFactory.getInstance().newInstance ( matchIdentifier ) ;
			bank = BankFactory.getInstance().newInstance ( matchIdentifier ) ;
			match = new Match ( gameMap , bank ) ;		
			match.setMatchState ( MatchState.WAIT_FOR_PLAYERS );
			timer.schedule ( new WaitingPlayersTimerTask () , DELAY ) ;
		} 
		catch ( SingletonElementAlreadyGeneratedException e ) 
		{
			System.out.println ( "MATCH_CONTROLLER - CREATING PHASE : SINGLETON_ELEMENT_ALREADY_GENERATED_EXCETPION" ) ;
			throw new WorkflowException () ;
		}
	}
	
	/**
	 * This method is in the second phase of the Game lifecycle.
	 * It wait for players to arrive.
	 * When they do it, adds them to the Match, and if the MAX_NUMBER_OF_PLAYERS is reached,
	 * cause the Match workflow to finish the INITIALIZATION phase and to move to the next phase. 
	 */
	private void waitForPlayersPhase () throws WorkflowException
	{
		Player newPlayer ;
		System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYER PHASE - INIZIO" ) ;
		while ( match.getMatchState() != MatchState.INITIALIZATION )
		{
			try 
			{
				System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYERS PHASE - ATTENDENDO UN PLAYER" ) ;
					newPlayer = tempBlockingQueue.poll() ;
				while ( newPlayer == null && match.getMatchState() != MatchState.INITIALIZATION )
					newPlayer = tempBlockingQueue.poll() ;
				if ( newPlayer != null )
				{
					System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYERS PHASE - PLAYER ACCETTATO" ) ;
					match.addPlayer ( newPlayer ) ;
					System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYERS PHASE - PLAYER AGGIUNTO AL MATCH" ) ;
					if ( match.getNumberOfPlayers () == GameConstants.MAX_NUMBER_OF_PLAYERS ) 
					{
						System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYERS PHASE - MASSIMO NUMERO DI GIOCATORI RAGGIUNTO" ) ;
						timer.cancel () ;
						match.setMatchState ( MatchState.INITIALIZATION ) ;
						matchStartCommunicationController.notifyFinishAddingPlayers () ;
					}
				}
			}
			catch ( WrongMatchStateMethodCallException e ) 
			{
				throw new WorkflowException () ;
			}			
		}
		System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYER PHASE - FINE" ) ;
	}
	
	/**
	 * This is the third method in the logical flow of the Game Controller.
	 * It is divided in 4 phases:
	 * 1. All the Sheeps are placed in the Regions.
	 * 2. The initial Cards are given to the Players.
	 * 3. The Players are provided with money from the Bank.
	 * 4.
	 */
	public void initializationPhase () throws WorkflowException 
	{
		System.out.println ( "GAME CONTROLLER : INITIALIZATION PHASE, PRIMA DI PLACE SHEEPS" ) ;
		placeSheeps () ;
		System.out.println ( "GAME CONTROLLER : INITIALIZATION PHASE, PRIMA DI DISTRIBUTE INITIAL CARDS" ) ;
		distributeInitialCards () ;
		System.out.println ( "GAME CONTROLLER : INITIALIZATION PHASE, PRIMA DI MONEY DISTRIBUTION" ) ;	
		moneyDistribution () ;
		System.out.println ( "GAME CONTROLLER : INITIALIZATION PHASE, PRIMA DI CHOOSE PLAYERS ORDER" ) ;
		choosePlayersOrder () ;
		System.out.println ( "GAME CONTROLLER : INITIALIZATION PHASE, PRIMA DI DISTRIBUTE SHEPERDS" ) ;
		distributeSheperds () ;
		match.setMatchState ( MatchState.TURNATION );
	}
	
	/**
	 * This method place one Sheep per Region in the Game Map and the Black Sheep
	 * in the Region of Sheepsburg.
	 * 
	 */
	private void placeSheeps () throws WorkflowException
	{
		Animal blackSheep ;
		Animal wolf ;
		Region sheepsburg ;
		Ovine bornOvine ;
		try 
		{
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : INIZIO " ) ;
			for ( Region region : match.getGameMap ().getRegions () ) 
			{
				bornOvine = animalsFactory.newAdultOvine ( MathUtilities.genProbabilityValue() > 0.5 ? AdultOvineType.RAM : AdultOvineType.SHEEP ) ;
				region.addAnimal ( bornOvine ) ;
				bornOvine.moveTo ( region ) ;
			}
			sheepsburg = match.getGameMap ().getRegionByType ( RegionType.SHEEPSBURG ).iterator().next() ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : PRIMA DI GENERAZIONE PECORA NERA." ) ;
			blackSheep = animalsFactory.newBlackSheep () ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : PECORA NERA GENERATA." ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : PRIMA DI GENERAZIONE LUPO." ) ;
			wolf = animalsFactory.newWolf () ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : LUPO GENERATO." ) ;
			sheepsburg.addAnimal ( blackSheep ) ;
			sheepsburg.addAnimal ( wolf ) ;
			blackSheep.moveTo ( sheepsburg ) ;
			wolf.moveTo ( sheepsburg ) ;
			playersGenericNotification ( "All animals are in the Map!" + Utilities.CARRIAGE_RETURN + "Beeeee!" ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : FINE " ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			throw new WorkflowException () ;
		} 
		catch ( WolfAlreadyGeneratedException e ) 
		{
			throw new WorkflowException () ;
		}
	}
	
	/**
	 * This methods emulates the phase where one Initial Card is given to each Player.
	 * Which Card give to each Player is a randomic decision.
	 */
	private void distributeInitialCards () throws WorkflowException
	{
		Stack < RegionType > regions ;
		Card initCard ;
		try 
		{
			System.out.println ( "MATCH CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE INITIAL CARDS : INIZIO " ) ;
			regions = new Stack < RegionType > () ;
			regions.addAll ( RegionType.allTheTypesExceptSheepsburg () ) ;
			CollectionsUtilities.listMesh ( regions ) ;
			for( Player player : match.getPlayers () )
			{
				System.out.println ( "MATCH_CONTROLLER - DISTRIBUTE_INITIAL_CARDS : SERVENDO IL PLAYER :" + player.getName () ) ;
				initCard = match.getBank ().takeInitialCard ( regions.pop() ) ;
				player.setInitialCard ( initCard ) ;
				player.genericNotification ( "Initial Card choosen for you !" + Utilities.CARRIAGE_RETURN + "Your secret card has the type : " + initCard.getRegionType () + "\nRemember it!" ) ;
			}
			System.out.println ( "MATCH CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE INITIAL CARDS : FINE " ) ;
		}
		catch ( NoMoreCardOfThisTypeException e ) 
		{
			System.out.println ( "MATCH_CONTROLLER - DISTRIBUTE_INITIAL_CARDS : NO_MORE_CARD_OF_THIS_TYPE_EXCEPTION GENERATED " ) ;
			throw new WorkflowException () ;
		}	
		catch ( WriteOncePropertyAlreadSetException e ) 
		{
			System.out.println ( "MATCH_CONTROLLER - DISTRIBUTE_INITIAL_CARDS : NO_MORE_CARD_OF_THIS_TYPE_EXCEPTION GENERATED " ) ;
			throw new RuntimeException () ;
		}
	}
	
	/**
	 * This method emulates the phase of the Game where money are given to every player.
	 * It determines the money to give to every Player based on the number of Player,
	 * and gives them them.
	 */
	private void moneyDistribution ()
	{
		int moneyToDistribute;
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - MONEY DISTRIBUTION : INIZIO " ) ;
		if ( match.getNumberOfPlayers () == 2 )
			moneyToDistribute = 30 ;
		else
			moneyToDistribute = 20 ;
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - MONEY DISTRIBUTION : DISTRIBUISCO " + moneyToDistribute + " AD OGNI GIOCATORE" ) ;
		for( Player player : match.getPlayers () )
		{
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - MONEY DISTRIBUTION : DISTRIBUISCO SOLDI AL PLAYER " + player.getName () ) ;
			player.receiveMoney ( moneyToDistribute ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - MONEY DISTRIBUTION : IL PLAYER " + player.getName () + " HA RICEVUTO I SOLDI INIZIALI." ) ;
			player.genericNotification ( "Hai ricevuto i tuoi soldi iniziali : ben " + moneyToDistribute + " denari !" ) ;
		}
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - MONEY DISTRIBUTION : FINE " ) ;
	}
	
	/**
	 * This method is the fourth in the INITIALIZATION PHASE.
	 * It performs a randomic ordering on the Players list to determine who will play
	 * first, second, and so on. 
	 * @throws WorkflowException if an unexpected error occurs 
	 */ 
	private void choosePlayersOrder () throws WorkflowException 
	{
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : INIZIO " ) ;
		Map < Player , Integer > playersMapOrder ;
		List < Player > orderedPlayers ;
		int i ;
		try 
		{
			orderedPlayers = CollectionsUtilities.newListFromIterable ( match.getPlayers () ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : RANDOMIZZANDO I GIOCATORI " ) ;
			CollectionsUtilities.listMesh ( orderedPlayers );
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : GIOCATORI RANDOMIZZATI" ) ;
			playersMapOrder = new HashMap < Player , Integer > ( orderedPlayers.size () ) ;
			i = 0 ;
			for ( Player p : orderedPlayers )
			{
				playersMapOrder.put ( p , i ) ;
				p.genericNotification ( "Il tuo turno nella partita è il numero " + ( i + 1 ) + " !" ) ;
				i ++ ;
			}
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : IMPOSTANDO L'ORDINE DEI GIOCATORI NEL MATCH." ) ;
			match.setPlayerOrder ( playersMapOrder ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : ORDINE DEI GIOCATORI NEL MATCH IMPOSTATO." ) ;			
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : FINE " ) ;
		} 
		catch ( WrongMatchStateMethodCallException e ) 
		{
			throw new WorkflowException () ;
		}
	}
	
	/**
	 * This methods describes the phase of the Game where Sheperds are distributed to Players.
	 * This procedure, determines the number of Sheperds per Player based on the total number
	 * of Players, ask each Player the color he wants for a given Sheperd and assigns Sheperd.
	 * to Players
	 */
	private void distributeSheperds () 
	{
		final Collection < NamedColor > colors ;
		NamedColor choosenColor ;
		Sheperd [] sheperds ;
		Road selectedRoad ;
		int numberOfSheperdsPerPlayer ;
		colors = CollectionsUtilities.newCollectionFromIterable( GameConstants.getSheperdColors () ) ;
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : INIZIO " ) ;
		numberOfSheperdsPerPlayer = match.getNumberOfPlayers () == 2 ? 2 : 1 ;
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : INIZIO " ) ;
		for ( Player currentPlayer : match.getPlayers () )
			if ( currentPlayer.isSuspended() == false )
			{
				try 
				{
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : GESTITSCO IL PLAYER " + currentPlayer.getName () ) ;
					sheperds = new Sheperd [ numberOfSheperdsPerPlayer ] ;		
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : CHIDEDENDO AL PLAYER " + currentPlayer.getName () + " DI SCEGLIERE UN COLORE." ) ;
					choosenColor = currentPlayer.getColorForSheperd ( colors ) ;
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : IL PLAYER " + currentPlayer.getName () + " HA SCELTO IL COLORE " + choosenColor.getName () ) ;
					colors.remove ( choosenColor ) ;				
					sheperds [ 0 ] = new Sheperd ( currentPlayer.getName () + "_#" + 0 , choosenColor , currentPlayer ) ;
					if ( numberOfSheperdsPerPlayer == 2 )
						sheperds [ 1 ] = new Sheperd ( currentPlayer.getName () + "_#" + 1 , choosenColor , currentPlayer ) ;				
					try 
					{
						System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : SETTANDO I PASTORI PER IL PLAYER " + currentPlayer.getName () ) ;
						currentPlayer.initializeSheperds ( sheperds ) ;
					}
					catch ( WriteOncePropertyAlreadSetException e ) 
					{
						// something very wrong with the current player; remove him and continue with the others.
						currentPlayer.matchEndNotification ( PresentationMessages.UNEXPECTED_ERROR_MESSAGE ) ;
						match.removePlayer ( currentPlayer ) ;
					}
					for ( Sheperd s : currentPlayer.getSheperds () )
					{
						selectedRoad = currentPlayer.chooseInitialRoadForASheperd ( match.getGameMap ().getFreeRoads () ) ;
						s.moveTo ( selectedRoad ) ;
						selectedRoad.setElementContained ( s ) ;
					}
				} 
				catch ( TimeoutException e1 ) 
				{			
					System.out.println ( "GAME CONTROLLER - DISTRIBUTE SHEPERDS : TIMEOUT EXCEPTION RAISED" ) ;
					playersGenericNotification ( "The game is going to continue without " + currentPlayer.getName() + Utilities.CARRIAGE_RETURN + "May be he will come back later..." );
			}
		}
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : FINE " ) ;
	}
	
	/**
	 * This methods implements the core phase of the Game, the time when every player
	 * makes his moves.
	 * It is implemented as a cycle, which, until the Game is finished asks every player
	 * to do his moves, providing him the tools to do this. 
	 * @throws WorkflowException if an unexpected message occurs. 
	 */ 
	private void turnationPhase () throws WorkflowException 
	{
		GameMove choosenMove ;
		MoveFactory moveFactory ;
		BlackSheep blackSheep ;
		Wolf wolf ;
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
								try 
								{
									System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - CHIEDENDO DI FARE UNA MOSSA " ) ;				
									choosenMove = currentPlayer.doMove ( moveFactory , match.getGameMap () ) ;
									if ( choosenMove != null )
									{
										System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - MOSSA SCELTA " + choosenMove.toString () ) ;									
										choosenMove.execute ( match ) ;
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
									currentPlayer.genericNotification ( "Non puoi fare questa mossa, scegline un'altra!" ) ;
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
				marketPhase () ;
				System.out.println ( "GAME CONTROLLER - TURNATION PHASE - DOPO LA FASE DI MARKET." ) ;															
				try 
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - IL LUPO PROVA A SCAPPARE " ) ;															
					wolf.escape () ;
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
	
	/**
	 * Helper method that allows the System to choose a Sheperd for a Player's turn ( eventually aking him who ),
	 * and then create a MoveFactory for this User to play.
	 * 
	 * @param currentPlayer the player for who a MoveFactory has to be built.
	 * @return the created GameMoveFactory
	 * @throws TimeoutException if the currentPlayer has to choose between some Sheperds and
	 * 	       does not answer before a timeout.
	 */
	private MoveFactory moveFactoryGenerator ( Player currentPlayer ) throws TimeoutException 
	{
		MoveFactory res ;
		Sheperd choosenSheperd ;
		System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR : INIZIO" ) ;
		if ( match.getNumberOfPlayers () == 2 )
		{
			System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR - CHIDENDO AL PLAYER : " + currentPlayer.getName () + " DI SCEGLIERE UN PASTORE" ) ;					
			choosenSheperd = currentPlayer.chooseSheperdForATurn ( currentPlayer.getSheperds () ) ;
			System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR - IL PLAYER : " + currentPlayer.getName () + " HA SCELTO IL PASTORE " + choosenSheperd.getName () ) ;									
			res = MoveFactory.newInstance ( choosenSheperd , this , lambEvolver ) ;
		}
		else
			res = MoveFactory.newInstance ( currentPlayer.getSheperds ().iterator ().next () , this , lambEvolver ) ;
		System.out.println ( "GAME CONTROLLER - MOVE FACTORY GENERATOR : END" ) ;
		return res ;
	}
	
	/**
	 * This method implements the Market phase of the Game.
	 * The code is straightforward and highly tighted with the business rules.
	 * It uses some private helper methods. 
	 */
	private void marketPhase () 
	{
		Collection < SellableCard > sellableCards ;
		Iterable < SellableCard > receivedSellableCards ;
		int amount ;
		try
		{
			for ( Player currentPlayer : match.getPlayers() )
				currentPlayer.chooseCardsEligibleForSelling () ;
			for ( Player currentPlayer : match.getPlayers () )
			{
				try
				{					
					amount = 0 ;
					// generate a List containing all the Cards this Player can buy
					sellableCards = generateGettableCardList ( currentPlayer ) ;
					// ask the User which Cards he wants to buy
					receivedSellableCards = currentPlayer.chooseCardToBuy ( sellableCards ) ;
					for ( SellableCard s : receivedSellableCards )
						amount = amount + s.getSellingPrice () ;
					// if he has enough money 
					if ( amount <= currentPlayer.getMoney () )
						for ( SellableCard s : receivedSellableCards )
							{
								s.getOwner().getSellableCards().remove ( s ) ;
								s.getOwner().receiveMoney ( s.getSellingPrice () ) ;
								currentPlayer.getSellableCards().add ( s ) ;
								currentPlayer.pay ( s.getSellingPrice() ) ;
								s.setOwner ( currentPlayer ) ;
							}
					else
						throw currentPlayer.new TooFewMoneyException () ;
				}
				catch ( NotSellableException n ) 
				{
					currentPlayer.genericNotification ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;					
				}
				catch ( SellingPriceNotSetException e )
				{
					currentPlayer.genericNotification ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;										
				}
				catch ( TooFewMoneyException t ) 
				{
					currentPlayer.genericNotification ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;					
				}
			}
		}
		catch ( TimeoutException t ) 
		{
			for ( Player p : match.getPlayers() )
				p.genericNotification ( "One of the players is not connected yet, so this market phase can not go away." + Utilities.CARRIAGE_RETURN + "It's not our fault, it's his !!!" );
		}
	}
	
	/**
	 * This method generate a Collection containing all the Cards ( owned buy Match Players ) which
	 * the Player passed buy parameter is able to buy.
	 * 
	 * @param buyer the Player who is going to buy some cards.
	 * @return a Collection < SellableCard > containing all the Cards owned by other Players than
	 * 		   the one passed by parameter which the buyer Player is able to buy. 
	 */
	private Collection < SellableCard > generateGettableCardList ( Player buyer ) 
	{
		Collection < SellableCard > res ;
		res = new LinkedList < SellableCard > () ;
		for ( Player p : match.getPlayers () )
			if ( buyer.equals ( buyer ) )
				for ( SellableCard s : p.getSellableCards () )
						if ( s.isSellable () )
							res.add ( s ) ;
		return res ;
	}
	
	/**
	 * This move is the last in the game workflow.
	 * It calculate the points that every Player did and communicate the winner. 
	 * @throws WorkflowException in an unexpected error occurs 
	 */ 
	private void resultsCalculationPhase () throws WorkflowException 
	{
		MatchResultsCalculator matchResultsCalculator ;
		Map <Player, Integer> playerScoresMap;
		try 
		{
			matchResultsCalculator = new MatchResultsCalculator ( match ) ;
			playerScoresMap = new HashMap < Player , Integer> ( match.getNumberOfPlayers () ) ;
			for ( Player p : match.getPlayers () )
				playerScoresMap.put ( p , matchResultsCalculator.calculatePlayerScore ( p ) ) ;
			for ( Player p : match.getPlayers () )
				p.genericNotification ( "Your points : " + playerScoresMap.get ( p ) ) ;
		}
		catch ( WrongMatchStateMethodCallException e ) 
		{
			throw new WorkflowException () ;
		}
		
	}

	/***/
	private void matchFinishingProcedure ( String msg ) 
	{
		for ( Player currentPlayer : match.getPlayers () )
			currentPlayer.matchEndNotification ( msg ) ;
	}
	
	/**
	 * 
	 * */
	private void playersGenericNotification ( String msg )
	{
		for ( Player p : match.getPlayers() )
			if ( p.isSuspended() == false )
				p.genericNotification ( msg ) ;
	}
	
	// INNER CLASSES
	
	/**
	 * Utility class which implements the Task the GameController Timer has to do
	 * when the Timer expires.
	 * It set the MatchState to the INITIALIZATION value, and cancel the Timer. 
	 */
	private class WaitingPlayersTimerTask extends TimerTask implements Serializable
	{

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run ()
		{
			if ( match.getNumberOfPlayers() >= 2 ) 
			{
				System.out.println ( "GAME CONTROLLER - TIMER SCATTATO : NUMERO DI GIOCATORI : " + match.getNumberOfPlayers() ) ;
				match.setMatchState ( MatchState.INITIALIZATION ) ;
				matchStartCommunicationController.notifyFinishAddingPlayers () ;
			}
			else
			{
				System.out.println ( "GAME CONTROLLER - TIMER SCATTATO : NUMERO DI GIOCATORI : " + match.getNumberOfPlayers() + ", PARTITA RESPINTA." ) ;				
				matchStartCommunicationController.notifyFailStartMatch () ;
			}
			cancel () ;
		}
		
	}
	
}

/**
 * This class implements a MatchIdentifier for the Match managed by this GameController  
 */
class MatchIdentifier implements Identifiable < Match > 
{

	/**
	 * The unique identifier for this MatchIdentifier.
	 */
	private int uid ;
	
	/**
	 * @param uid the unique identifier for this MatchIdentifier. 
	 */
	protected MatchIdentifier ( int uid ) 
	{
		this.uid = uid ;
	}
	
	/**
	 * Getter method for the uid property.
	 * 
	 * @return the uid property. 
	 */
	public int getUID () 
	{
		return uid ;
	}
	
	/**
	 * Determine if this MatchIdentifier object is the same as the one
	 * passed by parameter.
	 * 
	 * @param otherObject the otherObject to compare this one.
	 * @return true if this object is equals to the one passed by parameter, false else. 
	 */
	public boolean isEqualsTo ( Identifiable<Match> otherObject ) 
	{
		if ( otherObject instanceof MatchIdentifier )
			return uid == ( ( MatchIdentifier ) otherObject).getUID () ;
		return false ;
	}

}

/**
 * Component that manages the evolution of the Lambs in the Game. 
 */
class LambEvolverImpl implements LambEvolver , Serializable
{

	/**
	 * An AnimalFactory object to replace the evolving Lamb. 
	 */
	private AnimalFactory animalFactory ;
	
	/**
	 * @param animalFactory the value for the animalFactory instance.
	 * @throws IllegalArgumentException if the animalFactory parameter is null. 
	 */
	protected LambEvolverImpl ( AnimalFactory animalFactory ) 
	{
		if ( animalFactory != null )
			this.animalFactory = animalFactory ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void evolve ( Lamb lamb ) 
	{
		Region whereTheLambIsNow ;
		Ovine newOvine ;
		if ( lamb != null )
		{
			whereTheLambIsNow = lamb.getPosition () ;
			whereTheLambIsNow.removeAnimal ( lamb ) ;
			lamb.moveTo ( null ) ;
			newOvine = animalFactory.newAdultOvine ( lamb.getName () + "EVOLVED" , MathUtilities.genProbabilityValue() > 0.5 ? AdultOvineType.RAM : AdultOvineType.SHEEP ) ;
			newOvine.moveTo ( whereTheLambIsNow ) ;
			whereTheLambIsNow.addAnimal ( newOvine ) ;
		}
		else
			throw new IllegalArgumentException () ;	
	}

}

/**
 * Component that calculate the results of a Match 
 */
class MatchResultsCalculator 
{

	/**
	 * The match where operate. 
	 */
	private Match match ;
	
	/**
	 * A Map containing the points that every regions has. 
	 */
	private Map <RegionType, Integer> regionValuesMap;
	
	/**
	 * @param match the match where operate.
	 * @throws IllegalArgumentException if the match parameter is null.
	 * @throws WrongMatchStateMethodCallException if the match parameter's state is not CALCULATING_RESULTS. 
	 */
	protected MatchResultsCalculator ( Match match ) throws WrongMatchStateMethodCallException
	{
		if ( match != null )
			if ( match.getMatchState() == MatchState.CALCULATING_RESULTS )
			{
				this.match = match ;
				regionValuesMap = new HashMap < RegionType , Integer > ( RegionType.values ().length - 1 ) ;
			}
			else
				throw new WrongMatchStateMethodCallException ( match.getMatchState () ) ;
		else
			throw new IllegalArgumentException () ;
		
	}

	/**
	 * Helper method to calculate the points for every region. 
	 */
	private void calculateRegionsResults () 
	{
		for ( RegionType r : RegionType.values () )
			if ( r != RegionType.SHEEPSBURG )
				regionValuesMap.put ( r , calculateRegionValue ( r ) ) ;
	}
	
	/**
	 * This method calculate the value of the RegionType passed by parameter.
	 * The procedure used to determine this value is the one specified by business rules.
	 * 
	 * @param rt the RegionType about that calculate the value.
	 * @return the value of the RegionType passed by parameter.
	 */
	private int calculateRegionValue ( RegionType rt )
	{
		Iterable<Region> regions;
		Iterable<Animal> animals;
		int amount;
		amount = 0;
		regions = match.getGameMap().getRegionByType ( rt ) ;
		for(Region r : regions)
		{
			animals = r.getContainedAnimals();
			for ( Animal a : animals )
				if ( ! ( a instanceof Wolf ) )
				{
					if ( a instanceof BlackSheep )
						amount = amount + 2;
					else 
						amount = amount + 1;
				}
		}
		return amount ;	
	}
	
	/**
	 * Calculate the score of the Player passed by parameter and returns it.
	 * 
	 * @param player the Player on which calculate the score.
	 * @param regionValuesMap a map containing, for each RegionType, the value of that RegionType in
	 *        this Match.
	 * @return the score of the Player passed by parameter.
	 */
	public int calculatePlayerScore ( Player player )
	{
		int res;
		Collection <Card> playerCards;
		if ( regionValuesMap.isEmpty () )
			calculateRegionsResults () ;
		playerCards = new ArrayList<Card>(player.getSellableCards().size() + 1);
		playerCards.addAll(player.getSellableCards());
		playerCards.add(player.getInitialCard());
		res = 0;
		for(Card card : playerCards)
			res = res + regionValuesMap.get(card.getRegionType());
		return 0;	
	}
	
}