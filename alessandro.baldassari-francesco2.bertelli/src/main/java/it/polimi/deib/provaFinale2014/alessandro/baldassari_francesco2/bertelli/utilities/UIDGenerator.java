package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.io.Serializable;

public class UIDGenerator implements Serializable
{

	private long lastValueEmitted ;
	
	public UIDGenerator ( long xZero ) 
	{
		lastValueEmitted = xZero ;
	}
	
	public synchronized long generateNewValue () 
	{
		lastValueEmitted ++ ;
		return lastValueEmitted ;
	}
	
}
