package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

/**
 * This class models the Mate move.
 * A Sheperd wants a Mate process to happen in a given Region. 
 */
public class Mate extends GameMove
{

	/**
	 * The number of turns after a Lamb should become a Ram or a Sheep. 
	 */
	public static final int NUMBER_OF_TURN_AFTER_THOSE_A_LAMB_EVOLVE = 2 ;
	
	/**
	 * The Sheperd who wants to perform this action. 
	 */
	private Sheperd theOneWhoWantsTheMate ;
	
	/**
	 * The Region where this action has to take place.
	 */
	private Region whereMate ;
	
	/***/
	private TurnNumberClock clockSource ;
	
	/***/
	private LambEvolver lambEvolver ;
	
	/**
	 * @param clockSource a component that will supply this Mate object information about the turn 
	 *        changing, thing needed for eventually generated lambs to grow up.
	 * @param theOneWhoWantsTheMate the Sheperd who wants to perform this action. 
	 * @param whereMate the Region where this action has to take place. 
	 * @throws IllegalArgumentException if the theOneWhoWantsTheMate or the whereMate parameters
	 *         is null.
	 */
	Mate ( TurnNumberClock clockSource , LambEvolver lambEvolver , Sheperd theOneWhoWantsTheMate , Region whereMate ) 
	{
		if ( clockSource != null && lambEvolver != null && theOneWhoWantsTheMate != null && whereMate != null ) {
			this.clockSource = clockSource ;
			this.lambEvolver = lambEvolver ;
			this.theOneWhoWantsTheMate = theOneWhoWantsTheMate ;
			this.whereMate = whereMate ;
		} 
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * The effective-algorithm method for this move.
	 * It verifies if all the conditions for this mate to happen are verified.
	 * If so, it tries the Mate; if it go well, it adds the born Lamb to the Region
	 * where the process took place.
	 * 
	 * @param match the Match on which the action is performed.
	 * @throws MoveNotAllowedException if something goes wrong.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException 
	{
		Runnable lambGrowerLookerRunnable ;
		Executor runnableExec ;
		Road sheperdRoad ;
		Lamb lamb ;
		List < AdultOvine > adultOvines ;
		AdultOvine ram ;
		AdultOvine sheep ;
		sheperdRoad = theOneWhoWantsTheMate.getPosition () ;
		if ( sheperdRoad.getFirstBorderRegion ().equals( whereMate ) || sheperdRoad.getSecondBorderRegion ().equals ( whereMate ) )
		{
			adultOvines = extractAdultOvines ( whereMate.getContainedAnimals () ) ;
			ram = lookForAnOvine ( adultOvines , AdultOvineType.RAM ) ;
			if ( ram != null )
			{
				sheep = lookForAnOvine ( adultOvines , AdultOvineType.SHEEP ) ;
				if ( sheep != null )
				{
					try 
					{
						lamb = sheep.mate ( ram ) ;
						whereMate.addAnimal ( lamb ) ;
						lamb.moveTo ( whereMate );
						runnableExec = Executors.newSingleThreadExecutor () ;
						lambGrowerLookerRunnable = new LambGrowerLookerRunnable ( clockSource , lamb , lambEvolver ) ;
						runnableExec.execute ( lambGrowerLookerRunnable ) ;
					} 
					catch ( CanNotMateWithHimException e ) 
					{
						e.printStackTrace();
						throw new RuntimeException () ;
					} 
					catch ( MateNotSuccesfullException e ) 
					{
						e.printStackTrace () ;
					}
				}
				else
					throw new RuntimeException () ;
			}
			else
				throw new MoveNotAllowedException () ;
		}
		else
			throw new MoveNotAllowedException () ;
	}

	/**
	 * Extract from the src Collection the Animals which are AdultOvines
	 * 
	 * @param src the Collection where to perform the search.
	 * @return a List containing all of the AdultOvines which are in the src parameter.
	 */
	private List < AdultOvine > extractAdultOvines ( Iterable < Animal > src ) 
	{
		List < AdultOvine > res ;
		res = new LinkedList < AdultOvine > () ;
		for ( Animal animal : src )
			if ( animal instanceof AdultOvine )
				res.add ( ( AdultOvine ) animal ) ;
		return res ;
	}
	
	/**
	 * Search for an AdultOvine of the parameter specified type in the src List, and if 
	 * it exists, return it.
	 * 
	 * @param src the List where to perform the search.
	 * @param type the type the returned AdultOvine must be.
	 * @return the target AdultOvine, null if it can not exists in the src List.
	 */
	private AdultOvine lookForAnOvine ( List < AdultOvine > src , AdultOvineType type ) 
	{
		AdultOvine res ;
		int i ;
		res = null ;
		i = 0 ;
		while ( i < src.size() && src.get ( i ).getType () != type ) 
		i ++ ;
		if ( i < src.size () )
			res = src.get ( i ) ;
		return res ;
	}

	/***/
	private class LambGrowerLookerRunnable implements Runnable 
	{
		
		/***/
		private TurnNumberClock turnNumberClock ;
		
		/***/
		private Lamb lambToEvolve ;
		
		/***/
		private LambEvolver lambEvolver ;
		
		private int initTurn ;
		
		/***/
		LambGrowerLookerRunnable ( TurnNumberClock turnNumberClock , Lamb lambToEvolve , LambEvolver lambEvolver ) 
		{
			if ( turnNumberClock != null && lambToEvolve != null && lambEvolver != null )
			{
				try 
				{
					this.turnNumberClock = turnNumberClock ;
					this.lambToEvolve = lambToEvolve ;
					this.lambEvolver = lambEvolver ;
					initTurn = turnNumberClock.getTurnNumber () ;
				} 
				catch (WrongMatchStateMethodCallException e) 
				{
					e.printStackTrace();
					throw new RuntimeException () ;
				}
			}
			else
				throw new IllegalArgumentException () ;
		}
		
		@Override
		public void run () 
		{
			int currentTurn ;
			boolean finished = false ;
			try 
			{
				while ( finished == false )
				{
					currentTurn = turnNumberClock.getTurnNumber () ;
					if ( currentTurn - initTurn == NUMBER_OF_TURN_AFTER_THOSE_A_LAMB_EVOLVE )
					{
						lambEvolver.evolve ( lambToEvolve ) ;
						finished = true ;
					}
				}
			}
			catch ( WrongMatchStateMethodCallException e) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
		}
	}
}
