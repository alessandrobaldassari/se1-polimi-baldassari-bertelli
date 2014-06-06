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
	private ClientCommunicationProtocolMessage operation ;
	
	/***/
	private Collection < Serializable > params ;
	
	/***/
	private Message ( long uid , ClientCommunicationProtocolMessage operation , Iterable < Serializable > params )
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
	public static Message newInstance ( ClientCommunicationProtocolMessage operation , Iterable < Serializable > params ) 
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
	public ClientCommunicationProtocolMessage getOperation () 
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

	/*
	@Override
	public void writeExternal ( ObjectOutput out ) throws IOException 
	{
		out.writeLong ( uid ) ;
		out.writeUTF ( operation.name() ) ;
		out.writeInt ( params.size () ) ;
		for ( Serializable s : params )
			out.writeObject ( s ) ;
	}

	@Override
	public void readExternal ( ObjectInput in ) throws IOException , ClassNotFoundException 
	{
		uid = in.readLong () ;
		operation = ClientCommunicationProtocolMessage.valueOf ( in.readUTF () ) ;
		
		
	}
	*/
}
