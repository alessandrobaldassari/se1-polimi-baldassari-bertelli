package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.connectionresuming;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerConnector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.SuspendedClientHandlerBuffer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting.RMIClientBrokerImpl;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/***/
public class RMIConnectionResumerServerImpl extends ConnectionResumerServer < RMIClientBroker > implements RMIConnectionResumerServer 
{

	/***/
	private static int lastNoEmitted = 0 ; ;
	
	/***/
	private static String NAME = "RMI_EMERGENCY" ;
	
	/***/
	private Registry registry ;
	
	/***/
	private Map < Integer , String > newNames ;
	
	/***/
	public RMIConnectionResumerServerImpl ( SuspendedClientHandlerBuffer suspendedClientHandlerBuffer )  
	{
		super ( suspendedClientHandlerBuffer ) ;
		newNames = new HashMap < Integer , String > () ; 
	}
	
	@Override
	public void connect () throws RemoteException 
	{
		registry = LocateRegistry.createRegistry ( PORT ) ;		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void resumeAlgo ( Couple < Integer , RMIClientBroker > req  ) throws IOException
	{
		RMIClientBroker stub ;
		ClientHandler < RMIClientBroker > handler ;
		handler = getHandler ( req.getFirstObject() ) ;
		if ( handler != null )
		{
			try 
			{
				lastNoEmitted ++ ;
				stub = (RMIClientBroker) UnicastRemoteObject.exportObject ( req.getSecondObject () , 0 ) ;
				registry.bind ( NAME + lastNoEmitted , stub );
				synchronized ( newNames )
				{
					newNames.put ( req.getFirstObject() , NAME + lastNoEmitted ) ;
				}
				handler.rebind ( new ClientHandlerConnector < RMIClientBroker > ( req.getSecondObject() ) ) ;
				handler.sendResumeSucceedNotification () ;
			}
			catch (AlreadyBoundException e) {
				newNames.put ( req.getFirstObject () , "KO" ) ;
				e.printStackTrace();
				throw new IOException ( e ) ;
			}
			
		}
		else
			newNames.put ( req.getFirstObject () , "KO" ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void resumeMe ( int uid ) 
	{
		RMIClientBroker newBrok ;
		newBrok = new RMIClientBrokerImpl () ;
		newNames.put ( uid , null ) ;
		tryResumeMe ( new Couple < Integer , RMIClientBroker > ( uid , newBrok ) ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized String areYouReadyForMe ( int uid ) 
	{
		return newNames.get ( uid ) ;
	}
	
}