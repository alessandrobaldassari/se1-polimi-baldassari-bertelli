package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.animator;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.MapMeasuresManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.PositionableElementCoordinatesManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;

public class FIFOWaitingAnimator extends Animator
{
	
	private MapMeasuresManager coordinatesManager ;
	
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	private BlockingQueue < GUIGameMapNotificationMessage > queue ;

	private GUIGameMapNotificationMessage addedOne ;
	
	private GUIGameMapNotificationMessage removedOne ;

	private String expected ;

	public FIFOWaitingAnimator ( JComponent toRepaint , MapMeasuresManager coordinatesManager , PositionableElementCoordinatesManager positionableElementManager ) 
	{
		super ( toRepaint ) ;
		if ( coordinatesManager != null && positionableElementManager != null )
		{
			this.coordinatesManager = coordinatesManager ;
			this.positionableElementsManager = positionableElementManager ;
			queue = new LinkedBlockingQueue < GUIGameMapNotificationMessage > () ;
			addedOne = null ;
			removedOne = null ;
			expected = "REMOVE" ;
		}
	}

	public void addAddMessage (  GUIGameMapNotificationMessage m ) 
	{
		queue.offer(m);
	}
	
	public void addRemoveMessage ( GUIGameMapNotificationMessage m ) 
	{
		queue.offer(m);
	}

	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void run () 
	{
		GUIGameMapNotificationMessage nextM ;
		while ( true )
		{
			try 
			{
				addedOne = null ;
				removedOne = null ;
				expected = "REMOVE";
				nextM = queue.take () ;
				if ( nextM.getActionAssociated ().compareToIgnoreCase ( "ADD" ) == 0 )
				{
					// if the flow is ok
					if ( expected.compareToIgnoreCase ( "ADD" ) == 0 )
					{
						// if this is the dest position, start the animation.
						if ( removedOne != null && removedOne.getWhoType() == nextM.getWhoType() && removedOne.getWhoId() == nextM.getWhoId() )
						{
							addedOne = nextM ;
							new LinearAnimatorDrawer(this, positionableElementsManager , coordinatesManager, removedOne , addedOne ) ;
							addedOne = null ;
							removedOne = null ;
							expected = "REMOVE" ;
						}
						else // may be there is a problem, print the data.
						{
							// do not loss any data
							if ( removedOne != null )
								positionableElementsManager.removeElement ( removedOne ) ;
							positionableElementsManager.addElement ( nextM ) ;
							repaintComponent();
						}
					}
					else	// not in animation context, probably a new element has been added draw.
					{
						positionableElementsManager.addElement ( nextM ) ;
						repaintComponent();
					}	
				}
				else
				{
					if ( nextM.getActionAssociated ().compareToIgnoreCase ( "REMOVE" ) == 0 )
					{
						if ( expected.compareToIgnoreCase( "REMOVE" ) == 0 )
						{
							removedOne = nextM ;
							expected = "ADD" ;
						}
						else
						{
							positionableElementsManager.removeElement ( nextM ) ;
							repaintComponent();
						}
					}
					else	// unknown message, log it.
						Logger.getGlobal().log ( Level.WARNING , "Animator - message received unknown" , nextM ) ;
				}
			}
			catch (InterruptedException e) 
			{
				Logger.getGlobal().log ( Level.WARNING , "Animator - interrupted" , e ) ;
			}
		}
	}	
		
}
