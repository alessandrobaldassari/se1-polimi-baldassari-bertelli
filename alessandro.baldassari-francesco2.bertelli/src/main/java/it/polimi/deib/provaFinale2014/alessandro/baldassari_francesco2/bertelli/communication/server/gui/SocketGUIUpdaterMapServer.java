package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/***/
public class SocketGUIUpdaterMapServer extends GUIUpdaterServer
{

	/***/
	private static int lastPortEmitted = 4000 ;
	
	/***/
	private int port ;
	
	/***/
	private ServerSocket serverSocket ;
	
	/***/
	private Collection < ObjectOutputStream > clients ;
	
	/***/
	private List < GUIGameMapNotificationMessage > messages ;
	
	/***/
	private Queue < GUIGameMapNotificationMessage > messagesForOthers ;
	
	/***/
	private Executor executor ;
	
	/***/
	public SocketGUIUpdaterMapServer () throws IOException 
	{
		lastPortEmitted ++ ;
		port = lastPortEmitted ;
		serverSocket = new ServerSocket ( port ) ;
		clients = new ArrayList < ObjectOutputStream > () ;
		messages = new ArrayList < GUIGameMapNotificationMessage > () ;
		executor = Executors.newCachedThreadPool () ;
		messagesForOthers = new LinkedList < GUIGameMapNotificationMessage > () ;
	}

	/***/
	public int getPort () 
	{
		return port ;
	}
	
	/***/
	@Override
	public void run () 
	{
		Socket s ;
		ObjectOutputStream out ;
		System.out.println ( "SOCKET_GUI_MAP_SERVER - RUN : BEFORE BEGIN CYCLE" ) ;
		while ( true )
		{
			try 
			{
				System.out.println ( "SOCKET_GUI_MAP_SERVER - RUN : WAITING FOR REQUESTS." ) ;
				s = serverSocket.accept () ;
				System.out.println ( "SOCKET_GUI_MAP_SERVER - RUN : REQUEST CATCH." ) ;
				new ObjectInputStream ( s.getInputStream () ) ;
				out = new ObjectOutputStream ( s.getOutputStream () ) ;
				synchronized ( clients )
				{
					clients.add ( out ) ;
					clients.notifyAll();
				}
				System.out.println ( "SOCKET_GUI_MAP_SERVER - RUN : REQUEST SERVED." ) ;
				executor.execute ( new LastArriviedNotifier ( out ) ); 
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * AS THE SUPER'S ONE 
	 */
	@Override
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , 
			PositionableElementType whoType, Integer whoId ) 
	{
		notificationAlgo ( "ADDED" , whereType , whereId , whoType , whoId ) ;
	}

	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	public void onPositionableElementRemoved ( GameMapElementType whereType,
			Integer whereId, PositionableElementType whoType , Integer whoId)
	{
		notificationAlgo ( "REMOVED" , whereType , whereId , whoType , whoId ) ;		
	}
	
	/***/
	private void notificationAlgo ( String actionAssociated , GameMapElementType whereType,
			int whereId, PositionableElementType whoType, int whoId ) 
	{
		GUIGameMapNotificationMessage m ;
		m = new GUIGameMapNotificationMessage ( actionAssociated , whereType , whereId , whoType , whoId ) ;
		messages.add ( m ) ;
		synchronized ( messagesForOthers ) 
		{
			messagesForOthers.offer ( m ) ;
			messagesForOthers.notifyAll();
		}
		System.out.println ( "SOCKET_GUI_MAP_SERVER - NOTIFICATION_ALGO : BEFORE DO A NOTIFICATION SESSION." ) ;
		synchronized ( clients ) 
		{		
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
	
	/***/
	private class LastArriviedNotifier implements Runnable 
	{

		/***/
		private ObjectOutputStream lastArrived ;
		
		/***/
		public LastArriviedNotifier ( ObjectOutputStream lastArrived ) 
		{
			this.lastArrived = lastArrived ;
		}
		
		/***/
		@Override
		public void run () 
		{
			synchronized ( messagesForOthers ) 
			{
				System.out.println ( "SOCKET_GUI_MAP_SERVER - NOTIFICATION_ALGO : BEFORE DO A LAST ARRIVIED NOTIFICATION." ) ;
				for ( GUIGameMapNotificationMessage m : messagesForOthers )
					try 
					{
						lastArrived.writeObject ( m ) ;
						lastArrived.flush();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
		}
		
	}
	
}
