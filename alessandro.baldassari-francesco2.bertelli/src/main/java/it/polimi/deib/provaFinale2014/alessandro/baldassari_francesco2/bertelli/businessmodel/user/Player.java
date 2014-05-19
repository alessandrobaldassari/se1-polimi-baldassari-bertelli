package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class models a Player in the Game.
 * It's abstract because some of it's methods are very tightly to the game strategy used.
 * This class is widely used in the whole model of the Game, so also if the implemented methods are not declared final, they should
 * not be overridden, and in the case, the contract ( particularly the raising of exceptions ) should be onered.
 */
public abstract class Player 
{

	// ATTRIBUTES
	
	/**
	 * The collection containing all the Cards owned by this Player.
	 */
	private final Collection < Card > cards ;
	
	/**
	 * The name of this Player.
	 */
	private final String name ;
	
	/**
	 * The number of money owned by this Player.
	 * This property ( obviously may change over time ).
	 * A value less then zero is not permitted; this rule is realized controls set in the methods that act upon this property.
	 */
	private int money ;
	
	/**
	 * A flag which is false if this Player is normally in the Game Workflow, false if he has been suspended.
	 * A value of false, may descend by two cases:
	 * 1. This player went offline.
	 * 2. The whole game is suspended.
	 * Anyway, from the point of view of this class there is no difference if the reason why the value is false is ( 1 ) or ( 2 ).
	 */
	private boolean suspended ;
	
	// METHODS
	
	/**
	 * @param name the name of this Player.
	 * @param money the initial amount of money of this Player.
	 * @throws IllegalArgumentException if the name parameter is null or if the money parameter is < 0.
	 */
	public Player ( String name , int money ) 
	{
		if ( name != null && money > 0 )
		{
			this.name = name ;
			this.money = money ;
			cards = new LinkedList < Card > () ;
			suspended = false ;
		}
		else 
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This methods is called by the system in the Market phase of the Game.
	 * Here, the Player, has the opportunity to set the selling state and price of his Cards, if he wants
	 * to sell some of his Cards to other Players.
	 * It's important for subclasses to consider that this Class is stateful w.r.t. Cards; so if for example a Player
	 * set a sellable for a given price at the turn t, and this Card is not sold at the same turn t, if the Player does not 
	 * want to sell this Card anymore at the turn t+1, he has to explicitly make it not sellable ( or modify its price if he wants ), 
	 * otherwise the system will consider the selling state the same as the turn t.
	 */
	public abstract void chooseCardsEligibleForSelling () ;
	
	/**
	 * Getter for the Cards property of this Player.
	 * It returns the Collection instantiated in this Player object, not a copy, so every modification made on
	 * the returned value will be visible here.
	 * 
	 * @return a Collection containing the Cards owned by this Player.
	 */
	public Collection < Card > getCards () 
	{
		return cards ;
	}
	
	/**
	 * Getter for the name property of this Player.
	 * 
	 * @return the name of this Player.
	 */
	public String getName () 
	{
		return name ;
	}
	
	/**
	 * Add some money to this Player.
	 * Because this method essentially models a payment to this Player, the parameter must be > 0.
	 * Technically it increments the money property by the value indicated by the parameter.
	 * 
	 * @param amountToReceive the amount of money to add.
	 * @throws IllegalArgumentException if the amountToAdd parameter is < 0
	 */
	public void receiveMoney ( int amountToReceive ) 
	{
		if ( amountToReceive >= 0 )
			money = money + amountToReceive ;
		else 
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Remove some money from this Player.
	 * Because this method essentially models a payment of this Player, the parameter must be > 0.
	 * Technically it decrements the money property by the value indicated by the parameter.
	 * 
	 * @param amountToPay the amount of money to remove
	 * @throws IllegalArgumentException if the parameter is < 0
	 * @throws TooFewMoneyException if this Player has not enough money to pay, technically if the parameter is >= than the money attribyte.
	 */
	public int pay ( int amountToPay ) throws TooFewMoneyException  
	{
		if ( amountToPay > 0 )
		{
			if ( money >= amountToPay )
				money = money - amountToPay ;
			else
				throw new TooFewMoneyException () ;
		}
		else
			throw new IllegalArgumentException () ;
		return amountToPay ;
	}
	
	/**
	 * Getter for the money property.
	 * 
	 * @return the money owned by this Player.
	 */
	public int getMoney () 
	{
		return money ;
	}
	
	/**
	 * Suspend this Player from the Game.
	 * Technically set the suspended property to true.
	 */
	public void suspend () 
	{
		suspended = true ;
	}
	
	/**
	 * Resume this Player and re-admits him to the Game.
	 * Technically set the suspended property to false.
	 */
	public void resume () 
	{
		suspended = false ;
	}
	
	/**
	 * Getter for the suspended property
	 * 
	 * @return true if this Player has been suspended, false else.
	 */
	public boolean isSuspended () 
	{
		return suspended ;
	}

	/**
	 * Simulation the launch of a dice by this player.
	 * This implementation is a Random ones.
	 * 
	 * @return the result of the simulated dice's launch, a number n : 1 <= n <= 6.
	 */
	public int launchDice () 
	{
		Random random ;
		int res ;
		random = new Random () ;
		res = random.nextInt ( 6 ) + 1 ;
		return res ;
	}
		
	/**
	 * This is the central method for the Player class ( and its subclasses too ) because it models the most important 
	 * behavior of a Player : play.
	 * This method will be called by the system every time a Player can ( and must ) do a move ( so 3 times every turn, as the rules say ).
	 * The two parameters are the tools that the Player can use to make his move: MoveFactory object, from where the Player can create
	 * a MoveGame object to return to the System, and a GameMap object, which represents the environment where the Player is, and from where he 
	 * can extracts the objects necessary to fill the move parameters.
	 * 
	 * @param moveFactory the object which the Player use to create GameMove objects.
	 * @param gameMap the map of the Game containing all the Character , Region and so on objects composing the game
	 * @return a GameMove object which represents the move the Player wants to make.
	 */
	public abstract GameMove doMove ( MoveFactory moveFactory , GameMap gameMap ) ;
	
	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This exception class model the situation where a Player has too few money to do an action ( tipically buy something ).
	 */
	public class TooFewMoneyException extends Exception {}
	
}
