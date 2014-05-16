package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

public class Couple < T , S > 
{

	private T firstObject ;
	private S secondObject ;
	
	public Couple ( T firstObject , S secondObject ) 
	{
		this.firstObject = firstObject ;
		this.secondObject = secondObject ;
	}
	
	public T getFirstObject () 
	{
		return firstObject ;
	}
	
	public S getSecondObject () 
	{
		return secondObject ;
	}
	
}
