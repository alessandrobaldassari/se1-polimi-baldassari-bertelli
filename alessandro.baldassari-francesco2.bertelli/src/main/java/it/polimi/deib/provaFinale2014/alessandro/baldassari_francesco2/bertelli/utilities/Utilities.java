package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class Utilities 
{
	
	/**
	 * The character used as a delimiter in the CSV format. 
	 */
	public static final String CSV_FILE_FIELD_DELIMITER = "," ;
	
	/***/
	public static final int MILLISECONDS_PER_SECOND = 1000 ;

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
