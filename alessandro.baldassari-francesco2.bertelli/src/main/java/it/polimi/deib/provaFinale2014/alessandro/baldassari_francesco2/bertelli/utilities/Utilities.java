package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class Utilities 
{
	
	public static final String EMPTY_STRING = "" ;
	
	public static final String CARRIAGE_RETURN = "\n" ;
	
	/**
	 * The character used as a delimiter in the CSV format. 
	 */
	public static final String CSV_FILE_FIELD_DELIMITER = "," ;
	
	/***/
	public static final int MILLISECONDS_PER_SECOND = 1000 ;

	/***/
	public static boolean isEmptyString ( String s ) 
	{
		boolean res ;
		if ( s != null )
		{
			if ( s.trim().compareToIgnoreCase ( EMPTY_STRING ) == 0 )
				res = true ;
			else
				res = false ;
		}
		else
			throw new NullPointerException();
		return res ;
	}
	
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
	
	/***/
	public static String fromBackslashnStringToBrHtmlString ( String backslashnString ) 
	{
		String [] lines ;
		String res ;
		int i ;
		lines = backslashnString.split ( CARRIAGE_RETURN ) ; 
		res = "<html>" ;
		for ( i = 0 ; i < lines.length - 1 ; i ++ ) 
			res = res + lines [ i ] + "<br>" ;
		res = res + lines [ i ] ;
		res = res + "</html>" ;
		return res ;
	}
	
}
