package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;
import java.rmi.RemoteException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;

/**
 * RMI-based implementation of the ClientHandler interface. 
 */
public class RMIClientHandler extends ClientHandler < RMIClientBroker >
{
	
	/**
	 * The prefix name each RMIClientHandler will have. 
	 */
	public static final String LOGICAL_ABSTRACT_RMI_CLIENT_HANDLER = "SHEEPLAND_RMI_CLIENT_HANDLER_#_" ;
	
	/**
	 * @param rmiClientBroker the value for the rmiClientBroker field
	 * @throws IOException 
	 * @throws IllegalArgumentException if the parameter is null. 
	 */
	public RMIClientHandler ( ClientHandlerConnector < RMIClientBroker > rmiClientBroker ) throws IOException 
	{
		super ( rmiClientBroker ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalRebinding () {}
	
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
		while ( getConnector().isClientReady () == false ) ;
		m = getConnector().getNextMessage () ;
		return m ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void write ( Message m ) throws IOException
	{
		try
		{
			while ( getConnector().isClientReady () == false ) ;
			getConnector().putNextMessage ( m ) ;
			getConnector().setServerReady () ;
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
