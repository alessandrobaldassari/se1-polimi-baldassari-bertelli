package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.threading;

import java.util.concurrent.atomic.AtomicReference;

public final class ThreadUtilities 
{

	private ThreadUtilities() {}
	
	/***/
	public static void waitForAtomicVariable ( AtomicReference < ? > toWaitFor )
	{
		synchronized ( toWaitFor )
		{
			while ( toWaitFor.get () == null )
				try 
				{
					toWaitFor.wait () ;
				} 
				catch ( InterruptedException e )
				{
					e.printStackTrace () ;
				}
		}
	}
	
}
