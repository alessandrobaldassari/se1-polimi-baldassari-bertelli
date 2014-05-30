package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.Serializable;

public class Message implements Serializable 
{

	private ClientHandlerClientCommunicationProtocolOperation operation ;
	
	private Iterable < Serializable > params ;
	
	private Message ( ClientHandlerClientCommunicationProtocolOperation operation , Iterable < Serializable > params )
	{
		if ( operation != null && params != null )
		{
			this.operation = operation ;
			this.params = params ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public static Message newInstance ( ClientHandlerClientCommunicationProtocolOperation operation , Iterable < Serializable > params ) 
	{
		return new Message ( operation , params ) ;
	}
	
	public ClientHandlerClientCommunicationProtocolOperation getOperation () 
	{
		return operation ;
	}
	
	public Iterable < Serializable > getParameters () 
	{
		return params ;
	}
	
}
