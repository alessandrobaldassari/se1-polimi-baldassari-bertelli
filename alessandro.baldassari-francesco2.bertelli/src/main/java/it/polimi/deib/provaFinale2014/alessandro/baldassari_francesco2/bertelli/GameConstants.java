package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A class that contains constants data of the game ( a.k.a. business rules ). 
 */
public final class GameConstants 
{

	/**
	 * The number of Region in a Map. 
	 */
	public static final int NUMBER_OF_REGIONS = 19 ;
	
	/**
	 * The number of Road in a Map 
	 */
	public static final int NUMBER_OF_ROADS = 42 ;
	
	/**
	 * The initial money amount, total. 
	 * It's a business rule. 
	 */
	public static final int INITIAL_MONEY_RESERVE = 80 ;
	
	/**
	 * The total number of non final fences.
	 * It's a business rule. 
	 */
	public static final int NON_FINAL_FENCE_NUMBER = 20 ;
	
	/**
	 * The number of final fences initially in the Bank.
	 * It's a business rule. 
	 */
	public static final int FINAL_FENCE_NUMBER = 12 ;
	
	/**
	 * The maximum number of Player for a Match. 
	 */
	public static final int MAX_NUMBER_OF_PLAYERS = 4;

	/**
	 * The number of moves a Player can do each turn.
	 * It's a business rule. 
	 */
	public static final int NUMBER_OF_MOVES_PER_USER_PER_TURN = 3 ;
	
	/***/
	public static final int NUMBER_OF_REGION_TYPE = 7 ;
	
	/**
	 * Minimum value a dice launch must return in order for a Sheperd to be payed
	 * during this BreakDown process.
	 * It's a business rule.
	 */
	public static final int MINIMUM_POINTS_TO_BE_PAYED = 5 ;
	
	/**
	 * The amount of money a Sheperd will be payed for his silence during this
	 * BreakDown process.
	 * It's a business rule.
	 */
	public static final int AMOUNT_TO_PAY_FOR_SILENCE = 2 ;
	
	/**
	 * The number of turns after a Lamb should become a Ram or a Sheep. 
	 */
	public static final int NUMBER_OF_TURN_AFTER_THOSE_A_LAMB_EVOLVE = 2 ;
	
	/**
	 * The number of non initial Cards to be created for each Region. 
	 * It's a business rule.
	 */
	public static final int NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE = 5 ;
	
	/**
	 * The total number of initial cards.
	 * It's a business rule. 
	 */
	public static final int NUMBER_OF_INITIAL_CARD = 6 ;
	
	/**
	 * The amount of value the Sheperd has to pay if the Road where he wants to go is 
	 * not adjacent to the Road where he is now. 
	 */
	public static final int MONEY_TO_PAY_IF_ROADS_NON_ADJACENT = 1 ;
	
	/***/
	private GameConstants () {}
	
	/**
	 * Generates and returns the an Iterable containing the colors the Sheperds may have.
	 * 
	 * @return an iterable containing the colors the Sheperds may have.
	 */
	public static final Iterable < NamedColor > getSheperdColors () 
	{
		Collection < NamedColor > colors ;
		colors = new LinkedList < NamedColor > () ;
		colors.add ( new NamedColor ( Color.RED.getRed() , Color.RED.getGreen() , Color.RED.getBlue() , "RED" ) ) ;
		colors.add ( new NamedColor ( Color.BLUE.getRed() , Color.BLUE.getGreen() , Color.BLUE.getBlue() , "BLUE" ) ) ;
		colors.add ( new NamedColor ( Color.GREEN.getRed() , Color.GREEN.getGreen() , Color.GREEN.getBlue() , "GREEN" ) ) ;
		colors.add ( new NamedColor ( Color.YELLOW.getRed () , Color.YELLOW.getGreen () , Color.YELLOW.getBlue () , "YELLOW" ) ) ;
		return colors ;
	}
	
}
