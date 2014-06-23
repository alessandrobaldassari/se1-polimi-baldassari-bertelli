package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

/**
 * This Exception class models the situation where a GameMove can not be executed for some reasons. 
 */
public class MoveNotAllowedException extends Exception 
{
	
	/**
	 * @param message the message that describes the Exception. 
	 */
	public MoveNotAllowedException ( String message ) 
	{
		super ( message ) ;
	}

	/**
	 * @param message the message that describes the Exception. 
	 * @param cause the cause of this Exception
	 */
	public MoveNotAllowedException ( String message , Throwable cause ) 
	{
		super ( message , cause ) ;
	}
	
}