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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.MasterServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
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
public class GameController implements Runnable
{
	
	private static int lastMatchIdentifierUID = -1 ;
	
	/**
	 * The Timer value about the time to wait before begin a Match. 
	 */
	private static final long DELAY = 150000;
	
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
	 * A standard Timer object to mange the timer at the beginning of a Match.
	 * It's a business rule. 
	 */
	private Timer timer;
	
	private MasterServer masterServer ;
	
	/***/
	public GameController ( MasterServer masterServer ) 
	{
		this.masterServer = masterServer ;
		timer = new Timer();
	}
	
	// fare cambiare età agli agnelli !
	/**
	 * 
	 */
	public void run () 
	{
		creatingPhase () ;
		while ( match.getMatchState() != MatchState.INITIALIZATION )
			try 
			{	
				wait () ;
			}
			catch ( InterruptedException e ) 
			{
				e.printStackTrace();
			}
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
			timer.schedule ( new WaitingPlayersTimerTask () , DELAY ) ;
		} 
		catch ( SingletonElementAlreadyGeneratedException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
	}
	
	/**
	 * This is the logically second method of the Game Controller lifecycle.
	 * Obviously is not called by this GameController itself; otherwise, other 
	 * components will call it to add new Players.
	 * This component is also responsible for control if the MAXIMUM NUMBER OF PLAYERS
	 * is reached; if so, it sets the match state to the INITIALIZATION state, and the WAIT
	 * FR PLAYERS phase is considered concluded.
	 */
	public void addPlayerAndCheck ( Player newPlayer ) throws WrongStateMethodCallException
	{
		if ( match.getMatchState () == Match.MatchState.WAIT_FOR_PLAYERS )
		{
			match.getPlayers ().add ( newPlayer ) ;
			if ( match.getPlayers ().size () == MAX_NUMBER_OF_PLAYERS ) 
			{
				timer.cancel () ;
				match.setMatchState ( MatchState.INITIALIZATION ) ;
				masterServer.notifyFinishAddingPlayers () ;
			}
		}
		else
			throw new WrongStateMethodCallException ( match.getMatchState () ) ;
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
		Region sheepsburg ;
		Ovine bornOvine ;
		try 
		{
			for ( Region region : match.getGameMap ().getRegions () ) 
			{
				bornOvine = generateOvine () ;
				region.getContainedAnimals().add ( bornOvine ) ;
			}
			sheepsburg = match.getGameMap ().getRegionByType ( RegionType.SHEEPSBURG ).iterator().next() ;
			sheepsburg.getContainedAnimals ().add ( animalsFactory.newBlackSheep () ) ;
			sheepsburg.getContainedAnimals().add ( animalsFactory.newWolf () ) ;
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
		catch ( NoMoreCardOfThisTypeException | WriteOncePropertyAlreadSetException e ) 
		{
			e.printStackTrace();
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
		if ( match.getPlayers ().size () == 2 )
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
		CollectionsUtilities.listMesh ( match.getPlayers() ) ;
	}
	
	private void distributeSheperds () 
	{
		final Color[] colors = { Color.RED , Color.BLUE , Color.GREEN , Color.YELLOW } ;
		Sheperd [] sheperds ; 
		byte numberOfSheperdsPerPlayer ;
		byte sheperdIndex ;
		byte colorIndex ;
		if ( match.getPlayers ().size () == 2 )
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
			try {
				p.initializeSheperds ( sheperds ) ;
			} catch (WriteOncePropertyAlreadSetException e) {
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
		}
	}
		
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
		Player currentPlayer ;
		byte playerIndex ;
		byte moveIndex ;
		boolean gamePlaying ;
		blackSheep = findBlackSheep () ;
		wolf = findWolf () ;
		gamePlaying = true ;
		while ( gamePlaying )
		{
			try 
			{
				blackSheep.escape() ;
			}
			catch ( CharacterDoesntMoveException e1 ) {}
			for ( playerIndex = 0 ; playerIndex < match.getPlayers().size () || ! match.isInFinalPhase () ; playerIndex ++ )
			{
				currentPlayer = match.getPlayers ().get ( playerIndex ) ;
				if ( match.getPlayers ().size () == 2 )
				{
					choosenSheperd = currentPlayer.chooseSheperdForATurn () ;
					moveFactory = new TwoPlayersMatchMoveFactory ( choosenSheperd ) ;
				}
				else
					moveFactory = new MoveFactory () ;
				for ( moveIndex = 0 ; moveIndex < NUMBER_OF_MOVES_PER_USER_PER_TURN ; moveIndex ++ ) 
					currentPlayer.doMove ( moveFactory , match.getGameMap () ) ;
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
			catch (CharacterDoesntMoveException e) {}
			if ( match.isInFinalPhase () )
				gamePlaying = false ;
		}
	}
	
	private void marketPhase () 
	{
		Collection < SellableCard > choosenCards ;
		Collection < SellableCard > sellableCards ;
		Player currentPlayer ;
		SellableCard sellableCard ;
		byte playerIndex ;
		int amount ;
		for ( playerIndex = 0 ; playerIndex < match.getPlayers ().size () ; playerIndex ++ )
			match.getPlayers().get ( playerIndex ).chooseCardsEligibleForSelling () ;
		for ( playerIndex = 0 ; playerIndex < match.getPlayers ().size () ; playerIndex ++ )
		{
			try
			{
				amount = 0 ;
				choosenCards = new LinkedList < SellableCard > () ;
				currentPlayer = match.getPlayers ().get ( playerIndex ) ;
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
			catch ( NotSellableException | SellingPriceNotSetException | TooFewMoneyException n ) {}
		}
	}
	
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
		playerScoresMap = new HashMap<Player, Integer>(match.getPlayers().size());
		regionValuesMap = calculateRegionsValue();
		for(Player p : match.getPlayers())
			playerScoresMap.put(p, calculatePlayerScore(p, regionValuesMap));
		
		
	}
	
	private int calculatePlayerScore(Player player, Map <RegionType, Integer> regionValuesMap){
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
	
	private Map <RegionType, Integer> calculateRegionsValue(){
		Map <RegionType, Integer> regionValuesMap;
		Iterable<Region> regions;
		Iterable<Animal> animals;
		int amount;
		regionValuesMap = new HashMap<RegionType, Integer>(RegionType.values().length -1);
		for(RegionType regionType: RegionType.values())	
			if(regionType != RegionType.SHEEPSBURG)
			{
				amount = 0;
				regions = match.getGameMap().getRegionByType(regionType);
				for(Region r : regions){
					animals = r.getContainedAnimals();
					for(Animal a : animals){
						if(!(a instanceof Wolf)){
							if(a instanceof BlackSheep)
								amount = amount + 2;
							else {
								amount = amount + 1;
							}
						}
					}
						
				}
				regionValuesMap.put(regionType, amount);
			}
		return regionValuesMap;
	}
	
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
	 * This helper method genereta an Ovine choosing it's type ( sex ) with a simple
	 * probabilistic process.
	 * With p = 0.5, a Sheep.
	 * With p = 0.5, a Ram.
	 * 
	 * @return the generated Ovine.
	 */
	private Ovine generateOvine ()  
	{
		final Ovine result ; 
		final double chooseOvineType ;
		chooseOvineType = Math.random () ;
		if ( chooseOvineType < 0.5 )
			result = animalsFactory.newAdultOvine ( "" ,  AdultOvineType.SHEEP ) ;
		else
			result = animalsFactory.newAdultOvine ( "" ,  AdultOvineType.RAM ) ;				
		return result ;
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
			if ( match.getPlayers().size() >= 2 ) 
			{
				match.setMatchState ( MatchState.INITIALIZATION ) ;
				masterServer.notifyFinishAddingPlayers () ;
				cancel () ;
			}
			else
			{
				masterServer.notifyFailure () ;
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
	
	// EXCEPTIONS
	
	/**
	 * This Exceptions models the situation where a Client tries to invoke a Method of a
	 * GameController while the GameController itself is in a state where that method 
	 * can not be called. 
	 */
	public class WrongStateMethodCallException extends Exception 
	{
		
		/**
		 * The state where the System is when this Exception is thrown. 
		 */
		private MatchState actualState ;
		
		/**
		 * @param actualState the state where the System is when this Exception is thrown. 
		 * @throws IllegalArgumentException if the actualState parameter is null.
		 */
		WrongStateMethodCallException ( MatchState actualState ) 
		{
			if ( actualState != null )
				this.actualState = actualState ;
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * Getter method for the actualState property.
		 * 
		 * @return the actualState property.
		 */
		public MatchState getActualState () 
		{
			return actualState ;
		}
		
	}

}
