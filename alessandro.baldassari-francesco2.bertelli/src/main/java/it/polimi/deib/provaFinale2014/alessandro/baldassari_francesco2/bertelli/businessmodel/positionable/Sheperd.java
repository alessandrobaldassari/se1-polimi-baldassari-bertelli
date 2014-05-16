package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;

import java.awt.Color;

public class Sheperd extends Character < Road > 
{

	private final Color color ;
	
	public Sheperd ( String name , Color color ) 
	{
		super ( name ) ;
		if ( color != null )
			this.color = color ;
		else
			throw new IllegalArgumentException () ;
	}

	public Color getColor () 
	{
		return color ;
	}
	
}
