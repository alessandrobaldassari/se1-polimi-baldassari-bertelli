package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

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
	public ClientHandlerClientCommunicationProtocolOperation getNextCommand () throws RemoteException ;
	
	/**
	 * Allow to retrieve the oldest parameter stored in this broker.
	 * 
	 * @return the oldest parameter stored in this broker.
	 */
	public Serializable getNextParameter () throws RemoteException ;
	
	/**
	 * Add a new parameter to the communications parameter queue.
	 * 
	 * @param op the parameter being added.
	 */
	public void putNextCommand ( ClientHandlerClientCommunicationProtocolOperation op ) throws RemoteException , AnotherCommandYetRunningException ;
	
	/***/
	public void putNextParameter ( Serializable parameter ) throws RemoteException ;
	
	/***/
	public void setServerReady ( boolean serverReady ) throws RemoteException ;
	
	/***/
	public boolean isServerReady () throws RemoteException ;
	
	/***/
	public void setClientReady ( boolean clientReady ) throws RemoteException ;
	
	/***/
	public boolean isClientReady () throws RemoteException ;
	
	/***/
	public class AnotherCommandYetRunningException extends Exception
	{
		
		protected AnotherCommandYetRunningException () 
		{
			super () ;
		}
		
	}
	
}