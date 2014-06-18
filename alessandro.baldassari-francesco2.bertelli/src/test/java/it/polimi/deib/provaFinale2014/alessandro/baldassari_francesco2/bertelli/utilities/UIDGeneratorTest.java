package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class UIDGeneratorTest {

	private final long xZero = 100; 
	private UIDGenerator uidGenerator = new UIDGenerator(xZero);

	@Test
	public void generateNewValue() {
		assertTrue(uidGenerator.generateNewValue() == 101);
	}

}
