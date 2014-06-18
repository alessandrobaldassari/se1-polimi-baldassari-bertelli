package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameMoveTypeTest {
	
	GameMoveType gameMoveType;

	@Test
	public void getHumanName() {
		gameMoveType = GameMoveType.BREAK_DOWN;
		assertEquals(gameMoveType.getHumanName(), "BREAKDOWN");
		
		gameMoveType = GameMoveType.BUY_CARD;
		assertEquals(gameMoveType.getHumanName(), "BUY_CARD");
		
		gameMoveType = GameMoveType.MATE;
		assertEquals(gameMoveType.getHumanName(), "MATE");
		
		gameMoveType = GameMoveType.MOVE_SHEEP;
		assertEquals(gameMoveType.getHumanName(), "MOVE_SHEEP");
		
		gameMoveType = GameMoveType.MOVE_SHEPERD;
		assertEquals(gameMoveType.getHumanName(), "MOVE_SHEPERD");
		
	}

}
