package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * This class is a Socket implementation of the ClientHandler interface, so it manages the game
 * communication with a Client that uses the Socket technology as its data transfer method. 
 */
public class SocketClientHandler extends ClientHandler implements Serializable
{

	/**
	 * A Socket object that points the Client. 
	 */
	private Socket clientChannel ;
	
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
	public SocketClientHandler ( Socket clientChannel ) throws IOException  
	{ 
		if ( clientChannel != null  )
		{
			this.clientChannel = clientChannel ;
			oos = new ObjectOutputStream ( clientChannel.getOutputStream () ) ;
			ois = new ObjectInputStream ( clientChannel.getInputStream () ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose () throws IOException  
	{
		oos.close () ;
		ois.close () ;
		clientChannel.close () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected Message read () throws IOException 
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
	public void write ( Message m ) throws IOException
	{
		oos.writeObject ( m ) ;
		oos.flush () ;
	}

}

