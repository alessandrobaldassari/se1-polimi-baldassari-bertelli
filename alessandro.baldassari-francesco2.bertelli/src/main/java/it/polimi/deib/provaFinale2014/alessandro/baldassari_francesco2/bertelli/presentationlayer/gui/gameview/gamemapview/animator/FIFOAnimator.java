package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.animator;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.MapMeasuresManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.PositionableElementCoordinatesManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JComponent;

public class FIFOAnimator extends Animator 
{

	private MapMeasuresManager coordinatesManager ;
	
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	private GUIGameMapNotificationMessage buffer ;
	
	public FIFOAnimator ( JComponent toRepaint , MapMeasuresManager coordinatesManager , PositionableElementCoordinatesManager positionableElementManager ) 
	{
		super ( toRepaint ) ;
		if ( coordinatesManager != null && positionableElementManager != null )
		{
			this.coordinatesManager = coordinatesManager ;
			this.positionableElementsManager = positionableElementManager ;
			buffer = null ;
		}
	}

	@Override
	public void run () 
	{
		try 
		{
			Thread.sleep(60000);
		}
		catch (InterruptedException e) {}
	}

	@Override
	public synchronized void addAddMessage ( GUIGameMapNotificationMessage m ) 
	{
		if ( buffer == null ) // surely an adding operation, no animation for this
		{
			positionableElementsManager.addElement ( m ) ;
			repaintComponent () ; 
		}	
		else	// buffer not null, we could do an animation !
		{
			if ( m.getUID () > buffer.getUID () && m.getWhoId() == buffer.getWhoId () )	// if sn ok and element uid ok,
				new LinearAnimatorDrawer(this, positionableElementsManager, coordinatesManager, buffer, m ).animate(); 
			else
			{
				positionableElementsManager.removeElement(buffer);
				positionableElementsManager.addElement(m); 
				repaintComponent();
			}
			buffer = null ;
		}
	}

	@Override
	public synchronized void addRemoveMessage ( GUIGameMapNotificationMessage m ) 
	{
		buffer = m ;
	}

}
