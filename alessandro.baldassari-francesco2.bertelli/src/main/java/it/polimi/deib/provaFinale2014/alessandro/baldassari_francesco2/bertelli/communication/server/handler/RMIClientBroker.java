package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * This interface is the Protocol Component shared by an RMIClientHandler and an RMIClient.
 * Because this is a shared object between these two actors, they can pass the messages they need to
 * exchange ( and the relative parameters ) using this object, so it offers methods for these reasons. 
 */
public interface RMIClientBroker extends Remote
{

	/**
	 * Allow to retrieve the command stored in this broker. 
	 * 
	 * @return the command stored in this broker, null if there are none.
	 */
	public ClientHandlerClientCommunicationProtocolOperation getNextCommand () ;
	
	/**
	 * Allow to retrieve the oldest parameter stored in this broker.
	 * 
	 * @return the oldest parameter stored in this broker.
	 */
	public Serializable getNextParameter () ;
	
	/**
	 * Add a new parameter to the communications parameter queue.
	 * 
	 * @param op the parameter being added.
	 */
	public void putNextCommand ( ClientHandlerClientCommunicationProtocolOperation op ) throws AnotherCommandYetRunningException ;
	
	/***/
	public void putNextParameter ( Serializable parameter ) ;
	
	/***/
	public void setServerReady ( boolean serverReady ) ;
	
	/***/
	public boolean isServerReady () ;
	
	/***/
	public void setClientReady ( boolean clientReady ) ;
	
	/***/
	public boolean isClientReady () ;
	
	/***/
	public class AnotherCommandYetRunningException extends Exception
	{
		
		protected AnotherCommandYetRunningException () 
		{
			super () ;
		}
		
	}
	
}