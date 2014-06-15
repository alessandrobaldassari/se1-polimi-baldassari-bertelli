package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.guimap;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

public class GUIMapNotificationMessage implements Serializable
{

	private String actionAssociated ;
	
	private GameMapElementType whereType ;
	
	private int whereId ;
	
	private PositionableElementType whoType;
	
	private int whoId ;
	
	public GUIMapNotificationMessage ( String actionAssociated , GameMapElementType whereType , int whereId , 
			PositionableElementType whoType , int whoId ) 
	{
		this.actionAssociated = actionAssociated ;
		this.whereType = whereType ;
		this.whereId = whereId ;
		this.whoType = whoType ;
		this.whoId = whoId ;
	}
	
	public String getActionAssociated () 
	{
		return actionAssociated ;
	}
	
	public GameMapElementType getWhereType () 
	{
		return whereType ;
	}
	
	public int getWhereId () 
	{
		return whereId ;
	}
	
	public PositionableElementType getWhoType () 
	{
		return whoType ;
	}
	
	public int getWhoId () 
	{
		return whoId ;
	}
	
}
