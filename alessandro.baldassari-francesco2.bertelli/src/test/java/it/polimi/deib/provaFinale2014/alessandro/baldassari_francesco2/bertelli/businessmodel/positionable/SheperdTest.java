package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;

import org.junit.Test;

public class SheperdTest 
{

	@Test
	public void setters () 
	{
		Color c ;
		Player p ;
		Sheperd s ;
		c = Color.RED ;
		p = new DummyPlayer ( "" ) ;
		s = new Sheperd ( "" , c , p ) ;
		assertTrue ( s.getColor().equals ( c ) ) ;
		assertTrue ( s.getOwner().equals ( p ) ) ;
	}
	
}
