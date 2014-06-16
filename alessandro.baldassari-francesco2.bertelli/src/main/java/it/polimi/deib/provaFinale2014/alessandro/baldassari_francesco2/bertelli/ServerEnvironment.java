package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerEnvironment 
{

	public static final int SOCKET_REQUEST_ACCEPT_SERVER_TCP_PORT = 3333 ;
	
	public static final int RMI_REQUEST_ACCEPT_SERVER_PORT = 3334 ;
	
	public static final int SOCKET_RESUME_CONNECTION_SERVER_TCP_PORT = 3331 ;
	
	public static final int RMI_GUI_MAP_SERVER_PORT = 7000 ;
	
	private static ServerEnvironment instance ;
	
	private final String DEFAULT_LOCALHOST_IP_ADDRESS ;
	
	private String localhostIPAddress ;
	
	private ServerEnvironment () 
	{
		DEFAULT_LOCALHOST_IP_ADDRESS = "127.0.0.1" ;
		try 
		{
			localhostIPAddress = InetAddress.getLocalHost ().getHostAddress () ;
		}
		catch (UnknownHostException e) 
		{
			// log this
			e.printStackTrace() ;
			localhostIPAddress = DEFAULT_LOCALHOST_IP_ADDRESS ;
		} 
	}
	
	public synchronized static ServerEnvironment getInstance () 
	{
		if ( instance == null )
			instance = new ServerEnvironment () ;
		return instance ;
	}
	
	public String getLocalhostIPAddress ()
	{
		 return localhostIPAddress ;
		
	}
	
}
