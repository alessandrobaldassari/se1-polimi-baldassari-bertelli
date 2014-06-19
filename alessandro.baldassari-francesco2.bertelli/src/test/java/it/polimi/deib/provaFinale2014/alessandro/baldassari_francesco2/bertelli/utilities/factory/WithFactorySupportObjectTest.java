package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory.FactorySupport;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory.WithFactorySupportObject;

import org.junit.Test;

public class WithFactorySupportObjectTest {

	private WithFactorySupportObject<Integer> withFactorySupportObject = new WithFactorySupportObject<Integer>();
	private FactorySupport<Integer> factorySupport;
	
	@Test
	public void getFactorySupport() {
		factorySupport = withFactorySupportObject.getFactorySupport();
		assertTrue(factorySupport != null);
	}

}
