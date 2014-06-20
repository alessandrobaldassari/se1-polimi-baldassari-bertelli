package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamedColorTest {

	NamedColor namedColor;
	
	@Before
	public void NameColor(){
		namedColor = new NamedColor(255, 0, 0, "Red");
	}
	@Test
	public void getName() {
		assertTrue(namedColor.getName() == "Red");
	}

	@Test
	public void String () 
	{
		namedColor = new NamedColor ( 0 , 0 , 0 , "Black" ) ;
		assertTrue ( namedColor.toString()!=null ) ;
	}
	
}
