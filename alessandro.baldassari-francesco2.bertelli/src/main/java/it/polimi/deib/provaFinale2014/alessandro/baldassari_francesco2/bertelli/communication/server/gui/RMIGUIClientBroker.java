package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGUIClientBroker extends Remote
{

	public abstract void putMessage ( GUIMapNotificationMessage nextMessage ) throws RemoteException ;

	public abstract GUIMapNotificationMessage getMessage () throws RemoteException ;

}