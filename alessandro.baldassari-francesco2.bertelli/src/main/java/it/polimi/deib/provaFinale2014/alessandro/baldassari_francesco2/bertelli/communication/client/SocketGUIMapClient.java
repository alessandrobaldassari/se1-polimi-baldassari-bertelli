package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.guimap.GUIMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;

/***/
public class SocketGUIMapClient extends WithReflectionAbstractObservable < GameMapObserver > implements Runnable
{

	/***/
	private final String SERVER_IP = "127.0.0.1" ;
	
	/***/
	private Socket socket ;
	
	/***/
	private boolean on ;
	
	/***/
	private ObjectInputStream ois ;
	
	/***/
	public SocketGUIMapClient ( int serverPort ) throws UnknownHostException, IOException
	{
		socket = new Socket ( SERVER_IP , serverPort ) ;
		ois = new ObjectInputStream ( socket.getInputStream () ) ;
		on = true ;
	}
	
	@Override
	public void run () 
	{
		GUIMapNotificationMessage m ;
		String methodName ;
		while ( on )
		{
			try 
			{
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : WAITING FOR A MESSAGE" ) ;
				m = ( GUIMapNotificationMessage ) ois.readObject () ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : MESSAGE CATCH" ) ;
				if ( m.getActionAssociated ().compareTo ( "ADDED" ) == 0 )
					methodName = "onPositionableElementAdded" ;
				else
					methodName = "onPositionableElementRemoved" ;
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : BEFORE NOTIFYING." ) ;
				notifyObservers ( methodName , m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () );
				System.out.println ( "SOCKET_GUI_MAP_CLIENT : AFTER NOTIFYING." ) ;
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
			catch ( SocketException s )
			{
				s.printStackTrace();
				on = false ;
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch  (MethodInvocationException e ) 
			{
				e.printStackTrace();
			}
		}
	}

}
