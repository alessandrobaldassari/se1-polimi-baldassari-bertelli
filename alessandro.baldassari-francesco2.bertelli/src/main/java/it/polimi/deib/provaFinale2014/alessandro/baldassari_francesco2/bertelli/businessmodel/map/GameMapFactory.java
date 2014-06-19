package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.FilePaths;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory.WithFactorySupportObject;

/**
 * Factory class for the GameMap object.
 * It creates ( and ensure is created ) just one GameMap object per Match, and
 * encapsulates all the logic involved in the Map creation. 
 * The implementation of the Singleton pattern ensures that just one instance of
 * this class exists in the whole Application.
 */
public class GameMapFactory extends WithFactorySupportObject < Match > 
{
	
	/**
	 * This class instance to realize the Singleton pattern. 
	 */
	private static GameMapFactory instance ;
	
	/**
	 * A complex object containing the partial loaded data about the Regions in the GameMap 
	 * which will be read by the data files
	 */
	private Map < Integer , Couple < Region , int [] > > regionsMap ;
	
	/**
	 * A complex object containing the partial loaded data about the Roads in the GameMap 
	 * which will be read by the data files
	 */
	private Map < Integer , Couple < Road , int [] > > roadsMap ;
	
	/**
	 * @throws RuntimeException if the data files are not accessible, so the Game can not run. 
	 */
	private GameMapFactory ()  
	{
		super () ;
		InputStream regionsCSVInputStream ;
		InputStream roadsCSVInputStream ;
		try 
		{
			regionsCSVInputStream = Files.newInputStream ( Paths.get ( FilePaths.REGIONS_PATH ) , StandardOpenOption.READ  ) ;
			roadsCSVInputStream = Files.newInputStream ( Paths.get ( FilePaths.ROADS_PATH ) , StandardOpenOption.READ ) ;
			regionsMap = readRegionsDataFile ( regionsCSVInputStream ) ;
			roadsMap = readRoadsDataFile ( roadsCSVInputStream , regionsMap ) ;
			regionsCSVInputStream.close () ;
			roadsCSVInputStream.close () ;
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace () ;
			throw new RuntimeException ( e ) ;
		} 
	} 
	
	/**
	 * Singleton method for this class.
	 * 
	 * @return the only existing instance of this class. 
	 */
	public synchronized static GameMapFactory getInstance () 
	{
		if ( instance == null )
			instance = new GameMapFactory () ;
		return instance ;
	}
	
	/**
	 * Factory method for the GameMap object 
	 * @throws SingletonElementAlreadyGeneratedException if 
	 */
	public GameMap newInstance ( ObjectIdentifier < Match > requester ) throws SingletonElementAlreadyGeneratedException 
	{
		GameMap res ;
		if ( getFactorySupport().isAlreadyUser ( requester ) == false ) 
		{
			getFactorySupport().addUser ( requester ) ;
			res = new GameMap ( regionsMap , roadsMap ) ;
		}
		else
			throw new SingletonElementAlreadyGeneratedException () ;
		return res ;
	}
	
	/**
	 * This method reads the raw data about region contained in the CSV source passed by parameter.
	 * 
	 * @param regionsCSVInputStream the datasource where read the data in a raw CSV format, assuming it's already opened
	 * @return a Map object where, for every entry, the key is the UID of a Region, while the value is a Couple
	 * 	       object where, the first param is the Region object associated with the UID key, and the value is
	 *         an array of int, where each value matches the UID of a Road object ( if the workflow goes 
	 *         well not already created ) bordering this region.
	 */
	private Map < Integer , Couple < Region , int [] > > readRegionsDataFile ( InputStream regionsCSVInputStream )  
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
			lineComponents = scanner.nextLine().split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
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
	 * This method reads the raw data about Roads in the CSV data file passed by parameter.
	 * 
	 * @param roadsCSVInputStream the datasource where read the data in a raw CSV format, assuming it's already opened
	 * @return a Map object where, for every entry, the key is the UID of a Road, while the value is a Couple
	 * 	       object where, the first param is the Road object associated with the UID key, and the value is
	 *         an array of int, where each value matches the UID of a Road object ( if the workflow goes 
	 *         well not already created ) adjacent to this region.
	 */
	private Map < Integer , Couple < Road , int [] > > readRoadsDataFile ( InputStream roadsCSVInputStream , Map < Integer , Couple < Region , int [] > > regionsMap ) 
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
			lineComponents = scanner.nextLine ().split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
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
