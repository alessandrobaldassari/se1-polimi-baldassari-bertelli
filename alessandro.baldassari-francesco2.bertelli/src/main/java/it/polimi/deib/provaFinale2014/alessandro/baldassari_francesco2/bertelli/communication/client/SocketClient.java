package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.SocketServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * Socket based implementation of the Client astraction entity. 
 */
public class SocketClient extends Client 
{
	
	/**
	 * The socket object used to make the connection to the server. 
	 */
	private Socket channel ;
		
	/**
	 * An ObjectInputStream object used to extend the features of the socket's attribute InputStream. 
	 */
	private ObjectInputStream ois ;
	
	/**
	 * An ObjectOutputStream object used to extend the features of the socket's attribute OutputStream.  
	 */
	private ObjectOutputStream oos ;
	
	/**
	 * @param communicationProtocolResponser the value for the communicationProtocolResponser field.
	 */
	public SocketClient ( CommunicationProtocolResponser communicationProtocolResponser )   
	{
		super ( communicationProtocolResponser ) ;
		channel = new Socket () ;
		ois = null ;
		oos = null ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalConnect () throws IOException
	{
		SocketAddress socketAddress ;
		System.out.println ( "SOCKET_CLIENT - TECHNICAL CONNECT : BEGIN" ) ;
		socketAddress = new InetSocketAddress ( SocketServer.SERVER_IP_ADDRESS , SocketServer.TCP_LISTENING_PORT ) ;
		channel.connect ( socketAddress ) ;
		System.out.println ( "SOCKET_CLIENT - TECHNICAL CONNECT : CONNECTION CREATED." ) ;		
		ois = new ObjectInputStream ( channel.getInputStream () ) ;
		oos = new ObjectOutputStream ( channel.getOutputStream () ) ;
		System.out.println ( "SOCKET_CLIENT - TECHNICAL_CONNECT : END." ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalDisconnect () throws IOException 
	{
		ois.close () ;
		oos.close () ;
		channel.close () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected Message read () throws IOException 
	{
		Message res ;
		try 
		{
			System.out.println ( "SOCKET_CLIENT - READ : WAITING FOR A MESSAGE." ) ; 			
			res = (Message) ois.readObject () ;
			System.out.println ( "SOCKET_CLIENT - READ : MESSAGE READ." ) ; 			
		}
		catch (ClassNotFoundException e) 
		{
			throw new IOException ( e ) ;
		}
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void write ( Message m ) throws IOException 
	{
		System.out.println ( "SOCKET_CLIENT - WRITE : WRITING MESSAGE" ) ; 
		oos.writeObject ( m ) ;
		oos.flush () ;
		System.out.println ( "SOCKET_CLIENT - WRITE : MESSAGE WRITTEN" ) ;
	}
	
	protected void operationFinished () throws IOException {}

	
}
