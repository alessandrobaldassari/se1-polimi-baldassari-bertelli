package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

import java.io.Serializable;
import java.util.Collection;


public class Message implements Serializable 
{

	/***/
	private static long IDS = 0 ;
	
	/***/
	private final long uid ;
	
	/***/
	private GameProtocolMessage operation ;
	
	/***/
	private Collection < Serializable > params ;
	
	/***/
	private Message ( long uid , GameProtocolMessage operation , Iterable < Serializable > params )
	{
		if ( uid > 0 && operation != null && params != null )
		{
			this.uid = uid ;
			this.operation = operation ;
			this.params = CollectionsUtilities.newCollectionFromIterable ( params ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public static Message newInstance ( GameProtocolMessage operation , Iterable < Serializable > params ) 
	{
		IDS ++ ;
		return new Message ( IDS , operation , params ) ;
	}
	
	/***/
	public long getUID () 
	{
		return uid ;
	}
	
	/***/
	public GameProtocolMessage getOperation () 
	{
		return operation ;
	}
	
	/***/
	public Iterable < Serializable > getParameters () 
	{
		return params ;
	}
	
	@Override
	public String toString () 
	{
		String res ;
		res = "UID : " + uid + "\nOperation : " + operation + "\nParameters : " + params ;
		return res ;
	}
	
	@Override
	public boolean equals ( Object obj ) 
	{
		Message other ;
		boolean res ;
		if ( obj instanceof Message ) 
		{
			other = ( Message ) obj ;
			res = uid == other.getUID () ;
		}
		else
			res = false ;
		return res ;
	}

}
