package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerClientCommunicationProtocolOperation;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;

/**
 * RMI-based implementation of the Client abstraction entity. 
 */
public class RMIClient extends Client 
{

	/**
	 * The IP address where locate the Server. 
	 */
	private final static String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The port where contact the Server. 
	 */
	private final static int SERVER_PORT = 3334 ;

	/**
	 * A broker object to interact with the Server. 
	 */
	private RMIClientBroker clientBroker ;
	
	/**
	 * @param communicationProtocolResponser the value for the communicationProtocolResponser field.
	 */
	public RMIClient ( CommunicationProtocolResponser communicationProtocolResponser ) 
	{
		super ( communicationProtocolResponser ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalConnect() throws IOException 
	{
		RMIServer server ;
		String key ;
		Registry registry ;
		registry = LocateRegistry.getRegistry ( SERVER_IP_ADDRESS , SERVER_PORT ) ;
		try 
		{
			System.out.println ( "RMI_CLIENT : BEGIN TECHNICAL CONNECT " ) ;
			server = ( RMIServer ) registry.lookup ( RMIServer.LOGICAL_SERVER_NAME ) ;
			key = server.addPlayer () ; 
			clientBroker = ( RMIClientBroker ) registry.lookup ( key ) ;
			System.out.println ( "RMI_CLIENT : END TECHNICAL CONNECT" ) ;
		} 
		catch ( NotBoundException e ) 
		{
			e.printStackTrace();
		}
	}	
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalDisconnect () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void communicationProtocolImpl () 
	{
		Collection < Serializable > c ;
		ClientHandlerClientCommunicationProtocolOperation nextOperation ;
		String s ;
		Message m ;
		try 
		{
			c = new ArrayList < Serializable > ( 2 ) ;
			System.out.println ( "RMIClient : waiting for some commands " + clientBroker.isServerReady () ) ;
			while ( clientBroker.isServerReady () == false ) ;
			System.out.println ( "cliennt passed" ) ;
			m = clientBroker.getNextMessage () ;
			switch ( m.getOperation () )
			{
				case NAME_REQUESTING_REQUEST :
					s = getDataPicker ().onNameRequest () ;
					c.add  ( s ) ;
					m = Message.newInstance ( ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_RESPONSE , c ) ;
					clientBroker.putNextMessage ( m ) ;
					clientBroker.setClientReady () ;
				break ;	
				case SHEPERD_COLOR_REQUESTING_REQUEST:
				
				break;
				case MATCH_WILL_NOT_START_NOTIFICATION:
				break;
				case GENERIC_NOTIFICATION_NOTIFICATION:
				break;
				case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
					
				break ;
				case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
				break ;
				case CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST :
				break ;
				case DO_MOVE_REQUESTING_REQUEST :
				break ;
				default :
				break ;
			}
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AnotherCommandYetRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}