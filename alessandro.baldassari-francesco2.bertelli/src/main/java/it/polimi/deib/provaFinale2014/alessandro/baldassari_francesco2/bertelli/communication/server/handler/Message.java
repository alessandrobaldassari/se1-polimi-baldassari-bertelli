package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.Serializable;

public class Message implements Serializable 
{

	private ClientCommunicationProtocolMessage operation ;
	
	private Iterable < Serializable > params ;
	
	private Message ( ClientCommunicationProtocolMessage operation , Iterable < Serializable > params )
	{
		if ( operation != null && params != null )
		{
			this.operation = operation ;
			this.params = params ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public static Message newInstance ( ClientCommunicationProtocolMessage operation , Iterable < Serializable > params ) 
	{
		return new Message ( operation , params ) ;
	}
	
	public ClientCommunicationProtocolMessage getOperation () 
	{
		return operation ;
	}
	
	public Iterable < Serializable > getParameters () 
	{
		return params ;
	}
	
}
