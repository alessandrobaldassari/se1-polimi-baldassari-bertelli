package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.Test;

public class BankFactoryTest {
	
	BankFactory bankFactory;
	Bank bank;
	DummyMatchIdentifier dummyMatchIdentifier;
	
	@Before
	public void setUp(){
		dummyMatchIdentifier = new DummyMatchIdentifier(1);
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
