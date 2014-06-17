package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

public class BankFactoryTest {
	
	BankFactory bankFactory;
	Bank bank;
	Identifiable<Match> dummyMatchIdentifier; 
	
	@Before
	public void setUp(){
		dummyMatchIdentifier = MatchIdentifier.newInstance();
		bankFactory = BankFactory.getInstance();
		
	}
	
	@Test (expected = SingletonElementAlreadyGeneratedException.class)
	public void newInstance() throws SingletonElementAlreadyGeneratedException {
		try {
			bank = bankFactory.newInstance(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(bank != null);
		bank = bankFactory.newInstance(dummyMatchIdentifier);
	}

}
