package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import org.junit.Before;
import org.junit.Test;

public class TurnationPhaseManagerTest {
	
	private TurnationPhaseManager turnationPhaseManager;
	private DummyMatch dummyMatch;
	private LambEvolverImpl lambEvolverImpl;
	
	@Before
	public void setUp(){
		dummyMatch = new DummyMatch();
		dummyMatch.match.setMatchState(MatchState.TURNATION);
		lambEvolverImpl = new LambEvolverImpl(dummyMatch.animalFactory);
	}
	
	@Test
	public void TurnationPhaseManager() {
		boolean correct = false;
		try{
		turnationPhaseManager = new TurnationPhaseManager(null, lambEvolverImpl);
		}
		catch(IllegalArgumentException e){
			correct = true;
		}
		correct = false;
		try{
			turnationPhaseManager = new TurnationPhaseManager(dummyMatch.match, null);
		}
		catch(IllegalArgumentException e){
			correct = true;
		}
		assertTrue(correct);
		turnationPhaseManager = new TurnationPhaseManager(dummyMatch.match, lambEvolverImpl);
		assertTrue(turnationPhaseManager != null);
	}
	
	@Test
	public void getTurnNumber(){
		dummyMatch.match.setMatchState(MatchState.TURNATION);
		turnationPhaseManager = new TurnationPhaseManager(dummyMatch.match, lambEvolverImpl);
		assertTrue(turnationPhaseManager.getTurnNumber() == 0);
		
		dummyMatch.match.setMatchState(MatchState.SUSPENDED);
		turnationPhaseManager = new TurnationPhaseManager(dummyMatch.match, lambEvolverImpl);
		assertTrue(turnationPhaseManager.getTurnNumber() == 0);		
	}

}
