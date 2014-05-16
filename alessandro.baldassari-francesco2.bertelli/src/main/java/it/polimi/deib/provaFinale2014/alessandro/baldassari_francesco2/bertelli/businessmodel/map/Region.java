package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

public class Region extends GameMapElement
{

	private final RegionType type ;
	private Iterable < Road > borderRoads ;
	
	Region ( RegionType type , int uid ) 
	{
		super ( uid ) ;
		if ( type != null )
			this.type = type ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public RegionType getType () 
	{
		return type ;
	}
	
	void setBorderRoads ( Iterable < Road > borderRoads ) 
	{
		this.borderRoads = borderRoads ;
	} 
	
	public Iterable < Road > getBorderRoads () 
	{
		return borderRoads ;
	}
	
	public enum RegionType 
	{
		
		HILL ,
		
		FOREST ,
		
		LACUSTRINE ,
		
		CULTIVABLE ,
		
		MOUNTAIN ,
		
		DESERT ,
		
		SHEEPSBURG
		
	}
	
}
