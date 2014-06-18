package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;

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
	
	/***/
	public static final String SERVER_IP_ADDR = "127.0.0.1" ;
	
	/***/
	public static final int SERVER_PORT = 3333 ;
	
	/***/
	public static final int EMERGENCY_SERVER_PORT = 3331 ;
	
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
	public void directTechnicalConnect () throws IOException
	{
		SocketAddress socketAddress ;
		System.out.println ( "SOCKET_CLIENT - TECHNICAL CONNECT : BEGIN" ) ;
		socketAddress = new InetSocketAddress ( SERVER_IP_ADDR , SERVER_PORT ) ;
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
	protected synchronized Message read () throws IOException 
	{
		Object temp ;
		Message res ;
		try 
		{
			System.out.println ( "SOCKET_CLIENT - READ : WAITING FOR A MESSAGE." ) ; 			
			temp = ois.readObject() ;
			System.out.println ( "SOCKET_CLIENT - READ : RAW OBJECT READ : " + temp ) ;
			res = ( Message ) temp ;
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
	protected synchronized void write ( Message m ) throws IOException 
	{
		System.out.println ( "SOCKET_CLIENT - WRITE : WRITING MESSAGE : " + m ) ; 
		oos.writeObject ( m ) ;
		oos.flush () ;
		System.out.println ( "SOCKET_CLIENT - WRITE : MESSAGE WRITTEN" ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void operationFinished () throws IOException 
	{
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void resumeConnectionConnect () throws IOException 
	{
		Socket emergencySocket ;
		String res ;
		ObjectInputStream emOIS ;
		ObjectOutputStream emOOS ;
		emergencySocket = new Socket ( SERVER_IP_ADDR , EMERGENCY_SERVER_PORT ) ;
		emOIS = new ObjectInputStream ( emergencySocket.getInputStream() ) ;
		emOOS = new ObjectOutputStream ( emergencySocket.getOutputStream () ) ;
		emOOS.writeInt ( getUID () );
		res = emOIS.readUTF () ;
		if ( res.compareTo ( "OK" ) != 0 )
			throw new IOException ( "out." ) ;
		else
		{
			channel = emergencySocket ;
			ois = new ObjectInputStream ( channel.getInputStream() ) ;
			oos = new ObjectOutputStream ( channel.getOutputStream () ) ;
		}
	}

	
}
