package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.AlreadyInFinalPhaseException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

public class MatchTest 
{

	private Match m ;
	
	@Before
	public void setUp () 
	{
		int i ;
		ObjectIdentifier < Match > id ;
		i = 0 ;
		do 
		{
			try 
			{
				id = MatchIdentifier.newInstance();
				m = new Match ( GameMapFactory.getInstance().newInstance ( id ) , BankFactory.getInstance().newInstance ( id ) ) ;
			} 
			catch (SingletonElementAlreadyGeneratedException e) 
			{
				i ++ ;
			}
		}
		while ( m == null ) ;
 	}
	
	@Test
	public void getNumberOfPlayers () 
	{
		try
		{
			Player p ;
			p = new DummyPlayer("");
			assertTrue ( m.getNumberOfPlayers () == 0 ) ;
			m.setMatchState ( MatchState.WAIT_FOR_PLAYERS ) ;
			m.addPlayer ( p ) ;
			assertTrue ( m.getNumberOfPlayers () == 1 ) ;
			m.removePlayer ( p );
			assertTrue ( m.getNumberOfPlayers() == 0 ) ;
		}
		catch (WrongMatchStateMethodCallException e) 
		{
			fail () ;
		}
	}
	
	@Test 
	public void setPlayerOrder () 
	{
		List < Player > in ;
		List < Player > out ;
		Map < Player , Integer > x ;
		Player p1 ;
		Player p2 ;
		Player p3 ;
		p1 = new DummyPlayer ( "1" ) ;
		p2 = new DummyPlayer ( "2" ) ;
		p3 = new DummyPlayer ( "3" ) ;	
		in = new ArrayList < Player > ( 3 ) ;
		in.add(p1);
		in.add(p2);
		in.add(p3);
		CollectionsUtilities.listMesh(in); 
		x = new LinkedHashMap < Player , Integer > ( 3 ) ;
		for ( int i = 0 ; i < in.size() ; i ++ )
			x.put ( in.get(i) , i );
		try 
		{
			m.setMatchState ( MatchState.WAIT_FOR_PLAYERS );
			m.addPlayer(p1);
			m.addPlayer(p2);
			m.addPlayer(p3);
			m.setMatchState ( MatchState.INITIALIZATION );
			m.setPlayerOrder ( x ) ;
			out = CollectionsUtilities.newListFromIterable( m.getPlayers() ) ;
			assertTrue ( in.containsAll(out) && out.containsAll ( in ) ) ;
		}
		catch (WrongMatchStateMethodCallException e) 
		{
			fail () ;
		}
		
	}
	
	/***/
	@Test
	public void setMatchState () 
	{
		assertTrue ( m.getMatchState().equals ( MatchState.CREATED ) ) ;
		try
		{
			m.setMatchState ( null );
			fail () ;
		}
		catch ( IllegalArgumentException e )
		{
			assertTrue ( m.getMatchState().equals ( MatchState.CREATED ) ) ;
		}
		m.setMatchState ( MatchState.CALCULATING_RESULTS ) ;
		assertTrue ( m.getMatchState().equals ( MatchState.CALCULATING_RESULTS )) ;
	}
	
	@Test
	public void finaPhase () 
	{
		assertTrue ( m.isInFinalPhase() == false ) ;
		try 
		{
			m.enterFinalPhase();
			assertTrue ( m.isInFinalPhase() ) ;
		} 
		catch (AlreadyInFinalPhaseException e) 
		{
			fail () ;
		}
		try 
		{
			m.enterFinalPhase();
			fail () ;
		} 
		catch (AlreadyInFinalPhaseException e) 
		{
			assertTrue ( m.isInFinalPhase() ) ;
		}
	}
	
}
