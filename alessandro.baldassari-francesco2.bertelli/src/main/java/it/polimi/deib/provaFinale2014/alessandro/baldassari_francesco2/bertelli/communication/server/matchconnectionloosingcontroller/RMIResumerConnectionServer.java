package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.RMIClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;

import java.rmi.Remote;

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

class RMIResumerConnectionServerImpl extends ResumeConnectionServer < RMIClientBroker , RMIClient > implements RMIResumerConnectionServer {

	@Override
	protected void resumeAlgo ( RMIClient c ) 
	{
		
	}

	@Override
	public void resumeMe ( RMIClient c ) 
	{
		tryResumeMe ( c ) ;
	}
	
	class RMIResumerConnectionServerImplSpec 
	{
		
		
	}
	
}