package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import java.io.Serializable;

public class GUINotificationMessage implements Serializable
{

	private String actionAssociated ;

	public GUINotificationMessage ( String actionAssociated ) 
	{
		this.actionAssociated = actionAssociated ;
	}

	public String getActionAssociated () 
	{
		return actionAssociated ;
	}
	
}
