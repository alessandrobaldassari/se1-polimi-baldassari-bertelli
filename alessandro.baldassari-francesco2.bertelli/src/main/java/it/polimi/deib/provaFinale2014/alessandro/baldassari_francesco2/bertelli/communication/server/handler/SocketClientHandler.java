package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is a Socket implementation of the ClientHandler interface, so it manages the game
 * communication with a Client that uses the Socket technology as its data transfer method. 
 */
public class SocketClientHandler extends ClientHandler < Socket > 
{

	/**
	 * An ObjectInputStream object used to extend the features of the socket's attribute InputStream. 
	 */
	private ObjectInputStream ois ;
	
	/**
	 * An ObjectOutputStream object used to extend the features of the socket's attribute OutputStream. 
	 */
	private ObjectOutputStream oos ;
	
	/**
	 * @param clientChannel a Socket object that points to the client.
	 * @throws IllegalArgumentException if the clientChannel parameter is null 
	 */
	public SocketClientHandler ( ClientHandlerConnector < Socket > conn ) throws IOException  
	{ 
		super ( conn ) ;
	}
	
	protected void technicalRebinding () throws IOException 
	{
		oos = new ObjectOutputStream ( getConnector ().getOutputStream () ) ;
		ois = new ObjectInputStream ( getConnector ().getInputStream () ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose () throws IOException  
	{
		ois.close () ;
		oos.close () ;
		getConnector().close () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected synchronized Message read () throws IOException 
	{
		Message m ;
		try 
		{
			m = ( Message ) ois.readObject () ;
		}
		catch ( ClassNotFoundException e ) 
		{
			throw new IOException ( e ) ;
		}
		return m ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public synchronized void write ( Message m ) throws IOException
	{
		System.out.println ( "SOCKET_CLIENT_HANDLER - WRITE : DATA TO WRITE : " + m ) ;
		oos.writeObject ( m ) ;
		oos.flush () ;
		oos.reset();
		System.out.println ( "SOCKET_CLIENT_HANDLER - WRITE : DATA WRITTEN " ) ;
	}

}

