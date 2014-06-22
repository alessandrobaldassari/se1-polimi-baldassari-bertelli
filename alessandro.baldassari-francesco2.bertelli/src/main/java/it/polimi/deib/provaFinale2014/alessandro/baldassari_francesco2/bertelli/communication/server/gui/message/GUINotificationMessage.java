package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;

import java.io.Serializable;

/***/
public class GUINotificationMessage implements Serializable , Identifiable
{

	private static int uidGen = 0 ;
	
	private final int uid ;
	
	/***/
	private String actionAssociated ;

	/***/
	public GUINotificationMessage ( String actionAssociated ) 
	{
		if ( actionAssociated != null )
		{
			uidGen ++ ;
			uid = uidGen ;
			this.actionAssociated = actionAssociated ;
		}
		else
			throw new IllegalArgumentException();
	}

	/**
	 * Getter method for the actionAssociated field.
	 * 
	 * @return the actionAssociated field.
	 */
	public String getActionAssociated () 
	{
		return actionAssociated ;
	}

	@Override
	public int getUID() 
	{
		return uid ;
	}
	
	@Override
	public boolean equals ( Object obj ) 
	{
		boolean res ;
		GUINotificationMessage other ;
		if ( obj instanceof GUINotificationMessage )
		{
			other = ( GUINotificationMessage ) obj ;
			res = uid == other.getUID();
		}
		else
			res = false ;
		return res ;
	}
	
	
}
