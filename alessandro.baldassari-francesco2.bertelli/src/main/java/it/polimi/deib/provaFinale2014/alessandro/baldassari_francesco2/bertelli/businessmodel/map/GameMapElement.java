package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

/**
 * This class represent a generic element which composes the GameMapElement.
 * It is an immutable class, also if it is expectable that its subclasses will not.
 */
public abstract class GameMapElement 
{

	// ATTRIBUTES
		
	/**
	 * The UID for this region, unique for every component of the map that extends this class
	 */
	private final int uid ;
	
	// ACCESSOR METHODS 
	
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
