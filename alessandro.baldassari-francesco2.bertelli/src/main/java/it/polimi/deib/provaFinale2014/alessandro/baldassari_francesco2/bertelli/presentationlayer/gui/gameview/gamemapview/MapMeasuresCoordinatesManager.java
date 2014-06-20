package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.FilePaths;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * Class that manages the measures of the GameMap.
 */
public class MapMeasuresCoordinatesManager
{

	// ATTRIBUTES
	
	/***/
	int ROADS_RADIUS = 25 ;
		
	int ANIMAL_RADIUS = 20 ;
	
	/***/
	private Map < Integer , Polygon > regionsCoordinates ;
	
	/***/
	private Map < Integer , Ellipse2D > roadsCoordinates ;

	/***/
	private Map < Integer , Map < PositionableElementType , Ellipse2D > > animalsInRegions ;

	/***/
	private Map < Integer , Couple < PositionableElementType , Ellipse2D > > objectsInRoads ;
	
	/***/
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	// METHODS
	
	/***/
	public MapMeasuresCoordinatesManager ( PositionableElementCoordinatesManager positionableElementManager ) 
	{
		InputStream regionsI ;
		InputStream roadsI ;
		InputStream animalsI ;
		int i ;
		try 
		{
			this.positionableElementsManager = positionableElementManager ;
			regionsI = Files.newInputStream ( Paths.get ( FilePaths.REGIONS_COORDINATES_PATH ) , StandardOpenOption.READ ) ;
			roadsI = Files.newInputStream ( Paths.get ( FilePaths.ROADS_COORDINATES_PATH ) , StandardOpenOption.READ ) ;
			animalsI = Files.newInputStream ( Paths.get ( FilePaths.REGIONS_ANIMALS_PATH ) , StandardOpenOption.READ ) ;
			regionsCoordinates = readRegionsCoordinates ( regionsI ) ;
			regionsI.close () ;
			roadsCoordinates = readRoadsCoordinates ( roadsI ) ;
			roadsI.close () ;
			animalsInRegions = readAnimalsCoordinates ( animalsI ) ;
			objectsInRoads = new HashMap < Integer , Couple < PositionableElementType , Ellipse2D > > ( GameConstants.NUMBER_OF_ROADS ) ;
			for ( i = 1 ; i <= GameConstants.NUMBER_OF_ROADS ; i ++ )
				objectsInRoads.put ( i , null ) ;
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
	}
	
	/***/
	private Map < Integer , Polygon > readRegionsCoordinates ( InputStream in ) 
	{
		String [] values ;
		Map < Integer , Polygon > res ;
		Polygon p ;
		Scanner s ;
		String rawEntry ;
		int i ;
		res = new HashMap < Integer , Polygon > ( 19 ) ;
		s = new Scanner ( in ) ;
		while ( s.hasNextLine () )
		{
			rawEntry = s.nextLine () ;
			values = rawEntry.split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
			p = new Polygon () ;
			for ( i = 1 ; i < values.length ; i = i + 2 )
				p.addPoint ( Integer.parseInt ( values [ i ] ) , Integer.parseInt ( values [ i + 1 ] ) ) ;
			res.put ( Integer.parseInt ( values [ 0 ] ) , p ) ;
		}
		s.close () ;
		return res ;
	}
	
	/***/
	private Map < Integer , Ellipse2D > readRoadsCoordinates ( InputStream in ) 
	{
		String [] data ;
		Map < Integer , Ellipse2D > res ;
		Scanner s ;
		Ellipse2D e ;
		String rawEntry ;
		res = new HashMap < Integer , Ellipse2D > ( GameConstants.NUMBER_OF_ROADS ) ;
		s = new Scanner ( in ) ;
		while ( s.hasNextLine () )
		{
			rawEntry = s.nextLine () ;
			data = rawEntry.split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
			e = new Ellipse2D.Float ( Integer.parseInt ( data [ 1 ] ) - ROADS_RADIUS , Integer.parseInt ( data [ 2 ] ) - ROADS_RADIUS , ROADS_RADIUS * 2 , ROADS_RADIUS * 2 ) ;
			res.put ( Integer.parseInt ( data [ 0 ] ) , e ) ;
		}
		s.close () ;
		return res ;
	} 
	
	/***/
	private Map  < Integer , Map < PositionableElementType , Ellipse2D > > readAnimalsCoordinates ( InputStream in ) 
	{
		String [] data ;
		Scanner s ;
		String rawEntry ;
		Map  < Integer , Map < PositionableElementType , Ellipse2D > > res ;
		Map < PositionableElementType , Ellipse2D > m ;
		res = new HashMap  < Integer , Map < PositionableElementType , Ellipse2D > > ( GameConstants.NUMBER_OF_REGIONS ) ;
		s = new Scanner ( in ) ;
		while ( s.hasNextLine () )
		{
			rawEntry = s.nextLine () ;
			data = rawEntry.split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
			m = new HashMap < PositionableElementType , Ellipse2D > () ;
			res.put ( Integer.parseInt ( data [0] ) , m ) ;
			m.put ( PositionableElementType.STANDARD_ADULT_OVINE , new Ellipse2D.Float ( Integer.parseInt ( data [ 1 ] ) , Integer.parseInt ( data [ 2 ] ) , ANIMAL_RADIUS * 2 , ANIMAL_RADIUS * 2 ) ) ;
			m.put ( PositionableElementType.BLACK_SHEEP , new Ellipse2D.Float ( Integer.parseInt ( data [ 3 ] ) , Integer.parseInt ( data [ 4 ] ) , ANIMAL_RADIUS * 2 , ANIMAL_RADIUS * 2 ) ) ;
			m.put ( PositionableElementType.WOLF , new Ellipse2D.Float ( Integer.parseInt ( data [ 5 ] ) , Integer.parseInt ( data [ 6 ] ) , ANIMAL_RADIUS * 2 , ANIMAL_RADIUS * 2 ) ) ;
		}
		s.close();
		return res ;
	}
	
	/***/
	public Map < PositionableElementType , Ellipse2D > getRegionData ( Integer regionUID ) 
	{
		return animalsInRegions.get ( regionUID ) ;
	}
	
	/**
	 * @param
	 * @param 
	 */
	public void scale ( float xFactor , float yFactor ) 
	{
		Map < PositionableElementType , Ellipse2D > m ;
		int i ;
		for ( Polygon p : regionsCoordinates.values() )
			for ( i = 0 ; i < p.npoints ; i ++ )
			{
				p.xpoints [ i ] = ( int ) ( p.xpoints [ i ] * xFactor ) ;
				p.ypoints [ i ] = ( int ) ( p.ypoints [ i ] * yFactor ) ;
			}
		for ( Ellipse2D p : roadsCoordinates.values () )
			p.setFrame ( p.getX () * xFactor , p.getY () * yFactor , p.getWidth () * xFactor , p.getHeight () * yFactor ) ;
		for ( Integer r : animalsInRegions.keySet() )
		{
			m = animalsInRegions.get ( r ) ;
			for ( Ellipse2D e : m.values () )
				e.setFrame ( e.getX () * xFactor , e.getY () * yFactor , e.getWidth () * xFactor , e.getHeight () * yFactor ) ;
		}
		for ( Integer r : objectsInRoads.keySet() )
		{
			if ( objectsInRoads.get (r) != null )
			{
				Ellipse2D e = objectsInRoads.get ( r ).getSecondObject () ;
				e.setFrame ( e.getX () * xFactor , e.getY () * yFactor , e.getWidth () * xFactor , e.getHeight () * yFactor ) ;
			}
		}
		ROADS_RADIUS = (int) (ROADS_RADIUS * xFactor) ;
	}
		
	/***/
	public Polygon getRegionBorder ( int regionUID )
	{
		return regionsCoordinates.get ( regionUID ) ;
	}
	
	/***/
	public Ellipse2D getRoadBorder ( int roadUID ) 
	{
		return roadsCoordinates.get ( roadUID ) ;
	}
	
	/***/
	public Integer getRegionId ( int x , int y ) 
	{
		Integer res ;
		res = null ;
		for ( Integer uid : regionsCoordinates.keySet () )
			if ( regionsCoordinates.get(uid).contains ( x , y ) )
			{
				res = uid ;
				break ;
			}
		return res ;
	}
	
	/***/
	public Integer getRoadId ( int x , int y ) 
	{
		Integer res ;
		res = null ;
		for ( Integer uid : roadsCoordinates.keySet () )
			if ( roadsCoordinates.get ( uid ).contains ( x , y ) )
			{
				res = uid ;
				break ;
			}
		return res ;
	}

	/***/
	public Integer getSheperdId ( int x , int y ) 
	{
		Couple < PositionableElementType , Integer > selectedRoadInfo ;
		Integer res ;
		Integer roadUID ;
		roadUID = getRoadId ( x , y ) ;
		selectedRoadInfo = positionableElementsManager.getElementInRoad ( roadUID ) ;
		System.out.println ( "\n\n\nMMCM : " + selectedRoadInfo +"\n\n\n" ) ;
		if ( selectedRoadInfo != null && PositionableElementType.isSheperd( selectedRoadInfo.getFirstObject () ) )
			res = selectedRoadInfo.getSecondObject () ;
		else
			res = null ;
		return res ;
	}
	
	/**
	public Collection < Integer > getAnimalsId ( int x , int y ) 
	{
		Map < PositionableElementType , Collection < Integer > > animalsIds ;
		Collection < Integer > res ;
		Integer regionUID ;
		PositionableElementType elementTypeSelected ;
		regionUID = getRegionId ( x , y ) ;
		System.out.println ( "MAP_MEASURES_COORDINATES_MANAGER - GET_ANIMALS_ID : REGION UID RETRIEVED : " + regionUID ) ;
		elementTypeSelected = findType ( regionUID , x, y ) ;
		System.out.println ( "MAP_MEASURES_COORDINATES_MANAGER - GET_ANIMALS_ID : ELEMENT TYPE RETRIEVED : " + elementTypeSelected ) ;
		if ( elementTypeSelected != null )
		{
			animalsIds = positionableElementsManager.getAnimalsInRegion ( regionUID ) ;
			if ( animalsIds.containsKey ( elementTypeSelected ) )
				res = animalsIds.get ( elementTypeSelected ) ;
			else
				res = new LinkedList < Integer > () ;
		}
		else
			res = new LinkedList < Integer > () ;
		return res ;
	}**/
	
}
