package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIGUIClientBrokerImpl implements RMIGUIClientBroker 
{

	private BlockingQueue < GUIMapNotificationMessage > messages ;
	
	public RMIGUIClientBrokerImpl () 
	{
		messages = new LinkedBlockingQueue < GUIMapNotificationMessage > () ;
	}
	
	@Override
	public void putMessage ( GUIMapNotificationMessage nextMessage ) throws RemoteException
	{
		messages.offer ( nextMessage ) ;
	}
	
	@Override
	public GUIMapNotificationMessage getMessage () throws RemoteException
	{
		try 
		{
			return messages.take () ;
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			throw new RemoteException () ;
		}
	}
	
}
