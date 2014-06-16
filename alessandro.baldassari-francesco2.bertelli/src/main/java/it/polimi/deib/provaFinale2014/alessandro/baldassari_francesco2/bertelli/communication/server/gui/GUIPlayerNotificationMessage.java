package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import java.io.Serializable;

public class GUIPlayerNotificationMessage extends GUINotificationMessage 
{

	private Serializable firstParam ;
	
	private Serializable secondParam ;
	
	public GUIPlayerNotificationMessage ( String actionAssociated , Serializable firstParam , Serializable secondParam ) 
	{
		super(actionAssociated);
		this.firstParam = firstParam ;
		this.secondParam = secondParam ;
	}

	public Serializable getFirstParam () 
	{
		return firstParam ;
	}
	
	public Serializable getSecondParam () 
	{
		return secondParam ;
	}
	
}
