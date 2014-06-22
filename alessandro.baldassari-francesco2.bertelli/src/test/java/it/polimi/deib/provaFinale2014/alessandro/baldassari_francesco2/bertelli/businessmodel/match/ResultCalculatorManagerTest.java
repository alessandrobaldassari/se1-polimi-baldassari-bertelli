package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import static org.junit.Assert.*;

import javax.xml.ws.FaultAction;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class ResultCalculatorManagerTest{

	DummyMatch dummyMatch;
	ResultsCalculatorManager matchResultsCalculator;
	Player player;
	SellableCard forest;
	
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
	
	@Test
	public void calculatePlayerScore(){
		dummyMatch.match.setMatchState(MatchState.CALCULATING_RESULTS);
		try {
			matchResultsCalculator = new ResultsCalculatorManager(dummyMatch.match);
		} catch (WrongMatchStateMethodCallException e1) {
			fail();
			e1.printStackTrace();
		}

		
		//player 0 initial card: HILL
		player = dummyMatch.players.get(0);
		
		try {
			player.addCard(dummyMatch.bank.sellACard(0, RegionType.FOREST));
		} catch (CardPriceNotRightException e1) {
			fail();
			e1.printStackTrace();
		} catch (NoMoreCardOfThisTypeException e1) {
			fail();
			e1.printStackTrace();
		}
		//adding ram in hill region
		dummyMatch.match.getGameMap().getRegionByUID(1).addAnimal(dummyMatch.animals.get(2));
		//adding the black sheep in hill region
		dummyMatch.match.getGameMap().getRegionByUID(1).addAnimal(dummyMatch.animals.get(1));
		//expected result 8 = 5 (player money) + 1 (ram) + 2 (black sheep)
		try {
			assertTrue(matchResultsCalculator.calculatePlayerScore(player) == 8);
		} catch (WorkflowException e) {
			fail();
			e.printStackTrace();
		}

	}
	
}
