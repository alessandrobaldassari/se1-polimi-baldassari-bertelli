package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.animator;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.MapMeasuresManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.PositionableElementCoordinatesManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Counter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JComponent;

public class BufferedAnimator extends Animator
{

	private final int NUMBER_OF_ADD_MESSAGE_TO_WAIT_FOR_A_REMOVE_MESSAGE_TO_EXPIRE = 3 ;

	private final PositionableElementCoordinatesManager positionableElementManager ;
	
	private final MapMeasuresManager coordinateManager ;
	
	private ConcurrentMap < GUIGameMapNotificationMessage , Counter > removeMessagesMap ;
	
	public BufferedAnimator ( JComponent componentToRepaint , PositionableElementCoordinatesManager positionableElementMananger , MapMeasuresManager coordinateManager ) 
	{
		super(componentToRepaint);
		if ( positionableElementMananger != null && coordinateManager != null )
		{
			this.positionableElementManager = positionableElementMananger ;
			this.coordinateManager = coordinateManager ;
			removeMessagesMap = new ConcurrentHashMap < GUIGameMapNotificationMessage , Counter > () ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	@Override
	public void run () 
	{
		try 
		{
			Thread.sleep ( 60000 ) ;
		}
		catch (InterruptedException e) {}
	}

	@Override
	public synchronized void addAddMessage ( GUIGameMapNotificationMessage m ) 
	{
		GUIGameMapNotificationMessage removed ;
		removed = findRemoved ( m ) ;
		// if I found the starter
		if ( removed != null )
		{
			// the removed message will now be processed.
			removeMessagesMap.remove ( removed ) ;
			// perform the animation
			new LinearAnimatorDrawer(this, positionableElementManager, coordinateManager, removed, m ).animate() ;
		}
		else	// no animation available.
		{
			// corrispondence not found, add the element
			positionableElementManager.addElement ( m ) ;
			repaintComponent();
		}
		// increment the counters
		incrementRemoveMessageCounter () ;
		// remove eventually old messages, bulk mode
		cleanOldMessages () ;
	}

	@Override
	public synchronized void addRemoveMessage ( GUIGameMapNotificationMessage m ) 
	{
		removeMessagesMap.put ( m , new Counter ( 0 ) ) ;
	}
	
	private GUIGameMapNotificationMessage findRemoved ( GUIGameMapNotificationMessage added ) 
	{
		GUIGameMapNotificationMessage removed ;
		removed = null ;
		for ( GUIGameMapNotificationMessage g : removeMessagesMap.keySet () )
			if( g.getUID() < added.getUID() && added.getWhoId() == g.getWhoId() )
			{
				removed = g ;
				break ;
			}
		return removed ;
	}
	
	private void incrementRemoveMessageCounter () 
	{
		for ( Counter c : removeMessagesMap.values () )
			c.increment();
	}
	
	private void cleanOldMessages () 
	{
		boolean repaint ;
		repaint = false ;
		for ( GUIGameMapNotificationMessage g : removeMessagesMap.keySet () )
			if ( removeMessagesMap.get(g).getValue() >= NUMBER_OF_ADD_MESSAGE_TO_WAIT_FOR_A_REMOVE_MESSAGE_TO_EXPIRE )
			{
				repaint = true ;
				positionableElementManager.removeElement ( g ) ;
				removeMessagesMap.remove ( g ) ;
			}
		if ( repaint  )
			repaintComponent();
	}
	
}
