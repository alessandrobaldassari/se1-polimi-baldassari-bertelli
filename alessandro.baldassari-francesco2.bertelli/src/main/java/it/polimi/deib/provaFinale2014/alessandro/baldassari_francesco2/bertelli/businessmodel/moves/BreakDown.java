package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.util.Collection;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This class represents the BreakDown move: a Sheperd wants to kill an Animal in a 
 * Region, and has to pay the nearest other Sheperd for their silence. 
 */
public class BreakDown extends GameMove
{
	
	// ATTRIBUTES
	
	/**
	 * The Sheperd who wants to do this BreakDown. 
	 */
	private Sheperd breaker ;
	
	/**
	 * The Animal that will be broken down if this process will go well. 
	 */
	private Animal animalToBreak ;
	
	// METHODS
	
	/**
	 * @param breaker the Sheperd who wants to do this BreakDown action.
	 * @param animalToBreak the Animal that will be broken down if this process
	 *        will go well.
	 * @throws CannotDoThisMoveException if the breaker sheperd is not near the Region where the animalToBreak
	 *         is located.
	 * @throws IllegalArgumentException if the breaker or the animalToBreak parameter
	 *         is null. 
	 */
	public BreakDown ( Sheperd breaker , Animal animalToBreak ) throws MoveNotAllowedException  
	{
		if ( breaker != null && animalToBreak != null )
			if ( breaker.getPosition ().getFirstBorderRegion ().equals ( animalToBreak.getPosition() ) || breaker.getPosition ().getSecondBorderRegion ().equals ( animalToBreak.getPosition () ) )
				if ( ! ( animalToBreak instanceof BlackSheep ) )
				{
					this.breaker = breaker ;
					this.animalToBreak = animalToBreak ;
				}
				else
					throw new MoveNotAllowedException ( "BREAK_DOWN : CAN NOT KILL BLACK SHEEP." ) ;
			else
				throw new MoveNotAllowedException ( "BREAK_DOWN : SHEPERD NOT IN CORRECT POSITION TO BREAK THIS ANIMAL." ) ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * The effective algorithm method for this move.
	 * It first retrieve the Players adjacent to the breaker one, then ask all of them
	 * to know if they have to right to be payed, then effectively pay them.
	 * An exception is raised if the breaker Player has not enough money to pay all
	 * the selected Players.
	 * 
	 * @param match the Match on which the action is performed.
	 * @throws WorkflowException 
	 * @throws MotNotAllowedException if the breaker Player has not enough money 
	 *         to pay all the selected Players
	 *         
	 * @PRECONDITIONS 
	 * 	1. match != null
	 * @POSTCONDITIONS 
	 *  1. breaker.money ( out ) = breaker.money ( in ) - AMOUNT_TO_PAY_FOR_SILENCE * # adjacent_players
	 *  2. forall ( adjacent player p : p ( out).money = p ( in ).money + AMOUNT_TO_PAY_FOR_SILENCE.
	 *  3. animalToBreak.position == null.
	 *  4. position ( in ) of animalToBreaker NOT CONTAINS animalToBreak
	 * @EXCEPTIONS_POSTCONDITIONS :
	 *  1. TOO_FEW_MONEY_EXCEPTION_RAISED && breaker.money < # adjacent players * AMOUNT_TO_PAY_FOR_SILENCE.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException, WorkflowException 
	{
		Collection < Player > adjacentPlayers ;
		if ( match != null )
		{
			// trovo coloro che potrei dover pagare per il silenzio
			adjacentPlayers = MapUtilities.retrieveAdjacentPlayers ( breaker.getPosition() ) ;
			// i potenziali testimoni lanciano il dado
			adjacentPlayersDiceLaunching ( adjacentPlayers ) ;
			try 
			{
				// pago per il silenzio
				breaker.getOwner().pay ( adjacentPlayers.size () * GameConstants.AMOUNT_TO_PAY_FOR_SILENCE ) ;
				// ogni testimone precedentemente selezionato riceve la somma
				for ( Player player : adjacentPlayers )
					player.receiveMoney ( GameConstants.AMOUNT_TO_PAY_FOR_SILENCE ) ;
				// effettivo abbattimento dell'animale
				animalToBreak.getPosition ().removeAnimal ( animalToBreak ) ;
				animalToBreak.moveTo ( null ) ;
			} 
			catch ( TooFewMoneyException e ) 
			{
				throw new MoveNotAllowedException ( PresentationMessages.NOT_ENOUGH_MONEY_MESSAGE ) ;
			} 
			catch ( WrongStateMethodCallException e ) 
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
			}
		}
		else
			throw new IllegalArgumentException ( "BREAK_DOWN - EXECUTE : The match parameter can not be null." ) ;
	}

	/**
	 * Ask every adjacent Player ( adjacent to the Breaker ) to launch a dice to see
	 * if they have the right to be payed.
	 * 
	 * @param adjacentPlayers the Players that have to be asked to see if they have
	 *        the right to be payed.
	 */
	private void adjacentPlayersDiceLaunching ( Collection < Player > adjacentPlayers ) 
	{
		Collection < Player > winners ;
		winners = new LinkedList < Player > () ;
		for ( Player player : adjacentPlayers )
			if ( ! player.isSuspended() && player.launchDice () >= GameConstants.MINIMUM_POINTS_TO_BE_PAYED )
				winners.add(player);
		adjacentPlayers.clear();
		for ( Player p : winners )
			adjacentPlayers.add(p);
	}
	
}
