package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement;

public abstract class Positionable < T extends GameMapElement > 
{

	private T position ;
	
	protected void setPosition ( T position ) 
	{
		this.position = position ;
	}
	
	public T getPosition () 
	{
		return position ;
	}
	
}
