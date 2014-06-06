package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingManager.ConnectionLoosingManagerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunchercommunicationcontroller.MatchStartCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import java.awt.Color;
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
public class GameController implements Runnable , TurnNumberClock , LambEvolver , ConnectionLoosingManagerObserver
{
	
	/**
	 * Static variable to generate id's for the Match objects here created. 
	 */
	private static int lastMatchIdentifierUID = -1 ;
	
	/**
	 * The Timer value about the time to wait before begin a Match. 
	 */
	private static final long DELAY = 30 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/**
	 * The maximum number of Player for a Match. 
	 */
	private static final int MAX_NUMBER_OF_PLAYERS = 4;

	/**
	 * The number of moves a Player can do each turn.
	 * It's a business rule. 
	 */
	private static final int NUMBER_OF_MOVES_PER_USER_PER_TURN = 3 ;
	
	/**
	 * A standard Timer object to mange the timer at the beginning of a Match.
	 * It's a business rule. 
	 */
	private final Timer timer;
	
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
	 * An AnimalsFactory object to create all the Animal objects who play in the Game. 
	 */
	private AnimalFactory animalsFactory ;
	
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
	public GameController ( MatchStartCommunicationController matchStartCommunicationController ) 
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
	public int getTurnNumber () throws WrongMatchStateMethodCallException
	{
		int res ;
		if ( match.getMatchState () == MatchState.TURNATION )
			res = turnNumber ;
		else
			throw new WrongMatchStateMethodCallException ( match.getMatchState () ) ;
		return res ;
	} 
	
	public void evolve ( Lamb lamb ) 
	{
		Region whereTheLambIsNow ;
		Ovine newOvine ;
		if ( lamb != null )
		{
			whereTheLambIsNow = lamb.getPosition () ;
			whereTheLambIsNow.removeAnimal ( lamb ) ;
			newOvine = animalsFactory.newAdultOvine ( MathUtilities.genProbabilityValue() > 0.5 ? AdultOvineType.RAM : AdultOvineType.SHEEP ) ;
			newOvine.moveTo ( whereTheLambIsNow ) ;
			whereTheLambIsNow.addAnimal ( newOvine ) ;
		}
		else
			throw new IllegalArgumentException () ;
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
	}
	
	/**
	 * The first method executed by this controller during it's logical lifecycle.
	 * It creates all the resources necessary to the game and starts a Timer
	 * to wait for all players to add in. 
	 * 
	 * @throws RuntimeException if the AnimalFactory Singleton mechanism fails.
	 */
	private void creatingPhase () 
	{
		GameMap gameMap ;
		Bank bank;
		try 
		{
			lastMatchIdentifierUID ++ ;
			matchIdentifier = new MatchIdentifier ( lastMatchIdentifierUID ) ;
			animalsFactory = AnimalFactory.newAnimalFactory ( matchIdentifier ) ;
			gameMap = GameMapFactory.getInstance().newInstance ( matchIdentifier ) ;
			bank = BankFactory.getInstance().newInstance ( matchIdentifier ) ;
			match = new Match ( gameMap , bank ) ;		
			match.setMatchState ( MatchState.WAIT_FOR_PLAYERS );
			timer.schedule ( new WaitingPlayersTimerTask () , DELAY ) ;
		} 
		catch ( SingletonElementAlreadyGeneratedException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
	}
	
	/**
	 * This method is in the second phase of the Game lifecycle.
	 * It wait for players to arrive.
	 * When they do it, adds them to the Match, and if the MAX_NUMBER_OF_PLAYERS is reached,
	 * cause the Match workflow to finish the INITIALIZATION phase and to move to the next phase. 
	 */
	private void waitForPlayersPhase () 
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
					if ( match.getNumberOfPlayers () == MAX_NUMBER_OF_PLAYERS ) 
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
				e.printStackTrace();
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
	public void initializationPhase ()
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
	private void placeSheeps () 
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
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - PLACE SHEEPS : FINE " ) ;
		} 
		catch ( BlackSheepAlreadyGeneratedException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		} 
		catch ( WolfAlreadyGeneratedException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
	}
	
	/**
	 * This methods emulates the phase where one Initial Card is given to each Player.
	 * Which Card give to each Player is a randomic decision.
	 */
	private void distributeInitialCards ()
	{
		Stack < RegionType > regions ;
		try 
		{
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE INITIAL CARDS : INIZIO " ) ;
			regions = new Stack < RegionType > () ;
			for ( RegionType type : RegionType.values () ) 
				regions.push ( type ) ;
			regions.remove ( RegionType.SHEEPSBURG ) ;
			CollectionsUtilities.listMesh ( regions ) ;
			for( Player player : match.getPlayers () )
				player.setInitialCard ( match.getBank ().takeInitialCard ( regions.pop() ) ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE INITIAL CARDS : FINE " ) ;
		}
		catch ( NoMoreCardOfThisTypeException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}	
		catch ( WriteOncePropertyAlreadSetException e ) 
		{
			e.printStackTrace () ;
			throw new RuntimeException ( e ) ;
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
		}
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - MONEY DISTRIBUTION : FINE " ) ;
	}
	
	/**
	 * This method is the fourth in the INITIALIZATION PHASE.
	 * It performs a randomic ordering on the Players list to determine who will play
	 * first, second, and so on. 
	 */
	private void choosePlayersOrder () 
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
			System.out.println ( "IIIIIIIIIIIIIIIIIII" ) ;			
			for ( Player p : orderedPlayers )
			{
				playersMapOrder.put ( p , i ) ;
				i ++ ;
			}
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : IMPOSTANDO L'ORDINE DEI GIOCATORI NEL MATCH." ) ;
			match.setPlayerOrder ( playersMapOrder ) ;
			System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : ORDINE DEI GIOCATORI NEL MATCH IMPOSTATO." ) ;			
		} 
		catch ( WrongMatchStateMethodCallException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException (e);
		}
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - CHOOSE PLAYER ORDER PHASE : FINE " ) ;
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
		byte numberOfSheperdsPerPlayer ;
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : INIZIO " ) ;
		colors = new LinkedList < NamedColor > () ;
		colors.add ( new NamedColor ( Color.RED.getRed() , Color.RED.getGreen() , Color.RED.getBlue() , "RED" ) ) ;
		colors.add ( new NamedColor ( Color.BLUE.getRed() , Color.BLUE.getGreen() , Color.BLUE.getBlue() , "BLUE" ) ) ;
		colors.add ( new NamedColor ( Color.GREEN.getRed() , Color.GREEN.getGreen() , Color.GREEN.getBlue() , "GREEN" ) ) ;
		colors.add ( new NamedColor ( Color.YELLOW.getRed () , Color.YELLOW.getGreen () , Color.YELLOW.getBlue () , "YELLOW" ) ) ;
		if ( match.getNumberOfPlayers() == 2 )
			numberOfSheperdsPerPlayer = 2 ;
		else
			numberOfSheperdsPerPlayer = 1 ;
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : INIZIO " ) ;
		for ( Player p : match.getPlayers () )
		{
			if ( p.isSuspended() == false )
			{
				try 
				{
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : GESTITSCO IL PLAYER " + p.getName () ) ;
					sheperds = new Sheperd [ numberOfSheperdsPerPlayer ] ;		
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : CHIDEDENDO AL PLAYER " + p.getName () + " DI SCEGLIERE UN COLORE." ) ;
					choosenColor = p.getColorForSheperd ( colors ) ;
					System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : IL PLAYER " + p.getName () + " HA SCELTO IL COLORE " + choosenColor.getName () ) ;
					colors.remove ( choosenColor ) ;				
					sheperds [ 0 ] = new Sheperd ( p.getName () + "_#" + 0 , choosenColor , p ) ;
					if ( numberOfSheperdsPerPlayer == 2 )
						sheperds [ 1 ] = new Sheperd ( p.getName () + "_#" + 1 , choosenColor , p ) ;				
					try 
					{
						System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : SETTANDO I PASTORI PER IL PLAYER " + p.getName () ) ;
						p.initializeSheperds ( sheperds ) ;
					}
					catch ( WriteOncePropertyAlreadSetException e ) 
					{
						e.printStackTrace () ;
						throw new RuntimeException ( e ) ;
					}
					for ( Sheperd s : p.getSheperds () )
					{
						//selectedRoad = p.chooseInitialRegionForASheperd ( match.getGameMap ().getFreeRoads () ) ;
						selectedRoad = match.getGameMap ().getFreeRoads ().iterator().next () ;
						s.moveTo ( selectedRoad ) ;
						selectedRoad.setElementContained ( s ) ;
					}
				} 
				catch ( TimeoutException e1 ) 
				{					
					notifyPlayerDisconnected ( p ) ;
				}
			}
		}
		System.out.println ( "GAME CONTROLLER - INITIALIZATION PHASE - DISTRIBUTE SHEPERDS PHASE : FINE " ) ;
	}
		
	/**
	 * This methods implements the core phase of the Game, the time when every player
	 * makes his moves.
	 * It is implemented as a cycle, which, until the Game is finished asks every player
	 * to do his moves, providing him the tools to do this. 
	 */
	private void turnationPhase () 
	{
		GameMove choosenMove ;
		Sheperd choosenSheperd = null ;
		MoveFactory moveFactory ;
		BlackSheep blackSheep ;
		Wolf wolf ;
		byte moveIndex ;
		boolean gamePlaying ;
		System.out.println ( "GAME CONTROLLER - TURNATION PHASE : INIZIO " ) ;
		blackSheep = findBlackSheep () ;
		wolf = findWolf () ;
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
			for ( Player currentPlayer : match.getPlayers() )
			{				
				System.out.println ( "GAME CONTROLLER - TURNATION PHASE - TURNO DEL PLAYER : " + currentPlayer.getName () ) ;
				if ( match.isInFinalPhase () )
					break ;
				if ( match.getNumberOfPlayers () == 2 )
				{
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - CHIDENDO AL PLAYER : " + currentPlayer.getName () + " DI SCEGLIERE UN PASTORE" ) ;					
					try 
					{
						choosenSheperd = currentPlayer.chooseSheperdForATurn () ;
					} 
					catch ( TimeoutException e ) {}
					System.out.println ( "GAME CONTROLLER - TURNATION PHASE - IL PLAYER : " + currentPlayer.getName () + " HA SCELTO IL PASTORE " + choosenSheperd.getName () ) ;									
					moveFactory = MoveFactory.newInstance ( choosenSheperd , this , this ) ;
				}
				else
					moveFactory = MoveFactory.newInstance ( currentPlayer.getSheperds ().iterator ().next () , this , this ) ;
				try
				{
					for ( moveIndex = 0 ; moveIndex < NUMBER_OF_MOVES_PER_USER_PER_TURN ; moveIndex ++ )
					{	
						try 
						{
							System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - CHIEDENDO DI FARE UNA MOSSA " ) ;				
							choosenMove = currentPlayer.doMove ( moveFactory , match.getGameMap () ) ;
							System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - MOSSA SCELTA " + choosenMove.toString () ) ;									
							choosenMove.execute ( match ) ;
							System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " HA ESEGUITO LA MOSSA." ) ;									
						} 
						catch ( MoveNotAllowedException e ) 
						{
							System.out.println ( "GAME CONTROLLER - TURNATION PHASE - PLAYER : " + currentPlayer.getName () + " - ERRORE DURANTE L'ESECUZIONE DELLA MOSSA." ) ;									
							e.printStackTrace();
						} 
					}
				}
				catch ( TimeoutException t ) 
				{
					notifyPlayerDisconnected ( currentPlayer );
				}
				if ( match.isInFinalPhase () == false && match.getBank().hasAFenceOfThisType ( FenceType.NON_FINAL ) == false )
					try 
					{
						System.out.println ( "GAME CONTROLLER - TURNATION PHASE - ENTRO NELLA FASE FINALE " ) ;															
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
	
	/**
	 * This method finds the only BlackSheep which, at the beginning of the Game is in Sheepsburg.
	 * 
	 * @return a reference to the only BlackSheep present in the GameMap associated with this GameController
	 */
	private BlackSheep findBlackSheep () 
	{
		Iterable < Animal > sheepsburgAnimals ;
		BlackSheep result ;
		Region sheepsburg ;
		sheepsburg = match.getGameMap().getRegionByType ( RegionType.SHEEPSBURG ).iterator().next () ;
		sheepsburgAnimals = sheepsburg.getContainedAnimals () ;
		result = null ;
		for ( Animal a : sheepsburgAnimals )
			if ( a instanceof BlackSheep )
			{
				result = ( BlackSheep ) a ;
				break ;
			}
		return result ;
	}
	
	/**
	 * This method finds the only Wolf which, at the beginning of the Game is in Sheepsburg.
	 * 
	 * @return a reference to the only Wolf present in the GameMap associated with this GameController
	 */
	private Wolf findWolf () 
	{
		Iterable < Animal > sheepsburgAnimals ;
		Wolf result ;
		Region sheepsburg ;
		sheepsburg = match.getGameMap().getRegionByType ( RegionType.SHEEPSBURG ).iterator().next () ;
		sheepsburgAnimals = sheepsburg.getContainedAnimals () ;
		result = null ;
		for ( Animal a : sheepsburgAnimals )
			if ( a instanceof Wolf )
			{
				result = ( Wolf ) a ;
				break ;
			}
		return result ;
	}
	
	/**
	 * This method implements the Market phase of the Game.
	 * The code is straightforward and highly tighted with the business rules.
	 * It uses some private helper methods. 
	 */
	private void marketPhase () 
	{
		Collection < SellableCard > choosenCards ;
		Collection < SellableCard > sellableCards ;
		SellableCard sellableCard ;
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
					choosenCards = new LinkedList < SellableCard > () ;
					sellableCards = generateGettableCardList ( currentPlayer ) ;
					sellableCard = currentPlayer.chooseCardToBuy ( sellableCards ) ;
					if ( sellableCard != null && currentPlayer.getMoney () >= sellableCard.getSellingPrice () )
					{
						amount = amount + sellableCard.getSellingPrice () ;
						choosenCards.add ( sellableCard ) ;
						sellableCards.remove ( sellableCard ) ;
						sellableCard = currentPlayer.chooseCardToBuy ( sellableCards ) ;
						while ( sellableCard != null && currentPlayer.getMoney () >= amount + sellableCard.getSellingPrice () )
						{
							amount = amount + sellableCard.getSellingPrice () ;
							choosenCards.add ( sellableCard ) ;
							sellableCards.remove ( sellableCard ) ;
							sellableCard = currentPlayer.chooseCardToBuy ( sellableCards ) ;
						}
						for ( SellableCard s : choosenCards )
						{
							s.getOwner().getSellableCards().remove ( s ) ;
							s.getOwner().receiveMoney ( s.getSellingPrice () ) ;
							currentPlayer.getSellableCards().add ( s ) ;
							currentPlayer.pay ( s.getSellingPrice() ) ;
							s.setOwner ( currentPlayer ) ;
						}
					}
					else
					{
						// non ha soldi per comperare nemmeno una carta.
					}
				}
				catch ( NotSellableException n ) {}
				catch ( SellingPriceNotSetException e ){}
				catch ( TooFewMoneyException t ) {}
			}
		}
		catch ( TimeoutException t ) 
		{
			for ( Player p : match.getPlayers() )
				p.genericNotification ( "One of the players is not connected yet, so this market phase can not go away.\nIt's not our fault, it's his !!!" );
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
	 */
	private void resultsCalculationPhase () 
	{
		Map <RegionType, Integer> regionValuesMap;
		Map <Player, Integer> playerScoresMap;
		playerScoresMap = new HashMap < Player , Integer> ( match.getNumberOfPlayers () ) ;
		regionValuesMap = new HashMap < RegionType , Integer > ( RegionType.values ().length - 1 ) ;
		for ( RegionType r : RegionType.values () )
			if ( r != RegionType.SHEEPSBURG )
				regionValuesMap.put ( r , calculateRegionValue ( r ) ) ;
		for ( Player p : match.getPlayers () )
			playerScoresMap.put(p, calculatePlayerScore ( p , regionValuesMap ) ) ;	
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
	private int calculatePlayerScore ( Player player , Map < RegionType , Integer > regionValuesMap )
	{
		int res;
		Collection <Card> playerCards;
		playerCards = new ArrayList<Card>(player.getSellableCards().size() + 1);
		playerCards.addAll(player.getSellableCards());
		playerCards.add(player.getInitialCard());
		res = 0;
		for(Card card : playerCards)
			res = res + regionValuesMap.get(card.getRegionType());
		return 0;	
	}
	
	private void notifyPlayerDisconnected ( Player disconnectedPlayer ) 
	
	{
		for ( Player p : match.getPlayers() )
			if ( p.isSuspended() == false )
				p.genericNotification ( "The game is going to continue without " + disconnectedPlayer.getName() + "\nMay be he will come back later..." );
	}
	
	// INNER CLASSES
	
	/**
	 * Utility class which implements the Task the GameController Timer has to do
	 * when the Timer expires.
	 * It set the MatchState to the INITIALIZATION value, and cancel the Timer. 
	 */
	private class WaitingPlayersTimerTask extends TimerTask
	{

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
	
	/**
	 * This class implements a MatchIdentifier for the Match managed by this GameController  
	 */
	private class MatchIdentifier implements Identifiable < Match > 
	{

		/**
		 * The unique identifier for this MatchIdentifier.
		 */
		private int uid ;
		
		/**
		 * @param uid the unique identifier for this MatchIdentifier. 
		 */
		public MatchIdentifier ( int uid ) 
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

	@Override
	public void onConnectionRetrieved ( Suspendable retrievedElem ) 
	{
		
	}

	@Override
	public void onBeginSuspensionControl ( Suspendable pendant ) 
	{
		for ( Player p : match.getPlayers() )
			if ( p.equals ( pendant ) == false )
				p.genericNotification ( "We all wait a few time to retrieve a pendant player..." );
	}

	@Override
	public void onEndSuspensionControl ( boolean suspendedRetrieved ) 
	{
		if ( suspendedRetrieved )
			for ( Player p : match.getPlayers() )
				p.genericNotification ( "We are all ok again.\nThe show can go on!" );
	}
	
}
