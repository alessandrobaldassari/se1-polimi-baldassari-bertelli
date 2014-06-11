package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.guimap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.guimap.GUIMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionAbstractObservable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

public class SocketGUIMapClient extends WithReflectionAbstractObservable < GameMapObserver > implements Runnable
{

	private final String SERVER_IP = "127.0.0.1" ;
	
	private Socket socket ;
	
	private boolean on ;
	
	private ObjectInputStream ois ;
	
	private WithReflectionObservableSupport < GameMapObserver > support ;
	
	public SocketGUIMapClient ( int serverPort ) throws UnknownHostException, IOException
	{
		socket = new Socket ( SERVER_IP , serverPort ) ;
		ois = new ObjectInputStream ( socket.getInputStream () ) ;
		on = true ;
		support = new WithReflectionObservableSupport < GameMapObserver > () ;
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
				m = (GUIMapNotificationMessage) ois.readObject () ;
				if ( m.getActionAssociated ().compareTo ( "ADDED" ) == 0 )
					methodName = "onPositionableElementAdded" ;
				else
					methodName = "onPositionableElementRemoved" ;
				notifyObservers ( methodName , m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () );
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
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

	@Override
	public void addObserver(GameMapObserver newObserver) 
	{
		support.addObserver ( newObserver ) ;
	}

	@Override
	public void removeObserver ( GameMapObserver oldObserver ) 
	{
		support.removeObserver ( oldObserver ) ;
	}

}
