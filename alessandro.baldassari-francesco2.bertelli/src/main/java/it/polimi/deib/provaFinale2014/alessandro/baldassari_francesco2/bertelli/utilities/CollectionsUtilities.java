package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CollectionsUtilities 
{

	/***/
	public static < T > int iterableSize ( Iterable < T > src ) 
	{
		int res ;
		res = 0 ;
		for ( T t : src )
			res ++ ;
		return res ;
	}
	
	/***/
	public static < T > Collection < T > newCollectionFromIterable ( Iterable < T > src ) 
	{
		Collection < T > res ;
		res = new LinkedList < T > () ;
		for ( T element : src )
			res.add ( element ) ;
		return res ; 
	}
	
	/***/
	public static < T > List < T > newListFromIterable ( Iterable < T > src ) 
	{
		List < T > res ;
		res = new ArrayList < T > () ;
		for ( T t : src )
			res.add ( t ) ;
		return res ;
	}
	
	/***/
	public static < T > Iterable < T > newIterableFromArray ( T [] src ) 
	{
		Collection < T > res ;
		res = new ArrayList < T > ( src.length ) ;
		for ( T t : src )
			res.add ( t ) ;
		return res ;
	}
	
	/***/
	public static < T > boolean contains ( Iterable < T > src , T key ) 
	{
		boolean res ;
		res = false ;
		for ( T t : src )
			if ( key.equals ( t ) )
			{
				res = true ;
				break ;
			}
		return res ;
	}
	
	/***/
	public static < T > void listMesh ( List < T > list ) 
	{
		final Random random ;
		final int NUMBER_OF_EXCHANGES ;
		int index ;
		int pos1 ;
		int pos2 ;
		random = new Random () ;
		NUMBER_OF_EXCHANGES = 100 ;
		for ( index = 0 ; index < NUMBER_OF_EXCHANGES ; index ++ )
		{
			pos1 = random.nextInt ( list.size () ) ;
			pos2 = random.nextInt ( list.size () ) ;
			swapElements ( list , pos1 , pos2 ) ;
		}
	}
	
	/***/
	public static < T > void swapElements ( List < T > list , int pos1 , int pos2 ) 
	{
		T temp ;
		temp = list.get ( pos1 ) ;
		list.set ( pos1 , list.get ( pos2 ) ) ;
		list.set ( pos2 , temp ) ;
	}
	
}
