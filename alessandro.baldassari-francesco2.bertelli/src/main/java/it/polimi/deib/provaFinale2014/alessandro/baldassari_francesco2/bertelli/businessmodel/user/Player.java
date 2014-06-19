package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Suspendable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
 * This class models a Player in the Game.
 * It's abstract because some of it's methods are very tightly to the game strategy used.
 * This class is widely used in the whole model of the Game, so also if the implemented methods are not declared final, they should
 * not be overridden, and in the case, the contract ( particularly the raising of exceptions ) should be onered.
 */
public abstract class Player extends WithReflectionAbstractObservable < PlayerObserver > implements Serializable , Suspendable
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
		super () ;
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
	
	/***/
	public void addCard ( SellableCard toAdd ) 
	{
		sellableCards.add(toAdd) ;
		try 
		{
			notifyObservers ( "onCardAdded" , toAdd.getRegionType() ) ;
		}
		catch (MethodInvocationException e) 
		{
			e.printStackTrace();
		}
	}
	
	/***/
	public void removeCard ( SellableCard toRemove ) 
	{
		sellableCards.remove ( toRemove ) ;
		try 
		{
			notifyObservers ( "onCardRemoved" , toRemove.getRegionType() ) ;
		}
		catch (MethodInvocationException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean hasCard ( SellableCard s )
	{
		return sellableCards.contains ( s ) ;
	}
	
	/**
	 * Getter methods for the sellableCards property.
	 * 
	 * @return the sellableCards property.
	 */
	public Iterable < SellableCard > getSellableCards () 
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
	 * Accessor method useful for subclasses that want to know all the Sheperds.
	 * 
	 * @return an Iterable containing all the Sheperds that this Player has.
	 */
	public Iterable < Sheperd > getSheperds () 
	{
		Iterable < Sheperd > res ;
		if ( sheperds != null )
			res = CollectionsUtilities.newIterableFromArray ( sheperds ) ;
		else
			res = null ;
		return res ;
	}
	
	/**
	 * Remove some money from this Player.
	 * Because this method essentially models a payment of this Player, the parameter must be > 0.
	 * Technically it decrements the money property by the value indicated by the parameter.
	 * 
	 * @param amountToPay the amount of money to remove
	 * @throws IllegalArgumentException if the parameter is < 0
	 * @throws TooFewMoneyException if this Player has not enough money to pay, technically if the parameter is >= than the money attribyte.
	 * @throws WrongStateMethodCallException 
	 */
	public int pay ( int amountToPay ) throws TooFewMoneyException, WrongStateMethodCallException   
	{
		if ( amountToPay >= 0 )
		{
			if ( money != null )
				if ( money >= amountToPay )
				{
					money = money - amountToPay ;
					try 
					{
						notifyObservers ( "onPay" , amountToPay , money );
					}
					catch (MethodInvocationException e) 
					{
						e.printStackTrace();
					}
				}
				else
					throw new TooFewMoneyException () ;
			else
				throw new WrongStateMethodCallException () ;
		}
		else
			throw new IllegalArgumentException () ;
		return amountToPay ;
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
		{
			if ( money == null )
				money = amountToReceive ;
			else
				money = money + amountToReceive ;
			try 
			{
				notifyObservers ( "onPay" , amountToReceive , money );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		else 
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the money property.
	 * 
	 * @return the money owned by this Player.
	 * @throws WrongStateMethodCallException 
	 */
	public int getMoney () throws WrongStateMethodCallException 
	{
		if ( money != null )
			return money ;
		else
			throw new WrongStateMethodCallException () ;
	}
	
	/**
	 * Suspend this Player from the Game.
	 * Technically set the suspended property to true.
	 */
	@Override
	public void suspend () 
	{
		suspended = true ;
	}
	
	/**
	 * Resume this Player and re-admits him to the Game.
	 * Technically set the suspended property to false.
	 */
	@Override
	public void resume () 
	{
		suspended = false ;
	}
	
	/**
	 * Getter for the suspended property
	 * 
	 * @return true if this Player has been suspended, false else.
	 */
	@Override
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
			{
				this.initialCard = initialCard ;
				try 
				{
					notifyObservers ( "onCardAdded" , initialCard.getRegionType() ) ;
				}
				catch (MethodInvocationException e) 
				{
					e.printStackTrace();
				}
			}
			else
				throw new IllegalArgumentException () ;
		}
		else
			throw new WriteOncePropertyAlreadSetException ( "INITIAL_CARD" ) ;
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
		int i ;
		if ( this.sheperds == null )
		{
			if ( sheperds != null )
			{
				this.sheperds = new Sheperd [ sheperds.length ] ;
				for ( i = 0 ; i < sheperds.length ; i ++ )
					this.sheperds [ i ] = sheperds [ i ] ;
			}
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
	 * @param playerSheperds the Sheperds among which a Player may choose.
	 * @return the Sheperd a this Player chooses to play in a given turn.
	 * @throws TimeoutException 
	 */
	public abstract Sheperd chooseSheperdForATurn ( final Iterable < Sheperd > playersSheperds ) throws TimeoutException ;
	
	/**
	 * This methods is called by the system in the Market phase of the Game.
	 * Here, the Player, has the opportunity to set the selling state and price of his SellableCards ; if this Player
	 * has some SellableCards chooseCardsEligibleForSellingIfThereAreSellableCards () will be called, and
	 * subclasses will have a choice to choose some eligible cards.
	 * It's important for subclasses to consider that this Class is stateful w.r.t. SellableCards; so if for example a Player
	 * set a sellable for a given price at the turn t, and this Card is not sold at the same turn t, if the Player does not 
	 * want to sell this Card anymore at the turn t+1, he has to explicitly make it not sellable ( or modify its price if he wants ), 
	 * otherwise the system will consider the selling state the same as the turn t.
	 */
	public void chooseCardsEligibleForSelling () throws TimeoutException 
	{
		if ( sellableCards.size () > 0 )
			chooseCardsEligibleForSellingIfThereAreSellableCards () ;
	}
	
	protected abstract void chooseCardsEligibleForSellingIfThereAreSellableCards () throws TimeoutException ;
	
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
	 *         wants to buy, or an empty iterable to indicate that this User does not want to buy any card
	 * @throws TimeoutException 
	 */
	public abstract Iterable < SellableCard > chooseCardToBuy ( Iterable < SellableCard > src ) throws TimeoutException ;
	
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
	 * @throws TimeoutException 
	 */
	public abstract MoveSelection doMove ( final MoveSelector moveSelector , final GameMap gameMap ) throws TimeoutException ; 
		
	/**
	 * This method is called by the System during the initialization phase to ask
	 * this Player which Color to assign to one of his Sheperd.
	 * The color has to be in the availableColors Iterable.
	 * 
	 * @param availableColors the Iterable containing the colors available for choosing.
	 * @return the choosen color that has to be in the availableColors Iterable.
	 * @throws TimeoutException 
	 */
	public abstract NamedColor getColorForSheperd ( final Iterable < NamedColor > availableColors ) throws TimeoutException ;	
	/**
	 * This method is called by the System during the initialization phase to ask 
	 * this Player where initially Place one of his Sheperds.
	 * 
	 * @param availableRoads the Roads where the Player can place one of his Sheperds.
	 * @return the choosen Road.
	 * @throws TimeoutException 
	 */
	public abstract Road chooseInitialRoadForASheperd ( final Iterable < Road > availableRoads ) throws TimeoutException ;

	
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
	
	/**
	 * Method called by the System to notify the User that the Match is finishing.
	 * If subclasses uses Closeable resources ( connections, threads and so on ) this is the place
	 * when do it.
	 * No other method will be called by the System on this Player object after this.
	 * 
	 * @param cause the cause of the match finish in the form of a User friendly message.
	 */
	public abstract void matchEndNotification ( String cause ) ;
	
	public enum PlayerState 
	{
		
		NOT_CONNECTED ,
		
		PLAYING ,
		
		SUSPENDED ,
		
		DISCONNECTED 
		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String toString () 
	{
		String res ;
		res = "Player." + Utilities.CARRIAGE_RETURN ;
		return res ;
	}
	
	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This exception class model the situation where a Player has too few money to do an action ( tipically buy something ).
	 */
	public class TooFewMoneyException extends Exception 
	{
		
		/***/
		public TooFewMoneyException () {}
		
	}
	
}
