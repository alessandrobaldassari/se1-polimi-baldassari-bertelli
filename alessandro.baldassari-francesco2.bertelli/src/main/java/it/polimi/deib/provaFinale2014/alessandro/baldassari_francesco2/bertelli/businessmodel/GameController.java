package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.AlreadyInFinalPhaseException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.TwoPlayersMatchMoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.MatchStartCommunicationController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
public class GameController implements Runnable , TurnNumberClock , LambEvolver
{
	
	/**
	 * Static variable to generate id's for the Match objects here created. 
	 */
	private static int lastMatchIdentifierUID = -1 ;
	
	/**
	 * The Timer value about the time to wait before begin a Match. 
	 */
	private static final long DELAY = 60 * 1000;
	
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
	public int getTurnNumber () throws WrongStateMethodCallException
	{
		int res ;
		if ( match.getMatchState () == MatchState.TURNATION )
			res = turnNumber ;
		else
			throw new WrongStateMethodCallException ( match.getMatchState () ) ;
		return res ;
	} 
	
	public void evolve ( Lamb lamb ) 
	{
		Region whereTheLambIsNow ;
		Ovine newOvine ;
		if ( lamb != null )
		{
			whereTheLambIsNow = lamb.getPosition () ;
			whereTheLambIsNow.getContainedAnimals().remove ( lamb ) ;
			newOvine = animalsFactory.newAdultOvine ( "" , MathUtilities.genProbabilityValue() > 0.5 ? AdultOvineType.RAM : AdultOvineType.SHEEP ) ;
			newOvine.moveTo ( whereTheLambIsNow ) ;
			whereTheLambIsNow.getContainedAnimals().add ( newOvine ) ;
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
	 * @throws WrongStateMethodCallException if this method is not called during the WAIT_FOR_PLAYERS
	 *         phase.
	 * 
	 */
	public void addPlayer ( Player newPlayer ) throws WrongStateMethodCallException
	{
		if ( match.getMatchState () == Match.MatchState.WAIT_FOR_PLAYERS )
			tempBlockingQueue.offer ( newPlayer ) ;
		else
			throw new WrongStateMethodCallException ( match.getMatchState () ) ;
	}
	
	/**
	 * The run method of this GameController, which implements the Game Workflow, using
	 * appropriate private helper methods that describe each game phase.
	 */
	public void run () 
	{
		creatingPhase () ;
		waitForPlayersPhase () ;
		initializationPhase () ; 
		turnationPhase () ;
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
			matchIdentifier = new MatchIdentifier ( lastMatchIdentifierUID ++ ) ;
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
		while ( match.getMatchState() != MatchState.INITIALIZATION )
		{
			try 
			{
				newPlayer = tempBlockingQueue.take () ;
				match.addPlayer ( newPlayer ) ;
				if ( match.getNumberOfPlayers () == MAX_NUMBER_OF_PLAYERS ) 
				{
					timer.cancel () ;
					match.setMatchState ( MatchState.INITIALIZATION ) ;
					matchStartCommunicationController.notifyFinishAddingPlayers () ;
				}	
			}
			catch ( InterruptedException e ) 
			{
				e.printStackTrace () ;
			} 
			catch ( WrongStateMethodCallException e ) 
			{
				e.printStackTrace();
			}			
		}
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
		placeSheeps () ;
		distributeInitialCards () ;
		moneyDistribution () ;
		choosePlayersOrder () ;
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
			for ( Region region : match.getGameMap ().getRegions () ) 
			{
				bornOvine = animalsFactory.newAdultOvine ( "" , MathUtilities.genProbabilityValue() > 0.5 ? AdultOvineType.RAM : AdultOvineType.SHEEP ) ;
				region.getContainedAnimals().add ( bornOvine ) ;
				bornOvine.moveTo ( region ) ;
			}
			sheepsburg = match.getGameMap ().getRegionByType ( RegionType.SHEEPSBURG ).iterator().next() ;
			blackSheep = animalsFactory.newBlackSheep () ;
			wolf = animalsFactory.newWolf () ;
			sheepsburg.getContainedAnimals ().add ( animalsFactory.newBlackSheep () ) ;
			sheepsburg.getContainedAnimals().add ( animalsFactory.newWolf () ) ;
			blackSheep.moveTo ( sheepsburg ) ;
			wolf.moveTo ( sheepsburg ) ;
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
		List < RegionType > regions ;
		try 
		{
			regions = new ArrayList <RegionType> ( RegionType.values().length ) ;
			for ( RegionType type : RegionType.values () )
				regions.add ( type ) ;
			regions.remove ( RegionType.SHEEPSBURG ) ;
			CollectionsUtilities.listMesh ( regions ) ;
			for( Player player : match.getPlayers ())
			{
					player.setInitialCard ( match.getBank ().takeInitialCard ( regions.get ( 0 ) ) ) ;
				regions.remove ( 0 ) ;
			}
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
		if ( match.getNumberOfPlayers () == 2 )
			moneyToDistribute = 30 ;
		else
			moneyToDistribute = 20 ;
		for( Player player : match.getPlayers () )
			player.receiveMoney ( moneyToDistribute ) ;
	}
	
	/**
	 * This method is the fourth in the INITIALIZATION PHASE.
	 * It performs a randomic ordering on the Players list to determine who will play
	 * first, second, and so on. 
	 */
	private void choosePlayersOrder () 
	{
		Map < Player , Integer > playersMapOrder ;
		List < Player > orderedPlayers ;
		int i ;
		try 
		{
			orderedPlayers = CollectionsUtilities.newListFromIterable ( match.getPlayers () ) ;
			CollectionsUtilities.listMesh ( orderedPlayers );
			playersMapOrder = new HashMap < Player , Integer > ( orderedPlayers.size () ) ;
			i = 0 ;
			for ( Player p : orderedPlayers )
			{
				playersMapOrder.put ( p , i ) ;
				i ++ ;
			}
			match.setPlayerOrder ( playersMapOrder ) ;
		} 
		catch ( WrongStateMethodCallException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException (e);
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
		final Color[] colors = { Color.RED , Color.BLUE , Color.GREEN , Color.YELLOW } ;
		Sheperd [] sheperds ; 
		byte numberOfSheperdsPerPlayer ;
		byte sheperdIndex ;
		byte colorIndex ;
		if ( match.getNumberOfPlayers() == 2 )
			numberOfSheperdsPerPlayer = 2 ;
		else
			numberOfSheperdsPerPlayer = 1 ;
		colorIndex = 0 ;
		for ( Player p : match.getPlayers () )
		{
			sheperds = new Sheperd [ numberOfSheperdsPerPlayer ] ;
			for ( sheperdIndex = 0 ; sheperdIndex < numberOfSheperdsPerPlayer ; sheperdIndex ++ )
			{	sheperds [ sheperdIndex ] = new Sheperd ( "" , colors [ colorIndex ] , p ) ;
				colorIndex ++ ;
				if ( colorIndex == colors.length )
					colorIndex = 0 ;
			}
			try 
			{
				p.initializeSheperds ( sheperds ) ;
			}
			catch (WriteOncePropertyAlreadSetException e) {
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
		}
	}
		
	// fare cambiare età agli agnelli !
	/**
	 * This methods implements the core phase of the Game, the time when every player
	 * makes his moves.
	 * It is implemented as a cycle, which, until the Game is finished asks every player
	 * to do his moves, providing him the tools to do this. 
	 */
	private void turnationPhase () 
	{
		Sheperd choosenSheperd ;
		MoveFactory moveFactory ;
		BlackSheep blackSheep ;
		Wolf wolf ;
		byte moveIndex ;
		boolean gamePlaying ;
		blackSheep = findBlackSheep () ;
		wolf = findWolf () ;
		gamePlaying = true ;
		while ( gamePlaying )
		{
			turnNumber ++ ;
			try 
			{
				blackSheep.escape () ;
			}
			catch ( CharacterDoesntMoveException e1 ) 
			{
				// if you want communicate to users.
			}
			for ( Player currentPlayer : match.getPlayers() )
			{
				if ( match.isInFinalPhase () )
					break ;
				if ( match.getNumberOfPlayers () == 2 )
				{
					choosenSheperd = currentPlayer.chooseSheperdForATurn () ;
					moveFactory = new TwoPlayersMatchMoveFactory ( this , this , choosenSheperd ) ;
				}
				else
					moveFactory = new MoveFactory ( this , this ) ;
				for ( moveIndex = 0 ; moveIndex < NUMBER_OF_MOVES_PER_USER_PER_TURN ; moveIndex ++ )
					try 
					{
						currentPlayer.doMove ( moveFactory , match.getGameMap () ).execute ( match );
					} 
					catch (MoveNotAllowedException e) {
						e.printStackTrace();
					} 
				if ( match.isInFinalPhase () == false && match.getBank().hasAFenceOfThisType ( FenceType.NON_FINAL ) == false )
					try 
					{
						match.enterFinalPhase () ;
					} 
					catch ( AlreadyInFinalPhaseException e ) {}
			}
			marketPhase () ;
			try 
			{
				wolf.escape () ;
			}
			catch (CharacterDoesntMoveException e) 
			{
				// if you want communicate the user of the thing...
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
				match.setMatchState ( MatchState.INITIALIZATION ) ;
				matchStartCommunicationController.notifyFinishAddingPlayers () ;
				System.out.println ( "TIMER FINISHED" ) ;
				cancel () ;
			}
			else
			{
				matchStartCommunicationController.notifyFailStartMatch () ;
			}	
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
	
}
