package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class CounterTest {

	private final int initialValue = 10;
	private Counter counter = new Counter(initialValue);
	
	@Test
	public void getValue() {
		assertTrue(counter.getValue() == 10);
	}
	
	@Test
	public void increment(){
		counter.increment();
		assertTrue(counter.getValue() == 11);
	}

}
