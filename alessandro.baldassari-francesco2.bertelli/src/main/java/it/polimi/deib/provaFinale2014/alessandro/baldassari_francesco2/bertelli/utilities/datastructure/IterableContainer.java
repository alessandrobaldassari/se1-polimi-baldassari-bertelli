package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure;

public class IterableContainer < T > 
{

	private Iterable < T > data ;
	
	public IterableContainer ( Iterable < T > data ) 
	{
		if ( data != null )
			this.data = data ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public Iterable < T > getData ()
	{
		return data ;
	}
	
}
