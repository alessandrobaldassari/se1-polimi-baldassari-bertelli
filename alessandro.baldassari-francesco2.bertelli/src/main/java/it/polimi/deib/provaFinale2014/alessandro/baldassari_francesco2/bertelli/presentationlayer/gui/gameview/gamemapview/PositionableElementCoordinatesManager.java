package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

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
	
	/**
	 * A Map containing all the data about Elements that are in placed in Regions.
	 * Semantics :
	 * key : the Region UID.
	 * value : a Map containing, for each PositionableElementType ( Animal ), a Collection < Integer > containing the UIDs of the Animal here.  
	 */
	private Map < Integer , Map < PositionableElementType , Collection < Integer > > > regionsData ;
	
	/**
	 * A Map containing, for each Road, info about the Elements in. 
	 * Semantics :
	 * key : the Road UID.
	 * value : a Couple < PositionableElementType , Integer >, where the second object is the UID associated to the first object.
	 */
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

	/**
	 * Add an Element to this data structure.
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	public synchronized void addElement ( GUIGameMapNotificationMessage removingMessage  ) 
	{
		if ( removingMessage != null )
			if ( removingMessage.getWhereType () == GameMapElementType.REGION )
				regionsData.get ( removingMessage.getWhereId () ).get ( removingMessage.getWhoType () ).add ( removingMessage.getWhoId () ) ;
			else
				roadsData.put ( removingMessage.getWhereId () , new Couple < PositionableElementType , Integer >  ( removingMessage.getWhoType () , removingMessage.getWhoId () ) ) ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Remove an element from this data structure. 
	 */
	public synchronized void removeElement ( GUIGameMapNotificationMessage removingMessage ) 
	{
		if ( removingMessage != null )
			if ( removingMessage.getWhereType() == GameMapElementType.REGION )
				regionsData.get ( removingMessage.getWhereId() ).get ( removingMessage.getWhoType() ).remove ( removingMessage.getWhoId() ) ;
			else
				roadsData.put ( removingMessage.getWhereId() , null ) ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the regionsData field; finds and returns the data associated with a Region.
	 * 
	 * @param regionUID the UID of the Region of interest.
	 * @return a Map containing all data about the IDs of the Elements in the Region of interest.
	 */
	public Map < PositionableElementType , Collection < Integer > > getAnimalsInRegion ( int regionUID ) 
	{
		return regionsData.get ( regionUID ) ;
	}
	
	/**
	 * Getter method for the roadsData field; finds and returns the data associated with a Road.
	 * 
	 * @param roadUID the UID of the Road of interest.
	 * @param a Couple < PositionableElementType , Integer > containing data about the IDs of the Elements in the Region of interest. 
	 */
	public Couple < PositionableElementType , Integer > getElementInRoad ( int roadUID ) 
	{
		return roadsData.get ( roadUID ) ;
	}
	
}
