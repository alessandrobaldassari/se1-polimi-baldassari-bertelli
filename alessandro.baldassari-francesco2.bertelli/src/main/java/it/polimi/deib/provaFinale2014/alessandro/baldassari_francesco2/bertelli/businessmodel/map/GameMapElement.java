package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

public abstract class GameMapElement 
{

	private final int uid ;
	
	public GameMapElement ( int uid ) 
	{
		if ( uid > 0 )
			this.uid = uid ;
		else
			throw new IllegalArgumentException () ;
	}

	public int getUID () 
	{
		return uid ;
	}
	
}
