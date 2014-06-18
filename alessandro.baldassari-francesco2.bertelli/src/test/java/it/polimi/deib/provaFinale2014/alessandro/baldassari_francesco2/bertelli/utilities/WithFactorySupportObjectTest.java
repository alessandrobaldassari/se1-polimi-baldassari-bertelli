package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.*;

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
