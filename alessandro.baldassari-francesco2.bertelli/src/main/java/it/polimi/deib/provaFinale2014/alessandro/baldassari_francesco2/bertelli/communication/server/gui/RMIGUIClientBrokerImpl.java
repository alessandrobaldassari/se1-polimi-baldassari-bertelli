package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUINotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/***/
public class RMIGUIClientBrokerImpl implements RMIGUIClientBroker 
{

	/***/
	private BlockingQueue < GUINotificationMessage > messages ;
	
	/***/
	public RMIGUIClientBrokerImpl () 
	{
		messages = new LinkedBlockingQueue < GUINotificationMessage > () ;
	}
	
	@Override
	public void putMessage ( GUINotificationMessage nextMessage ) throws RemoteException
	{
		messages.offer ( nextMessage ) ;
	}
	
	/***/
	@Override
	public GUINotificationMessage getMessage () throws RemoteException
	{
		try 
		{
			return messages.take () ;
		}
		catch (InterruptedException e) 
		{
			Logger.getGlobal().log ( Level.WARNING , Utilities.EMPTY_STRING , e ) ;
			throw new RemoteException () ;
		}
	}
	
}
