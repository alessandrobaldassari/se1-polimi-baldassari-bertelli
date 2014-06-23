package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PositionableElementTypeTest {

	PositionableElementType positionableElementType;

	@Test
	public void isSheperd() {
		positionableElementType = null;
		assertFalse(PositionableElementType.isSheperd(positionableElementType));
		
		positionableElementType = PositionableElementType.RED_SHEPERD;
		assertTrue(PositionableElementType.isSheperd(positionableElementType));
		
		positionableElementType = PositionableElementType.BLUE_SHEPERD;
		assertTrue(PositionableElementType.isSheperd(positionableElementType));
		
		positionableElementType = PositionableElementType.GREEN_SHEPERD;
		assertTrue(PositionableElementType.isSheperd(positionableElementType));
		
		positionableElementType = PositionableElementType.YELLOW_SHEPERD;
		assertTrue(PositionableElementType.isSheperd(positionableElementType));
		
		positionableElementType = PositionableElementType.BLACK_SHEEP;
		assertFalse(PositionableElementType.isSheperd(positionableElementType));
	}
	
	@Test
	public void isStandardOvine(){
		positionableElementType = null;
		assertFalse(PositionableElementType.isStandardOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.LAMB;
		assertTrue(PositionableElementType.isStandardOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.SHEEP;
		assertTrue(PositionableElementType.isStandardOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.RAM;
		assertTrue(PositionableElementType.isStandardOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.FENCE;
		assertFalse(PositionableElementType.isStandardOvine(positionableElementType));
		
	}

	@Test
	public void sheperdsColors () 
	{
		assertTrue ( PositionableElementType.getSheperdByColor ( "red" ) == PositionableElementType.RED_SHEPERD ) ;
		assertTrue ( PositionableElementType.getSheperdByColor ( "green" ) == PositionableElementType.GREEN_SHEPERD ) ;
		assertTrue ( PositionableElementType.getSheperdByColor ( "blue" ) == PositionableElementType.BLUE_SHEPERD ) ;
		assertTrue ( PositionableElementType.getSheperdByColor ( "yellow" ) == PositionableElementType.YELLOW_SHEPERD ) ;
		assertTrue ( PositionableElementType.getSheperdByColor ( "black" ) == null ) ;

	}
	
}
