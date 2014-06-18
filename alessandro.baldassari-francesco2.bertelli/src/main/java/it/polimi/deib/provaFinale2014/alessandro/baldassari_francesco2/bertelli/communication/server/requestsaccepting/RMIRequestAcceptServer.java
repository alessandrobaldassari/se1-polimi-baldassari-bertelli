package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerConnector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.RMIClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlauncherserver.MatchPlayerAdder;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.net.RMIObjectUnbinder;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This interface defines the behavior of an RMI Server  
 */
public interface RMIRequestAcceptServer extends Remote 
{
	
	/**
	 * The logical name of this RMI Server.
	 */
	public static final String SERVER_NAME = "SHEEPLAND_RMI_REQUEST_ACCEPT_SERVER" ; 
 	
	/**
	 * Allows a Client to request to add himself to a Match.
	 * 
	 * @return a String containing a key that will allow the client to retrieve an object
	 *         he will use to communicate with the Server application.
	 * @throws RemoteException if something goes wrong with the connections.
	 */
	public String addPlayer () throws RemoteException ;
	
}

/**
 * Implementation of the RMI Server interface subclassing the RequestAccepterServer
 */
class RMIRequestAcceptServerImpl extends RequestAccepterServer implements RMIRequestAcceptServer , RMIObjectUnbinder
{

	/**
	 * Sleep time for a dummy-implementation of the lifeLoopImplementation method. 
	 */
	private static final long SLEEP_TIME = 25 * Utilities.MILLISECONDS_PER_SECOND ;

	/**
	 * Registry object to implement the RMI protocol. 
	 */
	private Registry registry ;
	
	/**
	 * The stub of this object to export. 
	 */
	private RMIRequestAcceptServer stub ;

	/**
	 * A Map object to implement the RMI object removal functionality. 
	 */
	private Map < String , RMIClientBroker > brokerStubs ;
	
	/**
	 * @param matchAdderCommunicationController the matchAdderCommunicationController field value.
	 * @param localhostAddress the IP address of the localhost where the created Server will be deployed.
	 * @param registryPort the port where the RMI Registry associated with the created Server will be listening
	 * @throws IllegalArgumentException if the localhostAddress parameter is null or the registryPort
	 *         parameter is < 0
	 */
	protected RMIRequestAcceptServerImpl ( MatchPlayerAdder matchAdderCommunicationController , String localhostAddress , int registryPort ) throws RemoteException 
	{
		super ( matchAdderCommunicationController ) ;
		if ( localhostAddress != null && registryPort >= 0 )
		{
			registry = LocateRegistry.createRegistry ( registryPort ) ;
			brokerStubs = new LinkedHashMap < String , RMIClientBroker > () ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - : REGISTRY LOCATED : " + registry ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String addPlayer () throws RemoteException 
	{
		RMIClientHandler clientHandler ;
		RMIClientBroker stub ;
		RMIClientBroker broker ;
		String res ;
		try 
		{
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : BEGIN_ACCEPT_REQUEST" ) ;
			// initialize the broker.
			broker = new RMIClientBrokerImpl () ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - EXPORT CLIENT BROKER FOR THIS REQUEST" ) ;
			// create a stub for the broker.
			stub = ( RMIClientBroker ) UnicastRemoteObject.exportObject ( broker , 0 ) ;		
			brokerStubs.put ( broker.getRMIName () , stub ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : BROKER IS OUT." ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : CREATING CLIENT HANDLER." ) ;
			// initialize the ClientHandler.
			clientHandler = new RMIClientHandler ( new ClientHandlerConnector < RMIClientBroker > ( broker ) , this ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : HANDLER ON." ) ;
			// inject a new ClientHandler / Player in a Match.
			submitToMatchAdderCommunicationController ( clientHandler )  ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : HANDLER SUBMITTED TO THE CONTROLLER" ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : BINDING THE BROKER" ) ;
			// bind the broker and make it available to name services.
			registry.bind ( broker.getRMIName () , stub ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - ADD_PLAYER : BROKER BOUND." ) ;
			res = broker.getRMIName () ;
		}
		catch ( RemoteException r ) 
		{
			// here probably the stub has not been created.
			res = null ;
			throw new RuntimeException ( r );
		}
		catch ( AlreadyBoundException e ) 
		{
			// this should never happen.
			throw new RuntimeException ( e ) ;
		} 
		catch (IOException e) 
		{
			// problems with the handler
			throw new RuntimeException ( e ) ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void acceptRequest () 
	{
		try 
		{
			addPlayer () ;
		} 
		catch ( RemoteException e ) 
		{
			e.printStackTrace () ;
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalStart () throws IOException
	{
		try 
		{
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - TECHNICAL START : BEGINS" ) ;
			stub = ( RMIRequestAcceptServer ) UnicastRemoteObject.exportObject ( this , 0 ) ;
			System.out.println ( stub ) ;
			registry.bind ( SERVER_NAME , stub ) ;
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - TECHNICAL START : SERVER UP" ) ;
		}
		catch (AlreadyBoundException e) 
		{
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - TECHNICAL_START : NAME ALREADY USED " ) ;
			registry.rebind ( SERVER_NAME , stub ) ;
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void lifeLoopImplementation () 
	{
		try 
		{
			Thread.sleep ( SLEEP_TIME ) ;
		} 
		catch ( InterruptedException e ) 
		{
			System.out.println ( "RMI_REQUEST_ACCEPT_SERVER - LIFE_LOOP_IMPLEMENTATION : INTERRUPTED BY " + Thread.currentThread() ) ;
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void shutDown () 
	{
		if ( stub != null )
			try
			{
				UnicastRemoteObject.unexportObject ( stub , true ) ;
			}
			catch ( NoSuchObjectException e ) {}
		super.shutDown (); 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void unbind ( String rmiName ) throws UnbindingException 
	{
		RMIClientBroker dying ;
		try 
		{
			dying = brokerStubs.get ( rmiName ) ;
			registry.unbind ( rmiName ) ;
			UnicastRemoteObject.unexportObject ( dying , true ) ;
			brokerStubs.remove ( rmiName ) ;
		} 
		catch (AccessException e) 
		{
			throw new UnbindingException ( e , rmiName ) ;
		}
		catch (RemoteException e) 
		{
			throw new UnbindingException ( e , rmiName ) ;
		}
		catch ( NotBoundException e ) 
		{
			throw new UnbindingException ( e , rmiName ) ;
		} 
	}
	
}
