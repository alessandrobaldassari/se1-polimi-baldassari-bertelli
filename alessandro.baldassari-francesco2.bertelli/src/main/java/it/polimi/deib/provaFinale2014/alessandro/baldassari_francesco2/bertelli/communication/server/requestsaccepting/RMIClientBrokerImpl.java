package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.UIDGenerator;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Standard implementation of the RMIClientBroker interface.
 * Thread safe. 
 */
public class RMIClientBrokerImpl implements RMIClientBroker 
{
	
	/**
	 * The prefix of the name of every object of this class. 
	 */
	public static final String OBJECT_BASE_NAME = "RMI_CLIENT_BROKER_IMPL_#" ;
	
	/**
	 * A UIDGenerator object to ensure every RMIClientBrokerImpl instance has a different name. 
	 */
	private static UIDGenerator uidGenerator ;
	
	/**
	 * The RMI name of this RMIClientBroker. 
	 */
	private final String rmiName ;
	
	/**
	 * The message the users of the object are exchanging.
	 */
	private Message message ;
	
	/**
	 * A flag indicating who between the client and the server may pass. 
	 */
	private AtomicBoolean flag ;
	
	/***/
	public RMIClientBrokerImpl () 
	{
		if ( uidGenerator == null )
			uidGenerator = new UIDGenerator ( 0L ) ;
		rmiName = OBJECT_BASE_NAME + uidGenerator.generateNewValue () ;
		message = null ;
		flag = new AtomicBoolean ( false ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String getRMIName () 
	{
		return rmiName ;
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

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String toString () 
	{
		return "Class : RMIClientBrokerImpl" ;
	}
	
}