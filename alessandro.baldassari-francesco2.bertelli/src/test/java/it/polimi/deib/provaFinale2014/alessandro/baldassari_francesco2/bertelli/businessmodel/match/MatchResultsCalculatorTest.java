package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import static org.junit.Assert.*;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

public class MatchResultsCalculatorTest{

	Match match;
	GameMap gameMap;
	Bank bank;
	DummyMatchIdentifier requesterDummyMatchIdentifier;
	ResultsCalculatorManager matchResultsCalculator;
	
	@Before
	public void setUp(){
		requesterDummyMatchIdentifier = new DummyMatchIdentifier(1);
		try {
			gameMap = GameMapFactory.getInstance().newInstance(requesterDummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		try {
			bank = BankFactory.getInstance().newInstance(requesterDummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		match = new Match(gameMap, bank);
	
	}

	@Test
	public void MatchResultCalculator () 
	{
		match.setMatchState(MatchState.CALCULATING_RESULTS);
		try {
			matchResultsCalculator = new ResultsCalculatorManager(match);
		} catch (WrongMatchStateMethodCallException e) {
			assertFalse(true);
		}
		match.setMatchState(MatchState.TURNATION);
		try {
			matchResultsCalculator = new ResultsCalculatorManager(match);
		} catch (WrongMatchStateMethodCallException e) {
			assertTrue(e.getActualState() == MatchState.TURNATION);
		}
		match.setMatchState(MatchState.CALCULATING_RESULTS);
		try {
			matchResultsCalculator = new ResultsCalculatorManager(null);
		}
		catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (WrongMatchStateMethodCallException e) {
			assertTrue(false);
		}
	}
}
