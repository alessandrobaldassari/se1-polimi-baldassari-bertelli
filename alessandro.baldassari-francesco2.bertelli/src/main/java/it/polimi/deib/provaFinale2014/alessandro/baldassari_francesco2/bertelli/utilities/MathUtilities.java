package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

public class MathUtilities 
{

	public static byte launchDice () 
	{ 
		byte result ;
		double temp ;
		temp = Math.random () ;
		result = ( byte ) ( 6 * temp ) ;
		return result ;
	}
	
	public static double genProbabilityValue () 
	{
		double res ;
		res = Math.random () ;
		return res ;
	}
	
}
