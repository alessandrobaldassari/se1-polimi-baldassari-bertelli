package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.AnotherCommandYetRunningException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.net.RMIObjectUnbinder;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.net.RMIObjectUnbinder.UnbindingException;

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
	 * An RMIObjectUnbinder to implement the dispose functionality. 
	 */
	private RMIObjectUnbinder unbinder ;
	
	/**
	 * @param rmiClientBroker the value for the rmiClientBroker field
	 * @throws IOException 
	 * @throws IllegalArgumentException if the unbinder parameter is null. 
	 */
	public RMIClientHandler ( ClientHandlerConnector < RMIClientBroker > rmiClientBroker , RMIObjectUnbinder unbinder ) throws IOException 
	{
		super ( rmiClientBroker ) ;
		if ( unbinder != null )
			this.unbinder = unbinder ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 * Nothing to do in this implementation
	 */
	@Override
	protected void technicalRebinding () 
	{
		Logger.getGlobal().log ( Level.INFO , "RMIClientHandler - technicalRebinding called." );
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose() throws IOException 
	{
		try 
		{
			unbinder.unbind ( super.getConnector ().getRMIName () );
		}
		catch (UnbindingException e) 
		{
			throw new IOException ( "FROM : RMI_CLIENT_HANDLER - DISPOSE" , e ) ;
		}
	}
	
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
