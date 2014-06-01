package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class Utilities 
{

	/***/
	public static Class < ? > [] getTypes ( Object ... src ) 
	{
		Class < ? > [] res ; 
		int i ;
		res = new Class [ src.length ] ;
		i = 0 ;
		for ( Object o : src )
		{
			res [ i ] = o.getClass () ;
			i ++ ;
		}
		return res ;
	}
	
}
