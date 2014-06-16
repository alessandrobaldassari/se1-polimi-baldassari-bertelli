package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.FilePaths;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

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
		
	/***/
	private Map < Integer , Polygon > regionsCoordinates ;
	
	/***/
	private Map < Integer , Ellipse2D > roadsCoordinates ;

	/***/
	private Map < Integer , Map < PositionableElementType , Shape > > animalsInRegions ;

	/***/
	private Map < Integer , PositionableElementType > objectsInRoads ;
	
	/***/
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	// METHODS
	
	/***/
	public MapMeasuresCoordinatesManager ( PositionableElementCoordinatesManager positionableElementManager ) 
	{
		InputStream regionsI ;
		InputStream roadsI ;
		int i ;
		try 
		{
			this.positionableElementsManager = positionableElementManager ;
			regionsI = Files.newInputStream ( Paths.get ( FilePaths.REGIONS_COORDINATES_PATH ) , StandardOpenOption.READ ) ;
			roadsI = Files.newInputStream ( Paths.get ( FilePaths.ROADS_COORDINATES_PATH ) , StandardOpenOption.READ ) ;
			regionsCoordinates = readRegionsCoordinates ( regionsI ) ;
			regionsI.close () ;
			roadsCoordinates = readRoadsCoordinates ( roadsI ) ;
			roadsI.close () ;
			animalsInRegions = new HashMap < Integer , Map < PositionableElementType , Shape > > ( GameConstants.NUMBER_OF_REGIONS ) ;
			for ( i = 1 ; i <= GameConstants.NUMBER_OF_REGIONS ; i ++ )
				animalsInRegions.put ( i , new LinkedHashMap < PositionableElementType , Shape > () ) ;
			objectsInRoads = new HashMap < Integer , PositionableElementType > ( GameConstants.NUMBER_OF_ROADS ) ;
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
		res = new HashMap < Integer , Ellipse2D > ( 42 ) ;
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
	
	/**
	 * @param
	 * @param 
	 */
	public void scale ( float xFactor , float yFactor ) 
	{
		int i ;
		for ( Polygon p : regionsCoordinates.values() )
			for ( i = 0 ; i < p.npoints ; i ++ )
			{
				p.xpoints [ i ] = Math.round ( p.xpoints [ i ] * xFactor ) ;
				p.ypoints [ i ] = Math.round (p.ypoints [ i ] * yFactor) ;
			}
		for ( Ellipse2D p : roadsCoordinates.values () )
			p.setFrame ( p.getX () * xFactor , p.getY () * yFactor , p.getWidth () * xFactor , p.getHeight () * yFactor ) ;
		ROADS_RADIUS = (int) (ROADS_RADIUS * xFactor) ;
	}
		
	/***/
	public Polygon getRegionBorder ( int regionUID )
	{
		return regionsCoordinates.get ( regionUID ) ;
	}
	
	/***/
	public Shape getRoadBorder ( int roadUID ) 
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
		if ( selectedRoadInfo != null && selectedRoadInfo.getFirstObject().equals ( PositionableElementType.SHEPERD ) )
			res = selectedRoadInfo.getSecondObject () ;
		else
			res = null ;
		return res ;
	}
	
	/***/
	public Collection < Integer > getAnimalsId ( int x , int y ) 
	{
		Map < PositionableElementType , Collection < Integer > > animalsIds ;
		Collection < Integer > res ;
		Integer regionUID ;
		PositionableElementType elementTypeSelected ;
		regionUID = getRegionId ( x , y ) ;
		elementTypeSelected = findType ( regionUID , x, y ) ;
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
	}
	
	private PositionableElementType findType ( int regionUID , int x , int y )
	{
		Map < PositionableElementType , Shape > elementsInRegion ;
		PositionableElementType res ;
		elementsInRegion = animalsInRegions.get ( regionUID ) ;
		res = null ;
		for ( PositionableElementType t : elementsInRegion.keySet () )
			if ( elementsInRegion.get ( t ).contains ( x , y ) )
			{
				res = t ;
				break ;
			}
		return res ;
	}
	
	/***/
	public void resetRegionData ( int regionUID ) 
	{
		animalsInRegions.get ( regionUID ).clear () ;
	}
	
	/***/
	public void updateAnimalsInRegionsMap ( int regionId , PositionableElementType t , Shape loc ) 
	{
		animalsInRegions.get ( regionId ).put ( t , loc ) ;
	}
	
	/***/
	public void resetRoadData ( int roadUID ) 
	{
		objectsInRoads.put ( roadUID , null ) ;
	}
	
	/***/
	public void updateObjectInRoadsMap ( int roadId , PositionableElementType t ) 
	{
		objectsInRoads.put ( roadId , t ) ;
	}
	
	/**
	 * Find the key, if it exists for which the ( x , y ) point is in the value associated.
	 * 
	 * @param dataStr
	 * @param x the x value of the interested position.
	 * @param y the y value of the interested position.
	 * @return the id ot the searched object, it it exists, null else.
	 */
	private Integer lookForAPointObjectId ( Map < Integer , ? extends Shape > dataStr , int x , int y ) 
	{
		Integer res ;
		res = null ;
		System.out.println ( "X = " + x + "\nY = " + y );
		for ( Integer i : dataStr.keySet () )
		{
			System.out.println ( dataStr.get(i).getBounds() ) ;
			if ( dataStr.get ( i ).contains ( x , y ) )
			{
				res = i ;
				break ;
			}
		}return res ;
	}
	
}
