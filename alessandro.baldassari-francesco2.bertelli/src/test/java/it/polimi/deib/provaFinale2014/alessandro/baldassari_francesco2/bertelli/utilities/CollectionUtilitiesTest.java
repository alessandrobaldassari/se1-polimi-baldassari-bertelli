package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/***/
public class CollectionUtilitiesTest 
{
	
	/***/
	@Test
	public void iterableSize () 
	{
		List < Integer > l ;
		int s ;
		l = new ArrayList < Integer > ( 3 ) ;
		l.add ( -4 ) ;
		l.add ( 0 ) ;
		l.add ( 1 ) ;
		s = CollectionsUtilities.iterableSize ( l ) ;
		assertEquals ( s , 3 ) ;
	}
	
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
	
	/***/
	@Test
	public void listMesh () 
	{
		List < Integer > in ;
		List < Integer > out ;
		boolean res ;
		in = new ArrayList < Integer > ( 5 ) ;
		out = new ArrayList < Integer > ( 5 ) ;
		in.add ( -32 ) ;
		in.add ( -5 ) ;
		in.add ( 0 ) ;
		in.add ( 456 ) ;
		in.add ( 2455 ) ;
		for ( Integer i : in )
			out.add ( i ) ;
		CollectionsUtilities.listMesh ( out ) ;
		res = in.containsAll ( out ) && out.containsAll ( in ) ;
		assertEquals ( res , true ) ;
	}
	
	/***/
	@Test 
	public void swapElements ()
	{
		List < Integer > in ;
		in = new ArrayList < Integer > ( 4 ) ;
		in.add ( 7 ) ;
		in.add ( -4 ) ;
		in.add ( 9 ) ;
		in.add ( 0 ) ;
		CollectionsUtilities.swapElements ( in , 0 ,  2 ) ;
		assertTrue ( 9 == in.get ( 0 ) ) ;
		assertTrue ( 7 == in.get ( 2 ) ) ;
	}
	
}
