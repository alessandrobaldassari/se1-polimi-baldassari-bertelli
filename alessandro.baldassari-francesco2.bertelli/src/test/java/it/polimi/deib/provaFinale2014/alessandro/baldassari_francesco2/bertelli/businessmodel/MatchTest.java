package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

public class MatchTest 
{

	private Match m ;
	
	@Before
	public void setUp () 
	{
		int i ;
		Identifiable < Match > id ;
		i = 0 ;
		do 
		{
			try 
			{
				id = new DummyMatchIdentifier ( i) ;
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
			assertTrue ( m.getNumberOfPlayers () == 0 ) ;
			m.setMatchState ( MatchState.WAIT_FOR_PLAYERS ) ;
			m.addPlayer ( new DummyPlayer ( "" ) ) ;
			assertTrue ( m.getNumberOfPlayers () == 1 ) ;
		}
		catch (WrongMatchStateMethodCallException e) 
		{
			fail () ;
		}
	}
	
}
