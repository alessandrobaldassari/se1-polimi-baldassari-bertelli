package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.assertTrue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import org.junit.Test;

public class SheperdTest 
{

	@Test
	public void setters () 
	{
		NamedColor c ;
		Player p ;
		Sheperd s ;
		c = new NamedColor ( 255 , 0 , 0 , "red" ) ;
		p = new DummyPlayer ( "" ) ;
		s = new Sheperd ( "" , c , p ) ;
		assertTrue ( s.getColor().equals ( c ) ) ;
		assertTrue ( s.getOwner().equals ( p ) ) ;
	}
	
}
