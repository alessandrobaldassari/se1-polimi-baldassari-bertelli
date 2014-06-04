package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This class models a Player in the Game.
 * It's abstract because some of it's methods are very tightly to the game strategy used.
 * This class is widely used in the whole model of the Game, so also if the implemented methods are not declared final, they should
 * not be overridden, and in the case, the contract ( particularly the raising of exceptions ) should be onered.
 */
public abstract class Player implements Serializable
{

	// ATTRIBUTES
	
	/**
	 * The collection containing all the SellableCard owned by this Player.
	 */
	private final Collection < SellableCard > sellableCards ;
	
	/**
	 * The name of this Player.
	 */
	private final String name ;
	
	/**
	 * The Sheperds owned by this Player. 
	 */
	private Sheperd [] sheperds ; 
	
	/**
	 * The initial Card assigned to this Player at the Game beginning 
	 */
	private Card initialCard ;
	
	/**
	 * The number of money owned by this Player.
	 * This property ( obviously may change over time ).
	 * A value less then zero is not permitted; this rule is realized controls set in the methods that act upon this property.
	 */
	private Integer money ;
	
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
	 * @throws IllegalArgumentException if the name parameter is null.
	 */
	public Player ( String name ) 
	{
		if ( name != null )
		{
			this.name = name ;
			sheperds = null ;
			sellableCards = new LinkedList < SellableCard > () ;
			initialCard = null ;
			money = null ;
			suspended = false ;
		}
		else 
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This methods is called by the system in the Market phase of the Game.
	 * Here, the Player, has the opportunity to set the selling state and price of his SellableCards, if he wants
	 * to sell some of his SellableCards to other Players.
	 * It's important for subclasses to consider that this Class is stateful w.r.t. SellableCards; so if for example a Player
	 * set a sellable for a given price at the turn t, and this Card is not sold at the same turn t, if the Player does not 
	 * want to sell this Card anymore at the turn t+1, he has to explicitly make it not sellable ( or modify its price if he wants ), 
	 * otherwise the system will consider the selling state the same as the turn t.
	 */
	public abstract void chooseCardsEligibleForSelling () ;
	
	/**
	 * Getter methods for the sellableCards property.
	 * 
	 * @return the sellableCards property.
	 */
	public Collection < SellableCard > getSellableCards () 
	{
		return sellableCards ;
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
	 * Partial Setter method for the sheperds property.
	 * If the property has not been set yet, and the parameter is not null, it
	 * sets the property to the parameter value, else throws some exceptions.
	 * 
	 * @param sheperds the value for the sheperds property.
	 * @throws IllegalArgumentException if the parameter is null.
	 * @throws SheperdsPropertyAlreadySetException if the property is already not null.
	 */
	public void initializeSheperds ( Sheperd [] sheperds ) throws WriteOncePropertyAlreadSetException
	{
		if ( this.sheperds == null )
		{
			if ( sheperds != null )
				this.sheperds = sheperds ;
			else
				throw new IllegalArgumentException () ;
		}
		else
			throw new WriteOncePropertyAlreadSetException ( "SHEPERDS" ) ;
	} 
	
	/**
	 * This method returns the Sheperd that a Player chooses, between his ones, to 
	 * play in a given turn.
	 * By business rule, this method will be asked only if the Match where this 
	 * Player is is a two-players Match.
	 * 
	 * @return the Sheperd a this Player chooses to play in a given turn.
	 */
	public abstract Sheperd chooseSheperdForATurn () ;
	
	/**
	 * Accessor method useful for subclasses that want to know all the Sheperds.
	 * 
	 * @return an Iterable containing all the Sheperds that this Player has.
	 */
	public Iterable < Sheperd > getSheperds () 
	{
		Iterable < Sheperd > res ;
		res = CollectionsUtilities.newIterableFromArray ( sheperds ) ;
		return res ;
	}
	
	/**
	 * Setter method for the initialCard property 
	 * 
	 * @param initialCard the value for the initialCard property.
	 * @throws IllegalArgumentException if the parameter is null.
	 * @throws WriteOncePropertyAlreadSetException if the property has been already set.
	 */
	public void setInitialCard ( Card initialCard ) throws WriteOncePropertyAlreadSetException 
	{
		if ( this.initialCard == null )
		{
			if ( initialCard != null )
				this.initialCard = initialCard ;
			else
				throw new IllegalArgumentException () ;
		}
		else
			throw new WriteOncePropertyAlreadSetException ( "INITIAL_CARD" ) ;
	}
	
	/**
	 * Getter method for the initialCard property.
	 * 
	 * @return the initialCard property.
	 */
	public Card getInitialCard () 
	{
		return initialCard ;
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
			if ( money == null )
				money = amountToReceive ;
			else
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
		return MathUtilities.launchDice () ;
	}
		
	/**
	 * AS THE SUPER'S ONE.
	 * Two Player objects are considered equals if, and only if, their two name properties are equal.
	 * This definition is valid also for all subclasses of Player, so this method is marked as final. 
	 */
	@Override
	public final boolean equals ( Object obj ) 
	{
		Player otherPlayer ;
		boolean res ;
		if ( obj instanceof Player )
		{
			otherPlayer = ( Player ) obj ;
			if ( name == otherPlayer.getName () )
				res = true ;
			else
				res = false ;
		}
		else
			res = false ;
		return res ;
	}
	
	/**
	 * Method called by the System during the Market phase of a Turn;
	 * this Player has to decide which Card, included in the parameter Iterable, he 
	 * wants to buy, and return it.
	 * If the value returned is null, it is considered as a signal for the caller
	 * that this Player does not want to buy any other card; else this method will 
	 * be called again in order for this Player to buy more than one Card.
	 * 
	 * @param src the List of Cards this Player can potentially buy.
	 * @return a SellableCard object contained in the parameter indicating a Card this Player
	 *         wants to buy, or null if this Player does not want to buy any Card.
	 */
	public abstract SellableCard chooseCardToBuy ( Iterable < SellableCard > src ) ;
	
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
	
	/**
	 * This method is called by the System during the initialization phase to ask
	 * this Player which Color to assign to one of his Sheperd.
	 * The color has to be in the availableColors Iterable.
	 * 
	 * @param availableColors the Iterable containing the colors available for choosing.
	 * @return the choosen color that has to be in the availableColors Iterable.
	 */
	public abstract NamedColor getColorForSheperd ( Iterable < NamedColor > availableColors ) ;
	
	/**
	 * This method is called by the System during the initialization phase to ask 
	 * this Player where initially Place one of his Sheperds.
	 * 
	 * @param availableRoads the Roads where the Player can place one of his Sheperds.
	 * @return the choosen Road.
	 */
	public abstract Road chooseInitialRegionForASheperd ( Iterable < Road > availableRoads ) ;
	
	/**
	 * This is a generic method the System can call to notify a Player of something that
	 * happend but is not a strictly workflow event.
	 * An example of such type of situation may be when a Player goes wrong in an action;
	 * the system may notify him of the error using this method before making the 
	 * consequent actions ( repeating the action, skip some actions and so on ). 
	 * 
	 * @param message the message the System wants to communicate to this Player.
	 */
	public abstract void genericNotification ( String message ) ;
	
	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This exception class model the situation where a Player has too few money to do an action ( tipically buy something ).
	 */
	public class TooFewMoneyException extends Exception 
	{
		
		/***/
		private TooFewMoneyException () {}
		
	}
	
}
