package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIPlayerNotificationMessage;

import java.rmi.RemoteException;
import java.util.List;

/***/
public class RMIGUINotifier implements Runnable , PlayerObserver
{

	/***/
	private RMIGUIClientBroker lastArrived ;
	
	private List < GUIGameMapNotificationMessage > messages ;
	
	/***/
	public RMIGUINotifier ( List < GUIGameMapNotificationMessage > messages , RMIGUIClientBroker lastArrived ) 
	{
		this.messages = messages ;
		this.lastArrived = lastArrived ;
	}
	
	/***/
	@Override
	public void run () 
	{
		GUIGameMapNotificationMessage nextMessage ;
		int i ;
		i = 0 ;
		while ( true )
		{
			if ( i < messages.size () )
			{
				
				nextMessage = messages.get(i) ;
				try 
				{
					synchronized  ( lastArrived ) 
					{
						lastArrived.putMessage ( nextMessage ) ;							
					}
				} 
				catch (RemoteException e) 
				{
					e.printStackTrace();
				}
				i ++ ;
			}
		}
	}

	@Override
	public void onPay(Integer paymentAmount, Integer moneyYouHaveNow) 
	{
		try 
		{
			synchronized  ( lastArrived ) 
			{
				lastArrived.putMessage ( new GUIPlayerNotificationMessage ( "onPay" , paymentAmount , moneyYouHaveNow ) );
			}
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onGetPayed ( Integer paymentAmount, Integer moneyYouHaveNow ) 
	{
		try 
		{
			synchronized  ( lastArrived ) 
			{
				lastArrived.putMessage ( new GUIPlayerNotificationMessage ( "onGetPayed" , paymentAmount , moneyYouHaveNow ) );
			}
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onCardAdded ( RegionType cardType ) 
	{
		try 
		{
			synchronized  ( lastArrived ) 
			{
				lastArrived.putMessage ( new GUIPlayerNotificationMessage ( "onCardAdded" , cardType , null ) );
			}
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onCardRemoved ( RegionType cardType )
	{
		try 
		{
			synchronized  ( lastArrived ) 
			{
				lastArrived.putMessage ( new GUIPlayerNotificationMessage ( "onCardRemoved" , cardType , null ) );
			}
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}				
	}
	
}