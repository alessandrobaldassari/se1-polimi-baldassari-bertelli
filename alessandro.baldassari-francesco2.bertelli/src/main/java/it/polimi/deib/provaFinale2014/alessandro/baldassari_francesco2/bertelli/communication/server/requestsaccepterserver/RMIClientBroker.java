package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This interface is the Protocol Component shared by an RMIClientHandler and an RMIClient.
 * Because this is a shared object between these two actors, they can pass the messages they need to
 * exchange ( and the relative parameters ) using this object, so it offers methods for these reasons. 
 */
public interface RMIClientBroker extends Remote
{

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
	
	/**
	 * Class that models the situation where someone tries to add a Command to this RMIClientBroker
	 * while another is still being processed. 
	 */
	public class AnotherCommandYetRunningException extends Exception
	{
		
		/***/
		protected AnotherCommandYetRunningException () 
		{
			super () ;
		}
		
	}
	
}

/**
 * Standard implementation of the RMIClientBroker interface.
 * Thread safe. 
 */
class RMIClientBrokerImpl implements RMIClientBroker 
{
	
	/**
	 * The message the users of the object are exchanging.
	 */
	private Message message ;
	
	/**
	 * A flag indicating who between the client and the server may pass. 
	 */
	private AtomicBoolean flag ;
	
	/***/
	RMIClientBrokerImpl () 
	{
		message = null ;
		flag = new AtomicBoolean ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized Message getNextMessage () 
	{
		Message res ; 
		res = message ;
		message = null ;
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void putNextMessage ( Message message ) 
	{
		this.message = message ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void setServerReady () throws RemoteException 
	{
		flag.set ( true ) ; 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public boolean isServerReady () throws RemoteException 
	{
		return flag.get () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void setClientReady () throws RemoteException 
	{
		flag.set ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized boolean isClientReady() throws RemoteException 
	{
		return ! flag.get () ;
	}

}