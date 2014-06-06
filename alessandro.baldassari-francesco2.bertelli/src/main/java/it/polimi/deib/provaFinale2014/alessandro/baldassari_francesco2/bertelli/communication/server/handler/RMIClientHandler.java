package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;
import java.rmi.RemoteException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;

/**
 * RMI-based implementation of the ClientHandler interface. 
 */
public class RMIClientHandler extends ClientHandler 
{
	
	/**
	 * The prefix name each RMIClientHandler will have. 
	 */
	public static final String LOGICAL_ABSTRACT_RMI_CLIENT_HANDLER = "SHEEPLAND_RMI_CLIENT_HANDLER_#_" ;
	
	/**
	 * A RMIClientBroker object this Handler will use to synchronize with the Client. 
	 */
	private RMIClientBroker rmiClientBroker;
	
	/**
	 * @param rmiClientBroker the value for the rmiClientBroker field
	 * @throws IllegalArgumentException if the parameter is null. 
	 */
	public RMIClientHandler ( RMIClientBroker rmiClientBroker ) 
	{
		if ( rmiClientBroker != null )
			this.rmiClientBroker = rmiClientBroker ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose() throws IOException {}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected Message read () throws IOException 
	{
		Message m ;
		while ( rmiClientBroker.isClientReady () == false ) ;
		m = rmiClientBroker.getNextMessage () ;
		return m ;
	}
	
	@Override
	public void write ( Message m ) throws IOException
	{
		try
		{
			while ( rmiClientBroker.isClientReady () == false ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			rmiClientBroker.setServerReady () ;
		}
		catch ( RemoteException e ) 
		{
			throw new IOException ( e ) ;
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			throw new IOException ( e ) ;
		}
	}
	
}
