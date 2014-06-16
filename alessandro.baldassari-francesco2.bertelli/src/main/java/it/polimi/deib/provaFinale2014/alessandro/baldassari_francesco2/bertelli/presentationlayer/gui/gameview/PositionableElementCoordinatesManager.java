package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Counter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class that manages the measures of the Elements which are on the GameMap. 
 */
public class PositionableElementCoordinatesManager
{
	
	// ATTRIBUTES 
	
	private Map < Integer , Map < PositionableElementType , Collection < Integer > > > regionsData ;
	
	private Map < Integer , Couple < PositionableElementType , Integer > > roadsData ;
	
	// METHODS
	
	/***/
	public PositionableElementCoordinatesManager () 
	{
		super () ;
		int i ;
		regionsData = new HashMap < Integer , Map < PositionableElementType , Collection < Integer > > > ( GameConstants.NUMBER_OF_REGIONS ) ;		
		roadsData = new HashMap < Integer , Couple < PositionableElementType , Integer > > ( GameConstants.NUMBER_OF_ROADS ) ;
		for ( i = 1 ; i <= GameConstants.NUMBER_OF_REGIONS ; i ++ )
		{
			regionsData.put ( i , new LinkedHashMap < PositionableElementType , Collection < Integer > > () ) ;
			regionsData.get ( i ).put ( PositionableElementType.BLACK_SHEEP , new LinkedList < Integer > () ) ;
			regionsData.get ( i ).put ( PositionableElementType.LAMB , new LinkedList < Integer > () ) ;
			regionsData.get ( i ).put ( PositionableElementType.RAM , new LinkedList < Integer > () ) ;
			regionsData.get ( i ).put ( PositionableElementType.SHEEP , new LinkedList < Integer > () ) ;
			regionsData.get ( i ).put ( PositionableElementType.WOLF , new LinkedList < Integer > () ) ;
		}
		for ( i = 1 ; i < GameConstants.NUMBER_OF_ROADS ; i ++ )
			roadsData.put ( i , null ) ;
	}

	/***/
	public synchronized void addElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId  ) 
	{
		if ( whereType == GameMapElementType.REGION )
			regionsData.get ( whereId ).get ( whoType ).add(whoId) ;
		else
			roadsData.put ( whereId , new Couple < PositionableElementType , Integer >  ( whoType , whoId ) ) ;
	}
	
	/***/
	public synchronized void removeElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId  ) 
	{
		if ( whereType == GameMapElementType.REGION )
			regionsData.get ( whereId ).get ( whoType ).remove ( whoId ) ;
		else
			roadsData.put ( whereId , null ) ;
	}
	
	/***/
	public Map < PositionableElementType , Collection < Integer > > getAnimalsInRegion ( int regionUID ) 
	{
		return regionsData.get ( regionUID ) ;
	}
	
	/***/
	public Couple < PositionableElementType , Integer > getElementInRoad ( int roadUID ) 
	{
		return roadsData.get ( roadUID ) ;
	}
	
}
