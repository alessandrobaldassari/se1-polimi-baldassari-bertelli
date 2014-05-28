package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;


public interface RMIClientBroker 
{

	public ClientHandlerClientCommunicationProtocolOperation getNextCommand () ;
	
	public Object getNextParameter () ;
	
	public void putNextCommand ( ClientHandlerClientCommunicationProtocolOperation op ) ;
	
	public void putNextParameter () ;
	
	public void setClientOperationEnded () ;
	
	public boolean isClientOperationEnded () ;
	
}