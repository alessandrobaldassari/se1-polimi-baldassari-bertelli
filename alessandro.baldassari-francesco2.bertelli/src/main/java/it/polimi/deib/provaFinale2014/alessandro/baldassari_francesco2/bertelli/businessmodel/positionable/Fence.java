package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;

public class Fence extends Positionable < Road >
{

	private FenceType type ;
	
	public Fence ( FenceType type ) 
	{
		if ( type != null )
			this.type = type ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public boolean isFinal () 
	{
		return type == FenceType.FINAL ;
	}
	
	public enum FenceType 
	{
		
		NON_FINAL ,
		
		FINAL
		
	}
	
}
