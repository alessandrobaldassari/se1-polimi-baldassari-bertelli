package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.TimeConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.NetworkCommunicantPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
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
public class MatchController implements Runnable , ConnectionLoosingManagerObserver , Serializable
{
	
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
		
	/***/
	private transient Iterable < GameMapObserver > mapObservers ;
	
	/**
	 * @param matchStartCommunicationController the value for the matchStartCommunicationController field.
	 * @throws IllegalArgumentException if the parameter passed is null.
	 */
	public MatchController ( MatchStartCommunicationController matchStartCommunicationController , Iterable < GameMapObserver > mapObservers ) 
	{
		if ( matchStartCommunicationController != null && mapObservers != null )
		{
			this.matchStartCommunicationController = matchStartCommunicationController ;
			timer = new Timer();
			tempBlockingQueue = new LinkedBlockingQueue < Player > () ;
			this.mapObservers = mapObservers ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public void addPlayerObserver ( PlayerObserver playerObserver , int clientHandlerUID ) 
	{
		for ( Player p : match.getPlayers () )
			if ( ( ( NetworkCommunicantPlayer ) p ).getClientHandler ().getUID() == clientHandlerUID )
				( ( NetworkCommunicantPlayer ) p ).addObserver(playerObserver);
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
			playersGenericNotification ( "We are all ok again." + Utilities.CARRIAGE_RETURN + "The show can go on!" ) ;
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
		//while ( match == null ) ;
		System.out.println ( "MATCH_CONTROLLER - ADD_PLAYER : match = " + match ) ;
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
		catch ( WorkflowException e ) 
		{
			System.out.println ( "MATCH_CONTROLLER - RUN : WORKFLOW_EXCEPTION GENERATED " + e.getMessage () ) ;
			endMessage = PresentationMessages.UNEXPECTED_ERROR_MESSAGE + Utilities.CARRIAGE_RETURN + e.getMessage() ;
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
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : BEGIN" ) ;
			matchIdentifier = MatchIdentifier.newInstance () ;
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : MATCH IDENTIFIER CREATED" ) ;
			animalsFactory = AnimalFactory.newAnimalFactory ( matchIdentifier ) ;
			lambEvolver = new LambEvolverImpl ( animalsFactory ) ;
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : LAMB EVOLVER CREATED" ) ;
			gameMap = GameMapFactory.getInstance().newInstance ( matchIdentifier ) ;
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : GAME MAP CREATED" ) ;
			bank = BankFactory.getInstance().newInstance ( matchIdentifier ) ;
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : BEFORE CREATING MATCH" ) ;
			match = new Match ( gameMap , bank ) ;		
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : MATCH CREATED" ) ;
			match.setMatchState ( MatchState.WAIT_FOR_PLAYERS ) ;
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : BEFORE ADDING MAP_OBSERVERS" ) ;			
			for ( GameMapObserver g : mapObservers )
				match.getGameMap().addObserver ( g ) ;
			timer.schedule ( new WaitingPlayersTimerTask () , TimeConstants.MATCH_CONTROLLER_TIMER_TIME ) ;
			System.out.println ( "MATCH_CONTROLLER - CREATING_PHASE : END" ) ;
		} 
		catch ( SingletonElementAlreadyGeneratedException e ) 
		{
			System.out.println ( "MATCH_CONTROLLER - CREATING PHASE : SINGLETON_ELEMENT_ALREADY_GENERATED_EXCETPION" ) ;
			throw new WorkflowException ( PresentationMessages.UNEXPECTED_ERROR_MESSAGE ) ;
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
					playersGenericNotification ( "Un nuovo giocatore è arrivato, il suo nome è " + newPlayer.getName () ) ;
					System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYERS PHASE - PLAYER AGGIUNTO AL MATCH" ) ;
					if ( match.getNumberOfPlayers () == GameConstants.MAX_NUMBER_OF_PLAYERS ) 
					{
						System.out.println ( "GAME CONTROLLER : WAIT FOR PLAYERS PHASE - MASSIMO NUMERO DI GIOCATORI RAGGIUNTO" ) ;
						timer.cancel () ;
						match.setMatchState ( MatchState.INITIALIZATION ) ;
						matchStartCommunicationController.notifyFinishAddingPlayers () ;
						// notify the user with the player abstract class. 
					}
				}
			}
			catch ( WrongMatchStateMethodCallException e ) 
			{
				throw new WorkflowException ( PresentationMessages.UNEXPECTED_ERROR_MESSAGE ) ;
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
					// ask the player a color
					choosenColor = currentPlayer.getColorForSheperd ( colors ) ;
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : IL PLAYER " + currentPlayer.getName () + " HA SCELTO IL COLORE " + choosenColor.getName () ) ;
					colors.remove ( choosenColor ) ;				
					// create sheperds
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
						// ask the player a position for one of his players
						selectedRoad = currentPlayer.chooseInitialRoadForASheperd ( match.getGameMap ().getFreeRoads () ) ;
						selectedRoad = match.getGameMap ().getRoadByUID ( selectedRoad.getUID() ) ;
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
	
	/***/
	private void turnationPhase () throws WorkflowException 
	{
		TurnationPhaseManager turnationPhaseManager ;
		turnationPhaseManager = new TurnationPhaseManager ( match , lambEvolver ) ;
		turnationPhaseManager.turnationPhase();
	}	
	
	/**
	 * This move is the last in the game workflow.
	 * It calculate the points that every Player did and communicate the winner. 
	 * @throws WorkflowException in an unexpected error occurs 
	 */ 
	private void resultsCalculationPhase () throws WorkflowException 
	{
		ResultsCalculatorManager matchResultsCalculator ;
		Map <Player, Integer> playerScoresMap;
		Player winner ;
		winner = null ;
		int max = -1 ;
		try 
		{
			matchResultsCalculator = new ResultsCalculatorManager ( match ) ;
			playerScoresMap = new HashMap < Player , Integer> ( match.getNumberOfPlayers () ) ;
			for ( Player p : match.getPlayers () )
				playerScoresMap.put ( p , matchResultsCalculator.calculatePlayerScore ( p ) ) ;
			for ( Player p : playerScoresMap.keySet() )
				if ( playerScoresMap.get ( p ) > max )
				{
					winner = p ;
					max = playerScoresMap.get ( p ) ;
				}
			for ( Player p : match.getPlayers() )
				if ( p.equals ( winner ) )
					p.genericNotification ( "Hai vinto !!!" );
				else
					p.genericNotification ( "Il vincitore è : " + winner.getName () ) ;
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
			if ( currentPlayer.isSuspended() == false )
			currentPlayer.matchEndNotification ( msg ) ;
	}
	
	/**
	 * Notifies all the non suspended Players in the Match of a Message
	 * 
	 * @param msg the message to pass.
	 */
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

