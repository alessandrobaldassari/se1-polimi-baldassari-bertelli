package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

/**
 * This class models the situation where a Player wants go out of the App before the end. 
 */
public class PlayerWantsToExitGameException extends Exception 
{

	/***/
	public PlayerWantsToExitGameException () 
	{
		this ( Utilities.EMPTY_STRING ) ;
	}
	
	/**
	 * @param msg a message to describe the error. 
	 */
	public PlayerWantsToExitGameException ( String msg ) 
	{
		super ( msg ) ;
	}
	
}
