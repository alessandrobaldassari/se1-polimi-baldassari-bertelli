package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Test;

public class MoveSelectionTest 
{

	private MoveSelection selection ;
	
	@Test ( expected = IllegalArgumentException.class )
	public void ctorException () 
	{
		new MoveSelection ( null , null ) ;
	}

	@Test
	public void ctorOk () 
	{
		MoveSelection m ;
		GameMoveType g ;
		Iterable < Serializable > p ;
		g = GameMoveType.BREAK_DOWN ;
		p = Collections.<Serializable>singleton ( new String () ) ;
		m = new MoveSelection(g, p);
		assertTrue ( m != null ) ;
	}
	
	@Test
	public void getType () 
	{
		MoveSelection m ;
		GameMoveType g ;
		Iterable < Serializable > p ;
		g = GameMoveType.BREAK_DOWN ;
		p = Collections.emptyList();
		m = new MoveSelection(g, p);
		assertTrue ( g == m.getSelectedType() ) ;
	}
	
	@Test
	public void getParams () 
	{
		MoveSelection m ;
		GameMoveType g ;
		List < Serializable > p ;
		int i ;
		g = GameMoveType.BREAK_DOWN ;
		p = new ArrayList < Serializable > () ;
		p.add ( "Ciao" ) ;
		p.add ( new Integer ( 3 ) ) ;
		m = new MoveSelection(g, p);
		i = 0 ;
		assertTrue ( p.size() == CollectionsUtilities.iterableSize ( m.getParams () ) );
		for ( Serializable s : m.getParams() )
		{
			assertTrue ( s.equals ( p.get(i) ) ) ;
			i ++ ;
		}
	}
	
	@Test
	public void equals () 
	{
		MoveSelection m1 ;
		MoveSelection m2 ;
		GameMoveType g ;
		List < Serializable > p ;
		int i ;
		g = GameMoveType.BREAK_DOWN ;
		p = new ArrayList < Serializable > () ;
		p.add ( "Ciao" ) ;
		p.add ( new Integer ( 3 ) ) ;
		m1 = new MoveSelection(g, p);
		m2 = new MoveSelection ( g , p ) ;
		assertTrue ( m1.equals ( m2 ) ) ;
		assertFalse ( m1.equals ( 3 ) ) ;
		p.add ( 2 ) ;
		m2 = new MoveSelection ( g , p ) ;
		assertFalse ( m1.equals(m2) );
	}
	
}
