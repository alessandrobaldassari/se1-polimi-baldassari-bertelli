package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements ClientHandler 
{

	private Socket clientChannel ;
	
	private ObjectInputStream ois ;
	
	private ObjectOutputStream oos ;
	
	public SocketClientHandler ( Socket clientChannel ) throws IOException  
	{ 
		if ( clientChannel != null )
		{
			this.clientChannel = clientChannel ;
			oos = new ObjectOutputStream ( clientChannel.getOutputStream () ) ;
			ois = new ObjectInputStream ( clientChannel.getInputStream () ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public String requestName () throws IOException  
	{
		String res ;
		oos.writeUTF ( SocketProtocolAction.NAME_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		System.out.println ( "Socket Client Handler : Name Request Sent" ) ;
		res = ois.readUTF () ;
		if ( SocketProtocolAction.valueOf ( res ) == SocketProtocolAction.NAME_REQUESTING_RESPONSE )
		{
			res = ois.readUTF () ;
		}
		else
		{
			// ERROR MANAGEMENT STRATEGY
		}
		return res ;
	}
	
	public void dispose () throws IOException  
	{
		oos.close () ;
		ois.close () ;
		clientChannel.close () ;
	}
	
}
