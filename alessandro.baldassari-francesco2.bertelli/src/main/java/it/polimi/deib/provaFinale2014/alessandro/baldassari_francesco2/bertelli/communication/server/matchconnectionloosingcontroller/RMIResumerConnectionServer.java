package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.RMIClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBrokerImpl;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/***/
public interface RMIResumerConnectionServer extends Remote 
{

	/***/
	public static final String LOGICAL_SERVER_NAME = "SHEEPLAND_CONNECTION_RESUMER_RMI_SERVER" ;
	
	/***/
	public static final int PORT = 3332 ;
	
	/***/
	public void resumeMe ( RMIClient c ) ;
	
}

/***/
class RMIResumerConnectionServerImpl extends ResumeConnectionServer < RMIClientBroker , RMIClient > implements RMIResumerConnectionServer {

	private Registry registry ;
	
	public RMIResumerConnectionServerImpl () throws RemoteException 
	{
		registry = LocateRegistry.createRegistry ( PORT ) ;
	}
	
	@Override
	protected void resumeAlgo ( Couple < RMIClient , RMIClientBroker > req  ) 
	{
		
	}

	@Override
	public void resumeMe ( RMIClient c ) 
	{
		RMIClientBroker newBrok ;
		newBrok = new RMIClientBrokerImpl () ;
		tryResumeMe ( new Couple < RMIClient , RMIClientBroker > ( c , newBrok ) ) ;
	}
	
	class RMIResumerConnectionServerImplSpec 
	{
		
		
	}
	
}