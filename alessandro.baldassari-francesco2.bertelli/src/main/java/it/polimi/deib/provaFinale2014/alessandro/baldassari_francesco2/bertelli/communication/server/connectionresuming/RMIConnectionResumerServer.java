package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.connectionresuming;

import java.rmi.Remote;

/***/
public interface RMIConnectionResumerServer extends Remote 
{

	/***/
	public static final String LOGICAL_SERVER_NAME = "SHEEPLAND_CONNECTION_RESUMER_RMI_SERVER" ;
	
	/***/
	public void resumeMe ( int uid ) ;
	
	/***/
	public String areYouReadyForMe ( int uid ) ;
	
}

