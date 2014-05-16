package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameMap 
{
	
	private static final String DATA_FILES_DELIMITER = "," ;
	private final Collection < Region > regions ;
	private final Collection < Road > roads ;
	
	public GameMap ( InputStream regionsCSVInputStream , InputStream roadsCSVInputStream ) 
	{
		Map < Integer , Couple < Region , int [] > > regionsMap ;
		Map < Integer , Couple < Road , int [] > > roadsMap ;
		Collection < Road > borderRoads ;
		Collection < Road > adjacentRoads ;
		Region region ;
		Road road ;
		regions = new ArrayList < Region > () ;
		roads = new ArrayList < Road > () ;
		regionsMap = readRegionsDataFile ( regionsCSVInputStream ) ;
		roadsMap = readRoadsDataFile ( roadsCSVInputStream , regionsMap ) ;
		for ( Couple < Region , int [] > couple : regionsMap.values() )
		{
			region = couple.getFirstObject () ;
			regions.add ( region ) ;
			borderRoads = new ArrayList < Road > ( couple.getSecondObject ().length ) ;
			for ( Integer roadUID : couple.getSecondObject () )
				borderRoads.add ( roadsMap.get ( roadUID ).getFirstObject() ) ;
			region.setBorderRoads ( borderRoads ) ;
		}
		for ( Couple < Road , int [] > couple : roadsMap.values () )
		{
			road = couple.getFirstObject () ;
			roads.add ( road ) ;
			adjacentRoads = new ArrayList < Road > ( couple.getSecondObject().length ) ;
			for ( Integer roadUID : couple.getSecondObject () )
				adjacentRoads.add ( roadsMap.get ( roadUID ).getFirstObject() ) ;
			road.setAdjacentRoads ( adjacentRoads ) ;
		}
	}
	
	/*
	public Collection < Road > getFreeRoads () {}

	public Collection < Region > getAdjacentRegions ( Region sourceRegion ) {}
	
	public int getDistance ( Road source , Road destination ) {}
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
	
}
