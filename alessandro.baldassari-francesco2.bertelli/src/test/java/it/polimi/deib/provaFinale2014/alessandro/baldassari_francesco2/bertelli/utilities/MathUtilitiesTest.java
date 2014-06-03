package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;

import org.junit.Test;

/***/
public class MathUtilitiesTest 
{

	/***/
	@Test
	public void launchDice () 
	{
		double res ;
		res = MathUtilities.launchDice () ;
		assertTrue ( res <= 6 ) ;
		assertTrue ( res >= 1 ) ;
	}
	
	/***/
	@Test
	public void genProbabilityValue () 
	{
		double res ;
		res = MathUtilities.genProbabilityValue () ;
		assertTrue ( res >= 0 ) ;
		assertTrue ( res <= 1 ) ;
	}
	
}
