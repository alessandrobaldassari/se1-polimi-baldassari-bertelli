package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

public class NetworkUtilities 
{

	public static byte [] fromStringToByteArrayIPAddress ( String src ) 
	{
		String [] raw ;
		byte [] res ;
		int index ;
		res = new byte [ 4 ] ;
		raw = src.split ( "." ) ;
		for ( index = 0 ; index < raw.length ; index ++ )
			res [ index ] =  Byte.parseByte ( raw [ index ] ) ; 
		return res ;
	}
	
}
