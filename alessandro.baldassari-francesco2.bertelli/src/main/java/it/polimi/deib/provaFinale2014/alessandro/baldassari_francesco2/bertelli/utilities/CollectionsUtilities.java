package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.util.Collection;
import java.util.LinkedList;

public class CollectionsUtilities 
{

	public static < T > Collection < T > newCollectionFromIterable ( Iterable < T > src ) 
	{
		Collection < T > res ;
		res = new LinkedList < T > () ;
		for ( T element : src )
			res.add ( element ) ;
		return res ; 
	}
	
}
