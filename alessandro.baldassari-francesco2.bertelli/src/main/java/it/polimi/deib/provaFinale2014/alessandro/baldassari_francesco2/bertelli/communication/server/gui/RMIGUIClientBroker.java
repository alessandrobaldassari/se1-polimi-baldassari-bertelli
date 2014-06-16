package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGUIClientBroker extends Remote
{

	public abstract void putMessage ( GUINotificationMessage nextMessage ) throws RemoteException ;

	public abstract GUINotificationMessage getMessage () throws RemoteException ;

}