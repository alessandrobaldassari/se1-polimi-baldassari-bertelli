package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

/**
 * This class modes the situation where a Character should move but, for some reasons,
 * doesn't. 
 */
public class CharacterDoesntMoveException extends Exception
{
	
	/**
	 * the Character involved in the Exception. 
	 */
	private Character character ;
	
	/**
	 * @param character the Character involved in the Exception.
	 * @throws IllegalArgumentException if the character parameter is null 
	 */
	public CharacterDoesntMoveException ( Character character ) 
	{
		super () ;
		if ( character != null )
			this.character = character ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the Character property.
	 * 
	 * @return the Character property.
	 */
	public Character getCharacter () 
	{
		return character ;
	}
	
}
