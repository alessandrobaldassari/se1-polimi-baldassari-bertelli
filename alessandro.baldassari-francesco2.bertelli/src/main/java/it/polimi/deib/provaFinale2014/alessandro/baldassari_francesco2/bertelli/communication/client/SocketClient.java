package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.SocketProtocolAction;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NetworkUtilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketClient implements Client , Runnable
{

	private static final String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	private static final int SERVER_PORT_NUMBER = 3333 ;
	
	private Socket channel ;
		
	private ObjectInputStream ois ;
	
	private ObjectOutputStream oos ;
	
	private boolean inFunction ;
	
	public SocketClient () throws IOException  
	{
		channel = new Socket () ;
		ois = null ;
		oos = null ;
		inFunction = false ;
	}
	
	/***/
	public void technicalConnect () throws UnknownHostException , IOException
	{
		InetAddress inetAddress ;
		SocketAddress socketAddress ;
		inetAddress = InetAddress.getByAddress ( NetworkUtilities.fromStringToByteArrayIPAddress ( SERVER_IP_ADDRESS ) ) ;
		socketAddress = new InetSocketAddress ( inetAddress , SERVER_PORT_NUMBER ) ;
		channel.connect ( socketAddress ) ;
		ois = new ObjectInputStream ( channel.getInputStream () ) ;
		oos = new ObjectOutputStream ( channel.getOutputStream () ) ;
		System.out.println ( "Socket Client : Connected" + channel.isConnected() ) ;
	}

	/**
	 * 
	 */
	public void run () 
	{
		String command ;
		inFunction = true ;
		while ( inFunction ) 
		{
			try 
			{
				System.out.println ( "Socket Client : Before Receiving Command" ) ;
				command = ois.readUTF () ; 
				System.out.println ( "Sockeet Client : command received" );
				switch ( SocketProtocolAction.valueOf ( command ) ) 
				{
					case NAME_REQUESTING_REQUEST :
						oos.writeUTF ( SocketProtocolAction.NAME_REQUESTING_RESPONSE.toString () );
						oos.flush () ;
						oos.writeUTF ( "Pippo" ) ;
						oos.flush () ;
						System.out.println ( "sent name" ) ;
					break ;
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
}
