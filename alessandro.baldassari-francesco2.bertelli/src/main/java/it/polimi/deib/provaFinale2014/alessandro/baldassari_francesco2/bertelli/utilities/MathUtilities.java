package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.util.Random;

/***/
public class MathUtilities 
{

	/***/
	private static Random random = new Random () ;
	
	/***/
	public static int launchDice () 
	{ 
		return random ( 1 , 6 ) ;
	}
	
	/***/
	public static double genProbabilityValue () 
	{
		double res ;
		res = random.nextDouble () ;
		return res ;
	}
	
	/***/
	public static int random ( int min , int max ) 
	{
		int res ;
		res = random.nextInt ( max - min + 1 ) + min ;
		return res ;
	}
	
}
