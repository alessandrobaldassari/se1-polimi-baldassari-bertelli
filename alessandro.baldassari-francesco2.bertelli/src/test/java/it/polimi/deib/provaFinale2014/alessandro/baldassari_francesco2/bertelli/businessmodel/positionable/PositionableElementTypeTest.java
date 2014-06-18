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
		assertFalse(PositionableElementType.isStandardAdultOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.LAMB;
		assertTrue(PositionableElementType.isStandardAdultOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.SHEEP;
		assertTrue(PositionableElementType.isStandardAdultOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.RAM;
		assertTrue(PositionableElementType.isStandardAdultOvine(positionableElementType));
		
		positionableElementType = PositionableElementType.FENCE;
		assertFalse(PositionableElementType.isStandardAdultOvine(positionableElementType));
		
	}

}
