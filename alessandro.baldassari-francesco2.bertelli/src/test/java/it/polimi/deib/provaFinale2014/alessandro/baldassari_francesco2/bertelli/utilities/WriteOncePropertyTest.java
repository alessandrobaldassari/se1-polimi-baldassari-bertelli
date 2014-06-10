package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class WriteOncePropertyTest {
	
	static WriteOnceProperty <Integer> writeOncePropertySet, writeOncePropertyNotSet;
	Integer intg;
	
	@BeforeClass
	static public void setUpBeforeClass() throws Exception {
		writeOncePropertySet = new WriteOnceProperty<Integer>();
		writeOncePropertyNotSet = new WriteOnceProperty<Integer>();
		writeOncePropertySet.setValue(1);
	}

	@Test (expected = WriteOncePropertyAlreadSetException.class)
	public void setValue() throws WriteOncePropertyAlreadSetException {
		writeOncePropertySet.setValue(2);
	}
	
	@Test (expected = PropertyNotSetYetException.class)
	public void getValue() throws PropertyNotSetYetException{
		assertTrue(writeOncePropertySet.getValue() == 1);
		writeOncePropertyNotSet.getValue();
	}
		
	@Test
	public void IsValueSet(){
		assertTrue(writeOncePropertySet.isValueSet());
		assertFalse(writeOncePropertyNotSet.isValueSet());
		
	}
	
}
