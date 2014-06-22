package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.gui;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.RMIGUIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUINotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIPlayerNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

public class RMIGUIMapClient implements Runnable
{

	/**
	 * The IP address where locate the Server. 
	 */
	private final static String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The port where contact the Server. 
	 */
	private final static int SERVER_DIRECT_PORT = 7000 ;
	
	/***/
	private final String brokerName ;
	
	/**
	 * A broker object to interact with the Server. 
	 */
	private RMIGUIClientBroker clientBroker ;
	
	/***/
	private boolean on ;
	
	private ViewPresenter viewPresenter ;
	
	private GameMapObserver gameMapObserver ;
	
	private PlayerObserver playerObserver ;
	
	/***/
	public RMIGUIMapClient ( String brokerName ) 
	{
		super () ;
		this.brokerName = brokerName ;
		viewPresenter = null ;
	}
	
	public void setGameMapObserver ( GameMapObserver g ) 
	{
		this.gameMapObserver = g ;
	}
	
	public void setPlayerObserver ( PlayerObserver playerObserver ) 
	{
		this.playerObserver = playerObserver ;
	}
	
	public void connect ( ViewPresenter viewPresenter ) throws IOException 
	{
		Registry registry ;
		try 
		{
			registry = LocateRegistry.getRegistry ( SERVER_IP_ADDRESS , SERVER_DIRECT_PORT ) ;
			clientBroker = ( RMIGUIClientBroker ) registry.lookup ( brokerName ) ;
			on = true ;
			this.viewPresenter = viewPresenter ;
		} 
		catch ( NotBoundException e ) 
		{
			viewPresenter.stopApp();
		}
	}	
	
	@Override
	public void run () 
	{
		GUINotificationMessage m ;
		while ( on )
		{
			try 
			{
				m = ( GUINotificationMessage ) clientBroker.getMessage () ;
				if ( m instanceof GUIGameMapNotificationMessage )
					manageGameMapMessage ( (GUIGameMapNotificationMessage) m ) ;
				else
					managePlayerMessage ( (GUIPlayerNotificationMessage) m ) ;
			}
			catch (IOException e) 
			{
				Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e ) ;
				viewPresenter.stopApp();
			} 
		}		
	}
	
	/***/
	private void manageGameMapMessage ( GUIGameMapNotificationMessage m ) 
	{
		if ( gameMapObserver != null && m!= null )
		{
			if ( m.getActionAssociated ().compareTo ( "ADD" ) == 0 )
				gameMapObserver.onPositionableElementAdded ( m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () ) ;
			else
				if ( m.getActionAssociated ().compareTo ( "REMOVE" ) == 0 )
					gameMapObserver.onPositionableElementRemoved ( m.getWhereType() , m.getWhereId () , m.getWhoType () , m.getWhoId () ) ;
		}
	}
	
	/***/
	private void managePlayerMessage ( GUIPlayerNotificationMessage m )
	{
		if ( playerObserver != null )
		{
			if ( m.getActionAssociated().compareToIgnoreCase ( "onPay" ) == 0 )
				playerObserver.onPay ( ( Integer ) m.getFirstParam() , (Integer) m.getSecondParam() );
			else
				if ( m.getActionAssociated().compareToIgnoreCase ( "onGetPayed" ) == 0 )
					playerObserver.onGetPayed ( ( Integer ) m.getFirstParam() , (Integer) m.getSecondParam() );
				else
					if ( m.getActionAssociated().compareToIgnoreCase ( "onCardAdded" ) == 0 )
						playerObserver.onCardAdded ( (RegionType) m.getFirstParam());
					else
						if ( m.getActionAssociated().compareToIgnoreCase ( "onCardRemoved" ) == 0 )
							playerObserver.onCardRemoved ( (RegionType) m.getFirstParam() );
						else
							Logger.getGlobal().log ( Level.WARNING , "Unknown message" , m ) ;
		}
	}
	
}
