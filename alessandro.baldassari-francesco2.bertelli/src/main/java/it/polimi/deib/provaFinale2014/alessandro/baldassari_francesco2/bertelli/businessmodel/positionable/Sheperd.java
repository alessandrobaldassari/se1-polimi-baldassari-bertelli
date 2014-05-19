package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.awt.Color;

public class Sheperd extends Character < Road > 
{

	private Player owner ;
	private final Color color ;
	
	public Sheperd ( String name , Color color , Player owner ) 
	{
		super ( name ) ;
		if ( color != null && owner != null )
		{
			this.color = color ;
			this.owner = owner ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	public Player getOwner () 
	{
		return owner ;
	}
	
	public Color getColor () 
	{
		return color ;
	}
	
}
