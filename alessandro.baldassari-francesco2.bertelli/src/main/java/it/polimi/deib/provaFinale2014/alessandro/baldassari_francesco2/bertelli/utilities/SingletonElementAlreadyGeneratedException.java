package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/**
 * This class models the situation where a component try to create an element that,
 * because has been declared singleton can not be generated again. 
 */
public class SingletonElementAlreadyGeneratedException extends Exception 
{

	/***/
	public SingletonElementAlreadyGeneratedException () 
	{
		super () ;
	}

}
