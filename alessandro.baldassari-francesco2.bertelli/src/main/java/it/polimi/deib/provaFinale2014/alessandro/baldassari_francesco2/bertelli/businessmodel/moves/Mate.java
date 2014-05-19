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

public class Mate extends ExecutableGameMove 
{

	private Sheperd theOneWhoWantsTheMate ;
	private Region whereMate ;
	
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

	private List < AdultOvine > extractAdultOvines ( Collection < Animal > src ) 
	{
		List < AdultOvine > res ;
		res = new LinkedList < AdultOvine > () ;
		for ( Animal animal : src )
			if ( animal instanceof AdultOvine )
				res.add ( ( AdultOvine ) animal ) ;
		return res ;
	}
	
	private AdultOvine lookForAnOvine ( List < AdultOvine > src , AdultOvineType type ) 
	{
		AdultOvine res ;
		int i ;
		res = null ;
		i = 0 ;
		while ( i < src.size() && src.get ( i ).getType () != type ) ;
		i ++ ;
		if ( i < src.size () )
			res = src.get ( i ) ;
		return res ;
	}
	
}
