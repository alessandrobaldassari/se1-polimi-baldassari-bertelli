package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

/**
 * This Exception class models the situation where a GameMove can not be executed for some reasons. 
 */
public class MoveNotAllowedException extends Exception 
{
	
	/***/
	protected MoveNotAllowedException ( String message ) 
	{
		super ( message ) ;
	}
	
}