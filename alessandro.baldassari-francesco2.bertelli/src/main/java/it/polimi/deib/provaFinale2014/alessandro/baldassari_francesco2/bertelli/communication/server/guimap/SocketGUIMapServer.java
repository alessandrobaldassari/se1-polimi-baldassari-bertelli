package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.guimap;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class SocketGUIMapServer implements Runnable , GameMapObserver
{

	private static int lastPortEmitted = 4000 ;
	
	private int port ;
	
	private ServerSocket serverSocket ;
	
	private Collection < ObjectOutputStream > clients ;
	
	public SocketGUIMapServer () throws IOException 
	{
		lastPortEmitted ++ ;
		port = lastPortEmitted ;
		serverSocket = new ServerSocket ( port ) ;
		clients = new ArrayList < ObjectOutputStream > () ;
	}

	public int getPort () 
	{
		return port ;
	}
	
	@Override
	public void run () 
	{
		Socket s ;
		ObjectOutputStream out ;
		while ( true )
		{
			try 
			{
				s = serverSocket.accept () ;
				out = new ObjectOutputStream ( s.getOutputStream () ) ;
				clients.add ( out ) ;
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , 
			PositionableElementType whoType, Integer whoId ) 
	{
		notificationAlgo ( "ADDED" , whereType , whereId , whoType , whoId ) ;
	}

	@Override
	public void onPositionableElementRemoved ( GameMapElementType whereType,
			Integer whereId, PositionableElementType whoType , Integer whoId)
	{
		notificationAlgo ( "REMOVED" , whereType , whereId , whoType , whoId ) ;		
	}
	
	private void notificationAlgo ( String actionAssociated , GameMapElementType whereType,
			int whereId, PositionableElementType whoType, int whoId ) 
	{
		GUIMapNotificationMessage m ;
		m = new GUIMapNotificationMessage ( actionAssociated , whereType , whereId , whoType , whoId ) ;
		for ( ObjectOutputStream out : clients )
		{
			try 
			{
				out.writeObject ( m ) ;
				out.flush () ;
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
}
