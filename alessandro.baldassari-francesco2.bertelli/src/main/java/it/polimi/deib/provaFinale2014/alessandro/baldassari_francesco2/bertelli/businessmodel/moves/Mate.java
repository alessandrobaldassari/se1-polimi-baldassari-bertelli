package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.CanNotMateWithHimException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.MateNotSuccesfullException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

/**
 * This class models the Mate move.
 * A Sheperd wants a Mate process to happen in a given Region. 
 */
public class Mate extends ExecutableGameMove 
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
	 * @param theOneWhoWantsTheMate the Sheperd who wants to perform this action. 
	 * @param whereMate the Region where this action has to take place. 
	 * @throws IllegalArgumentException if the theOneWhoWantsTheMate or the whereMate parameters
	 *         is null.
	 */
	Mate ( Sheperd theOneWhoWantsTheMate , Region whereMate ) 
	{
		if ( theOneWhoWantsTheMate != null && whereMate != null )
		{
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
						whereMate.getContainedAnimals ().add ( lamb ) ;
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
	private List < AdultOvine > extractAdultOvines ( Collection < Animal > src ) 
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
	
}
