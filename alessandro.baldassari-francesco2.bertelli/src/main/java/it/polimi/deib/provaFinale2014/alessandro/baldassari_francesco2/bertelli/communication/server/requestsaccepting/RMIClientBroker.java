package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is the Protocol Component shared by an RMIClientHandler and an RMIClient.
 * Because this is a shared object between these two actors, they can pass the messages they need to
 * exchange ( and the relative parameters ) using this object, so it offers methods for these reasons. 
 */
public interface RMIClientBroker extends Remote , Serializable
{
	
	/**
	 * Returns the registry RMI name of this RMIClientBroker.
	 * 
	 * @return the registry RMI name of this RMIClientBroker.
	 */
	public abstract String getRMIName () throws RemoteException ;
	
	/**
	 * Allow to retrieve the Message stored in this broker. 
	 * 
	 * @return the Message stored in this broker, null if there are none.
	 * @throws RemoteException if something goes wrong with the communication
	 */
	public abstract Message getNextMessage () throws RemoteException ;
	
	/**
	 * Add a new message to this Broker.
	 * 
	 * @param m the message to add.
	 * @throws RemoteException if something goes wrong with the communication
	 */
	public abstract void putNextMessage ( Message m ) throws RemoteException , AnotherCommandYetRunningException ;
	
	/**
	 * Allows a Server to tell the System that he has finished his current communication.
	 * 
	 * @throws RemoteException if something goes wrong with the communication
	 */
	public abstract void setServerReady () throws RemoteException ;
	
	/**
	 * Tells a Server if it can proceed with his operations on this RMIClientBroker, false else.
	 * 
	 * @return true if a Server may proceed with his operations on this RMIClientBroker, false else.
	 * @throws RemoteException if something goes wrong with the communication
	 */
	public abstract boolean isServerReady () throws RemoteException ;
	
	/**
	 * Allows a Client to tell the System that he has finished his current communication.
	 * 
	 * @throws RemoteException if something goes wrong with the communication
	 */
	public abstract void setClientReady () throws RemoteException ;
	
	/**
	 * Tells a Client if it can proceed with his operations on this RMIClientBroker, false else.
	 * 
	 * @return true if a Client may proceed with his operations on this RMIClientBroker, false else.
	 * @throws RemoteException if something goes wrong with the communication
	 */
	public abstract boolean isClientReady () throws RemoteException ;
	
}

