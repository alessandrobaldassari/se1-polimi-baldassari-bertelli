package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

/**
 * Class that models the situation where someone tries to add a Command to this RMIClientBroker
 * while another is still being processed. 
 */
public class AnotherCommandYetRunningException extends Exception 
{
	
	/***/
	protected AnotherCommandYetRunningException () 
	{
		super () ;
	}
	
}