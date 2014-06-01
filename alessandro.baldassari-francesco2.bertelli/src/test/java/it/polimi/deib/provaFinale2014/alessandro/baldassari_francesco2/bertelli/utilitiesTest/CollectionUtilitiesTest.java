package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilitiesTest;

import static org.junit.Assert.assertEquals;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/***/
public class CollectionUtilitiesTest 
{
	
	/***/
	@Test
	public void newCollectionFromIterable () 
	{
		List < Integer > in ;
		Iterable < Integer > out ;
		int index ;
		in = new ArrayList < Integer > ( 3 ) ;
		in.add ( 3 ) ;
		in.add ( 1 ) ;
		in.add ( -4 ) ;
		out = CollectionsUtilities.newCollectionFromIterable ( in ) ;
		index = 0 ;
		for ( Integer i : out  )
		{
			assertEquals ( i , in.get ( index ) ) ;
			index ++ ;
		}
		assertEquals ( index , in.size () ) ;
	}
	
	/***/
	@Test
	public void newListFromIterable () 
	{
		List < Integer > in ;
		List < Integer > out ;
		int index ;
		in = new ArrayList < Integer > ( 3 ) ;
		in.add ( -5 ) ;
		in.add ( 0 ) ;
		in.add ( 19 ) ;
		out = CollectionsUtilities.newListFromIterable ( in ) ;
		index = 0 ;
		assertEquals ( in.size () , out.size () ) ;
		for ( index = 0 ; index < in.size () ; index ++ )
			assertEquals ( in.get ( index ) , out.get ( index ) ) ;
	}
	
	/***/
	@Test
	public void newIterableFromArray () 
	{
		Integer [] in ;
		Iterable < Integer > out ;
		int index ;
		in = new Integer [ 4 ] ;
		for ( index = 0 ; index < in.length ; index ++ )
			in [ index  ] = -10 + 3 * index ;
		out = CollectionsUtilities.newIterableFromArray ( in ) ;
		index = 0 ;
		for ( Integer i : out )
		{
			assertEquals ( i , in [ index ] ) ;
			index ++ ;
		}
		assertEquals ( index , in.length ) ;
	}
	
	/***/
	@Test
	public void contains () 
	{
		Collection < Integer > in ;
		in = new ArrayList < Integer > ( 4 ) ;
		boolean res ;
		in.add ( -5 ) ;
		in.add ( -7 ) ;
		in.add ( 32 ) ;
		in.add ( 3527 ) ;
		res = CollectionsUtilities.contains ( in , -5 ) ;
		assertEquals ( res , true ) ;
		res = CollectionsUtilities.contains ( in , 32 ) ;
		assertEquals ( res , true ) ;
		res = CollectionsUtilities.contains ( in , 3527 ) ;
		assertEquals ( res , true ) ;
		res = CollectionsUtilities.contains ( in , 0 ) ;
		assertEquals ( res , false ) ;

	}
	
}
