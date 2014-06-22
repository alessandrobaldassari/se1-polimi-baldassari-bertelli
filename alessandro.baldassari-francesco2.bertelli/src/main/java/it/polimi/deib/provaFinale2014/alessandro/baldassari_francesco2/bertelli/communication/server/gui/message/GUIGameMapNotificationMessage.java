package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

/***/
public class GUIGameMapNotificationMessage extends GUINotificationMessage 
{

	/***/
	private GameMapElementType whereType ;
	
	/***/
	private int whereId ;
	
	/***/
	private PositionableElementType whoType;
	
	/***/
	private int whoId ;
	
	/***/
	private GUIGameMapNotificationMessage ( String actionAssociated , GameMapElementType whereType , int whereId , 
			PositionableElementType whoType , int whoId ) 
	{
		super ( actionAssociated ) ;
		if ( whereType != null && whoType != null )
		{
			this.whereType = whereType ;
			this.whereId = whereId ;
			this.whoType = whoType ;
			this.whoId = whoId ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public static GUIGameMapNotificationMessage newInstance ( String actionAssociated , GameMapElementType whereType , int whereId , 
			PositionableElementType whoType , int whoId ) 
	{
		return new GUIGameMapNotificationMessage(actionAssociated, whereType, whereId, whoType, whoId);
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
	
	@Override
	public String toString () 
	{
		return "GUIGameMapNotificationMessage\nAction : " + getActionAssociated() + "\nWhere : " + whereType + " [ " + whereId + " ]\nWho : " + whoType + " [ " + whoId + " ] " ;
	}
	
}
