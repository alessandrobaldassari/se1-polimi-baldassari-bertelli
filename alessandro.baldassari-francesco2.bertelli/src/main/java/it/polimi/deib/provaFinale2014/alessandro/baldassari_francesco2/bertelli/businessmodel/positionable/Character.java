package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement;

public abstract class Character < T extends GameMapElement > extends Positionable < T >
{

	private String name ;
	
	public Character ( String name ) 
	{
		if ( name != null )
			this.name = name ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public void moveTo ( T newPosition ) 
	{
		setPosition ( newPosition ) ;
	}
	
	public String getName ()
	{
		return name ;
	}
	
}

