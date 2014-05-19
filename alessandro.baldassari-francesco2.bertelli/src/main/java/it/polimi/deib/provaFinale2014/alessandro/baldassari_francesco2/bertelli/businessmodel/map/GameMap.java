package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is a passive data structure that represents the GameMap ( as the name says ).
 * It hides the bipartite graph structure of the real map, and offer to its clients only the methods
 * they need to interact with it.
 * The object is immutable, also if the regions and roads contained here are not.
 * 
 * @see Collection
 * @see Region
 * @see Road
 */
public class GameMap 
{

	// ATTRIBUTES
	
	/**
	 * The character used as a delimiter in the CSV InputStreams used to load the data of the map 
	 */
	private static final String DATA_FILES_DELIMITER = "," ;
	
	/**
	 * The map of all the regions that compose this map with the UID as the key and the Region object as the value.
	 */
	private final Map < Integer , Region > regions ;
	
	/**
	 * The map of all the roads that compose this map with the UID as the key and the Road object as the value.
	 */
	private final Map < Integer , Road > roads ;
	
	// ACCESSOR METHODS
	
	/**
	 * Constructs a GameMap object using two InputStreams where raw data about regions, roads and their 
	 * relationship are stored. 
	 * Assumes that these streams are already open and that the contents they point to are the right ones
	 * 
	 * @param regionsCSVInputStream the InputStream where take the raw data about the regions
	 * @param roadsCSVInputStream the InputStream where take the raw data about the roads
	 */
	public GameMap ( InputStream regionsCSVInputStream , InputStream roadsCSVInputStream ) 
	{
		Map < Integer , Couple < Region , int [] > > regionsMap ;
		Map < Integer , Couple < Road , int [] > > roadsMap ;
		Collection < Road > borderRoads ;
		Collection < Road > adjacentRoads ;
		Region region ;
		Road road ;
		regions = new HashMap < Integer , Region > () ;
		roads = new HashMap < Integer , Road > () ;
		regionsMap = readRegionsDataFile ( regionsCSVInputStream ) ;
		roadsMap = readRoadsDataFile ( roadsCSVInputStream , regionsMap ) ;
		for ( Couple < Region , int [] > couple : regionsMap.values() )
		{
			region = couple.getFirstObject () ;
			regions.put ( region.getUID() , region ) ;
			borderRoads = new ArrayList < Road > ( couple.getSecondObject ().length ) ;
			for ( Integer roadUID : couple.getSecondObject () )
				borderRoads.add ( roadsMap.get ( roadUID ).getFirstObject() ) ;
			region.setBorderRoads ( borderRoads ) ;
		}
		for ( Couple < Road , int [] > couple : roadsMap.values () )
		{
			road = couple.getFirstObject () ;
			roads.put ( road.getUID () , road ) ;
			adjacentRoads = new ArrayList < Road > ( couple.getSecondObject().length ) ;
			for ( Integer roadUID : couple.getSecondObject () )
				adjacentRoads.add ( roadsMap.get ( roadUID ).getFirstObject() ) ;
			road.setAdjacentRoads ( adjacentRoads ) ;
		}
	}
	
	public Region getRegionByUID ( int uid ) 
	{
		Region res ;
		res = regions.get ( uid ) ;
		return res ;
	}
	
	public Road getRoadByUID ( int uid ) 
	{
		Road res ;
		res = roads.get ( uid ) ;
		return res ;
	}
	
	/**
	 * Select and returns all the free Road objects contained in this map.
	 * A Road is considered free if, and only if, there is nor a Sheperd or a Fence in it.
	 * 
	 * @return an Iterable containing all the Road object considered free.
	 */
	public Iterable < Road > getFreeRoads () 
	{
		Collection < Road > res ;
		res = new LinkedList < Road > () ;
		for ( Road road : roads.values () )
			if ( road.getElementContained() == null )
				res.add ( road ) ;
		return res ;
	}

	// HELPER METHODS
	
	/**
	 * @param regionsCSVInputStream the datasource where read the data in a raw CSV format, assuming it's already opened
	 * @return a Map object where, for every entry, the key is the UID of a Region, while the value is a Couple
	 * 	       object where, the first param is the Region object associated with the UID key, and the value is
	 *         an array of int, where each value matches the UID of a Road object ( if the workflow goes 
	 *         well not already created ) bordering this region.
	 */
	private static Map < Integer , Couple < Region , int [] > > readRegionsDataFile ( InputStream regionsCSVInputStream )  
	{
		String [] lineComponents ;
		int [] borderRoadsUID ;
		Map < Integer , Couple < Region , int [] > > res ; 
		Region region ;
		Scanner scanner ;
		byte i ;
		res = new HashMap < Integer , Couple < Region , int [] > > () ;
		scanner = new Scanner ( regionsCSVInputStream ) ;
		while ( scanner.hasNextLine () )
		{
			lineComponents = scanner.nextLine().split ( DATA_FILES_DELIMITER ) ;
			region = new Region ( Region.RegionType.valueOf ( lineComponents [ 1 ] ) , Integer.parseInt ( lineComponents [ 0 ] ) ) ;		
			borderRoadsUID = new int [ lineComponents.length - 2 ] ;
			for ( i = 2 ; i < lineComponents.length ; i ++ )
				borderRoadsUID [ i - 2 ] = Integer.parseInt ( lineComponents [ i ] ) ;
			res.put ( region.getUID() , new Couple < Region , int [] > ( region , borderRoadsUID ) ) ;
		}
		scanner.close () ;
		return res ;
	}
	
	/**
	 * @param roadsCSVInputStream the datasource where read the data in a raw CSV format, assuming it's already opened
	 * @return a Map object where, for every entry, the key is the UID of a Road, while the value is a Couple
	 * 	       object where, the first param is the Road object associated with the UID key, and the value is
	 *         an array of int, where each value matches the UID of a Road object ( if the workflow goes 
	 *         well not already created ) adjacent to this region.
	 */
	private static Map < Integer , Couple < Road , int [] > > readRoadsDataFile ( InputStream roadsCSVInputStream , Map < Integer , Couple < Region , int [] > > regionsMap ) 
	{
		String [] lineComponents ;
		int [] adjacentRoadsUID ;
		Map < Integer , Couple < Road , int [] > > res ;
		Road road ;
		Scanner scanner ;
		byte i ;
		res = new HashMap < Integer , Couple < Road , int [] > > () ;
		scanner = new Scanner ( roadsCSVInputStream ) ;
		while ( scanner.hasNextLine () )
		{
			lineComponents = scanner.nextLine ().split ( DATA_FILES_DELIMITER ) ;
			road = new Road ( Integer.parseInt ( lineComponents [ 1 ] ) , Integer.parseInt ( lineComponents [ 0 ] ) , regionsMap.get ( Integer.parseInt ( lineComponents [ 2 ] ) ).getFirstObject () , regionsMap.get ( Integer.parseInt ( lineComponents [ 3 ] ) ).getFirstObject () ) ;
			adjacentRoadsUID = new int [ lineComponents.length - 4 ] ;
			for ( i = 4 ; i < lineComponents.length ; i ++ )
				adjacentRoadsUID [ i - 4 ] = Integer.parseInt ( lineComponents [ i ] ) ;
			res.put ( road.getUID () , new Couple < Road ,int [] > ( road , adjacentRoadsUID ) ) ;
		}
		scanner.close () ;
		return res ;
	}

	/**
	 * EXACTLY AS THE SUPER ONE'S
	 */
	@Override
	public String toString () 
	{
		String res ;
		StringBuffer stringBuffer ;
		stringBuffer = new StringBuffer () ;
		stringBuffer.append ( "Class : " + getClass ().getName() );
		
		res = stringBuffer.toString () ;
		return res ;
	}
	
}
