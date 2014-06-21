package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import org.junit.Before;
import org.junit.Test;

public class ResultCalculatorManagerTest{

	DummyMatch dummyMatch;
	ResultsCalculatorManager matchResultsCalculator;
	
	@Before
	public void setUp()
	{
		dummyMatch = new DummyMatch();
		try {
			dummyMatch.initializePlayersAndSheperds();
		} catch (WriteOncePropertyAlreadSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoMoreCardOfThisTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dummyMatch.initializeAnimals();
		} catch (WolfAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BlackSheepAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Test
	public void MatchResultCalculator () 
	{
		dummyMatch.match.setMatchState(MatchState.CALCULATING_RESULTS);
		try {
			matchResultsCalculator = new ResultsCalculatorManager(dummyMatch.match);
		} catch (WrongMatchStateMethodCallException e) {
			assertFalse(true);
		}
		dummyMatch.match.setMatchState(MatchState.TURNATION);
		try {
			matchResultsCalculator = new ResultsCalculatorManager(dummyMatch.match);
		} catch (WrongMatchStateMethodCallException e) {
			assertTrue(e.getActualState() == MatchState.TURNATION);
		}
		dummyMatch.match.setMatchState(MatchState.CALCULATING_RESULTS);
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
