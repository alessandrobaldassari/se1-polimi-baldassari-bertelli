package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlauncherserver.MatchPlayerAdder;

/**
 * This class represents a front end request accepter server, a component whose function is
 * to accept request from Users ( using a given technology ), create Handlers for them and then
 * pass these Handlers to the MatchAdderCommunicationController that will use them.
 */
public abstract class RequestAccepterServer implements Runnable
{

	/**
	 * The object used by this Server to communicate that a new Client has come 
	 */
	private final MatchPlayerAdder matchAdderCommunicationController ;
	
	/**
	 * A boolean flag indicating if this Server is on. 
	 */
	private boolean inFunction ;
	
	/**
	 * @param matchAdderCommunicationController the value for the matchAdderCommunicationController field.
	 * @throws IllegalArgumentException if the matchAdderCommunicationController parameter is null. 
	 */
	protected RequestAccepterServer ( MatchPlayerAdder matchAdderCommunicationController )
	{
		if ( matchAdderCommunicationController != null )
		{
			this.matchAdderCommunicationController = matchAdderCommunicationController ;
			inFunction = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Generates and returns a RequestAccepterServer which implements the Socket-based Protocol. 
	 * 
	 * @param matchAdderCommunicationController the matchAdderCommunicationController field value.
	 * @return the created Server.
	 * @throws IOException if something goes wrong with the creation of the object.
	 */
	public static RequestAccepterServer newSocketServer ( MatchPlayerAdder matchAdderCommunicationController ) throws IOException 
	{
		return new SocketRequestAcceptServer ( matchAdderCommunicationController ) ;
	}
	
	/**
	 * Generates and returns a RequestAccepterServer which implements the RMI-based Protocol.
	 * 
	 * @param matchAdderCommunicationController the matchAdderCommunicationController field value.
	 * @param localhostAddress the IP address of the localhost where the created Server will be deployed.
	 * @param registryPort the port where the RMI Registry associated with the created Server will be listening
	 * @return the created Server.
	 * @throws IOException if something goes wrong with the creation of the object.
	 */
	public static RequestAccepterServer newRMIServer ( MatchPlayerAdder matchAdderCommunicationController , String localhostAddress , int registryPort ) throws IOException
	{
		return new RMIRequestAcceptServerImpl ( matchAdderCommunicationController , localhostAddress , registryPort ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * It gives subclasses a change to initialize themselves, then, while the Server is up,
	 * execute the lifeLoopImplementation method ( implemented in subclasses ). 
	 */
	@Override
	public void run () 
	{
		try 
		{
			technicalStart () ;
			inFunction = true ;
			while ( inFunction )
				lifeLoopImplementation () ;
			shutDown () ;
		}
		catch ( IOException e ) 
		{
			System.out.println ( "REQUEST_ACCEPT_SERVER : IOEXCEPTION DURING CREATION PHASE." ) ;
		}
	}
	
	/**
	 * Subclasses have to define this method in order to implement their strategy to accept
	 * inbound requests. 
	 */
	protected abstract void acceptRequest () ;
	
	/**
	 * Gives subclasses a change to pass a created ClientHandler to the MatchAdderCommunicationController
	 * associated with this Server.
	 */
	protected void submitToMatchAdderCommunicationController ( ClientHandler clientHandler ) 
	{
		matchAdderCommunicationController.addPlayer ( clientHandler ) ;
	}
	
	/**
	 * Gives subclasses a chance to initialize their technical part before the lifeloop begins.
	 * 
	 * @throws IOException if something goes wrong with this creation phase.
	 */
	protected abstract void technicalStart () throws IOException ;
	
	/**
	 * The method subclasses wants to execute during their lifecycle. 
	 */
	protected abstract void lifeLoopImplementation () ;
	
	/**
	 * Shut down this Server. 
	 */
	public void shutDown () 
	{
		inFunction = false ;
	}
	
}
