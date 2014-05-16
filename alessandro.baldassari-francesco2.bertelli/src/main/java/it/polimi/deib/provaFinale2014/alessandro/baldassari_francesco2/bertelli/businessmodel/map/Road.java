package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

public class Road extends GameMapElement
{

	// ATTRIBUTES
	
	private static final int MIN_NUMBER = 1 ;
	private static final int MAX_NUMBER = 6 ;
	private final Region firstBorderRegion ;
	private final Region secondBorderRegion ;
	private final int number ;
	private Iterable < Road > adjacentRoads ;
	
	// ACCESSOR METHODS 
	
	Road ( int number , int uid , Region firstBorderRegion , Region secondBorderRegion ) 
	{
		super ( uid ) ;
		if ( number >= MIN_NUMBER && number <= MAX_NUMBER && firstBorderRegion != null && secondBorderRegion != null )
		{
			this.number = number ;
			this.firstBorderRegion = firstBorderRegion ;
			this.secondBorderRegion = secondBorderRegion ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public int getNumber () 
	{
		return number ;
	}
	
	void setAdjacentRoads ( Iterable < Road > adjacentRoads ) 
	{
		this.adjacentRoads = adjacentRoads ;
	} 
	
	public Iterable < Road > getAdjacentRoads () 
	{
		return adjacentRoads ;
	}
	
	public Region getFirstBorderRegion () 
	{
		return firstBorderRegion ;
	}
	
	public Region getSecondBorderRegion () 
	{
		return secondBorderRegion ;
	}
	
}
