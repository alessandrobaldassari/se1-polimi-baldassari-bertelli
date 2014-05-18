package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilitiesTest;

import org.junit.*;
import static org.junit.Assert.*;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.*;

public class CoupleTest {

	@Before public void setUp(){
		private Couple couple = new Couple(Integer 10, String "Pippo");
	}
	
	@Test public void getFirstObject(){
		assertEquals(couple.getFirstObject(), 10);
	}
	@Test public void getSecondObject(){
		assertEquals(couple.getSecondObject(), "Pippo");
	}
}
