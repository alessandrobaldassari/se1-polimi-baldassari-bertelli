package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.util.List;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;

/**
 * This class models the Mate move.
 * A Sheperd wants a Mate process to happen in a given Region. 
 */
public class Mate extends GameMove
{
	
	/**
	 * The Sheperd who wants to perform this action. 
	 */
	private Sheperd theOneWhoWantsTheMate ;
	
	/**
	 * The Region where this action has to take place.
	 */
	private Region whereMate ;
	
	/**
	 * a TurnNumberClock object to manage the turn that passes. 
	 */
	private TurnNumberClock clockSource ;
	
	/**
	 * A Lamb evolve to manage the life of the eventually born lambs. 
	 */
	private LambEvolver lambEvolver ;
	
	/**
	 * @param clockSource a component that will supply this Mate object information about the turn 
	 *        changing, thing needed for eventually generated lambs to grow up.
	 * @param theOneWhoWantsTheMate the Sheperd who wants to perform this action. 
	 * @param whereMate the Region where this action has to take place. 
	 * @throws MoveNotAllowedException if the parameters do not meet the business rules.
	 * @throws IllegalArgumentException if the theOneWhoWantsTheMate or the whereMate parameters
	 *         is null.
	 */
	public Mate ( TurnNumberClock clockSource , LambEvolver lambEvolver , Sheperd theOneWhoWantsTheMate , Region whereMate ) throws MoveNotAllowedException 
	{
		if ( clockSource != null && lambEvolver != null && theOneWhoWantsTheMate != null && whereMate != null ) 
			if ( MapUtilities.areAdjacents( theOneWhoWantsTheMate.getPosition () , whereMate ) )
				if ( canMateDueToSexReasons ( whereMate ) )
				{
					this.clockSource = clockSource ;
					this.lambEvolver = lambEvolver ; 
					this.theOneWhoWantsTheMate = theOneWhoWantsTheMate ;
					this.whereMate = whereMate ;
				}
				else
					throw new MoveNotAllowedException ( "There is not here a Ram and a Sheep that can mate..." ) ;
			else
				throw new MoveNotAllowedException ( "Selected Region not adjacent to the selected Sheperd." ) ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This method  
	 * 
	 * @param region
	 * @return
	 */
	public static boolean canMateDueToSexReasons ( Region region ) 
	{
		List < AdultOvine > adultOvines ;
		AdultOvine a ;
		boolean res ;
		if ( region != null )
		{
			adultOvines = MapUtilities.extractAdultOvinesExceptBlackSheep ( region.getContainedAnimals () ) ;
			a = MapUtilities.lookForAnOvine ( adultOvines , AdultOvineType.RAM ) ;
			if ( a != null )
			{
				a = MapUtilities.lookForAnOvine ( adultOvines , AdultOvineType.SHEEP ) ;
				if ( a != null )
					res = true ;
				else
					res = false ;
			}
			else
				res = false ;
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * The effective-algorithm method for this move.
	 * It verifies if all the conditions for this mate to happen are verified.
	 * If so, it tries the Mate; if it go well, it adds the born Lamb to the Region
	 * where the process took place.
	 * 
	 * @param match the Match on which the action is performed.
	 * @throws MoveNotAllowedException if something goes wrong.
	 * @throws WorkflowException 
	 * 
	 * @PRECONDITIONS
	 *  1. match != null
	 * @EXCEPTIONALS_POSTCONDITIONS 
	 *  1. MoveNotAllowedException && there not exist a Ram in the whereMate Region.
	 *  2. MoveNotAllowedException && there not exist a Sheep in the whereMate Region
	 *  3. MoveNotAllowedException and the mate has failed.
	 *  @POSTCONDITIONS
	 *  1. the whereMate contains a new Lamb
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException, WorkflowException 
	{
		Runnable lambGrowerLookerRunnable ;
		Lamb lamb ;
		List < AdultOvine > adultOvines ;
		AdultOvine ram ;
		AdultOvine sheep ;
		if ( match != null )
		{
			adultOvines = MapUtilities.extractAdultOvinesExceptBlackSheep ( whereMate.getContainedAnimals () ) ;
			// look for a male
			ram = MapUtilities.lookForAnOvine ( adultOvines , AdultOvineType.RAM ) ;
			if ( ram != null )
			{
				// look for a female
				sheep = MapUtilities.lookForAnOvine ( adultOvines , AdultOvineType.SHEEP ) ;
				if ( sheep != null )
					try 
					{
						// do the mate
						lamb = sheep.mate ( ram , clockSource.getTurnNumber () ) ;
						whereMate.addAnimal ( lamb ) ;
						lamb.moveTo ( whereMate ); 
						// launch some stuff to manage the evolution of the born Lamb.
						lambGrowerLookerRunnable = new LambGrowerLookerRunnable ( clockSource , lamb , lambEvolver ) ;
						Executors.newSingleThreadExecutor().execute ( lambGrowerLookerRunnable ) ;
					} 
					catch ( CanNotMateWithHimException e ) 
					{
						throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
					} 
					catch ( MateNotSuccesfullException e ) 
					{
						// some type of notification ...
					} 
				else
					throw new MoveNotAllowedException ( "No female to mate!" ) ;
			}
			else
				throw new MoveNotAllowedException ( "No male to mate!" ) ;
		}
		else throw new IllegalArgumentException () ;
	}
	
	/**
	 * The object that will take care of the evolution of lambs, one per lamb. 
	 */
	private class LambGrowerLookerRunnable implements Runnable 
	{
		
		/***/
		private TurnNumberClock turnNumberClock ;
		
		/***/
		private Lamb lambToEvolve ;
		
		/***/
		private LambEvolver lambEvolver ;
		
		/***/
		private int initTurn ;
		
		/***/
		public LambGrowerLookerRunnable ( TurnNumberClock turnNumberClock , Lamb lambToEvolve , LambEvolver lambEvolver ) 
		{
			if ( turnNumberClock != null && lambToEvolve != null && lambEvolver != null )
			{	
				this.turnNumberClock = turnNumberClock ;
				this.lambToEvolve = lambToEvolve ;
				this.lambEvolver = lambEvolver ;
				initTurn = turnNumberClock.getTurnNumber () ;
			}	
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * As the super's one.
		 */
		@Override
		public void run () 
		{
			int currentTurn ;
			boolean finished = false ;
			while ( finished == false )
			{
				currentTurn = turnNumberClock.getTurnNumber () ;
				if ( currentTurn - initTurn == GameConstants.NUMBER_OF_TURN_AFTER_THOSE_A_LAMB_EVOLVE )
				{
					lambEvolver.evolve ( lambToEvolve ) ;
					finished = true ;
				}
			}
		}
		
	}
	
}
