package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.util.Random;

public class MathUtilities 
{

	private static Random random = new Random () ;
	
	public static int launchDice () 
	{ 
		int res ;
		res = random.nextInt ( 5 ) + 1 ;
		return res ;
	}
	
	public static double genProbabilityValue () 
	{
		double res ;
		res = random.nextDouble () ;
		return res ;
	}
	
}
