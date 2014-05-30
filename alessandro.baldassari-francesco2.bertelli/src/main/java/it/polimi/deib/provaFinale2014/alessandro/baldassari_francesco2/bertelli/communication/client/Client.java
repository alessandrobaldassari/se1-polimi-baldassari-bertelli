package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import java.io.IOException;

/**
 * This class implements the communication end-point by the Client side.
 * It's implementation technically relies on an infinite loop executing in the run method of this
 * class where subclasses has the chance to implement their server-listening logic, server-sending-messages
 * logic and so on.
 */
public abstract class Client extends Thread
{
	
	/**
	 * Boolean flag indicating if the connections pointing to the server are opened. 
	 */
	private boolean technicallyOn ;
	
	/**
	 * A CommunicationProtocolResponser to retrieve the data to send to the Server. 
	 */
	private CommunicationProtocolResponser dataPicker ;
	
	/**
	 * @param dataPicker the value for the dataPicker field.
	 * @throws IllegalArgumentException if the dataPicker field is null.
	 */
	Client ( CommunicationProtocolResponser dataPicker )
	{
		if ( dataPicker != null )
		{
			this.dataPicker = dataPicker ;
			technicallyOn = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * This method is the one this class' users has to call to initialize the connections
	 * used by this client.
	 * It also enable the server part of this method to start.
	 * 
	 * @throws IOException if something goes wrong with this method.
	 */
	public void openConnection () throws IOException  
	{
		technicalConnect () ;
		technicallyOn = true ;
	}
	
	/**
	 * This method is the one this class's users has to call in order to close the connections 
	 * used by this client.
	 * This will probably stop the Thread from its running, also the behavior depends also by
	 * the subclasses implementation.
	 */
	public void closeConnection () throws IOException 
	{
		technicalDisconnect () ;
		technicallyOn = false ; 
	}
	
	/**
	 * This method represents the one where a Client has to implement the mechanism which open all
	 * the connection to the server
	 * It's not a logic related method, just a technical one.
	 * 
	 * @throws IOException if something goes wrong with the communication operations.
	 */
	protected abstract void technicalConnect () throws IOException ;
		
	/**
	 * This method represents the one where a Client has to implement the mechanism which closes
	 * all the connection to the server.
	 * It's not a logic related method, just a technical one.  
	 */
	protected abstract void technicalDisconnect () throws IOException ;
	
	/**
	 * The effective protocol implementation method.
	 * Here subclasses have to implement their logic related to the with-the-server communication. 
	 */
	protected abstract void communicationProtocolImpl () ;
	
	/**
	 * Getter method for the dataPicker property.
	 * 
	 * @return the dataPicker property.
	 */
	protected CommunicationProtocolResponser getDataPicker () 
	{
		return dataPicker ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * This methods do things in order for this Thread to start only if the technicallyOn parameter
	 * is true, so only if the openConnection method has been called before. 
	 */
	@Override
	public void start () 
	{
		if ( technicallyOn )
			super.start () ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * For every time the value of the technicallyOn attribute is true, it continues to call
	 * the communicationProtocolImpl method.
	 */
	@Override
	public void run () 
	{
		while ( technicallyOn )
			communicationProtocolImpl () ;
	}
	
}
