package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;

import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JComponent;

class Animator implements Runnable
{

	private JComponent toRepaint ;
	
	private MapMeasuresCoordinatesManager coordinatesManager ;
	
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	private BlockingQueue < GUIGameMapNotificationMessage > queue ;

	private GUIGameMapNotificationMessage addedOne ;
	
	private GUIGameMapNotificationMessage removedOne ;

	private String expected ;

	private AtomicBoolean inAnimation ;
	
	private BufferedImage toDraw ;
	
	private Ellipse2D whereDraw ;
	
	public Animator ( JComponent toRepaint , MapMeasuresCoordinatesManager coordinatesManager , PositionableElementCoordinatesManager positionableElementManager ) 
	{
		if ( toRepaint != null && coordinatesManager != null && positionableElementManager != null )
		{
			this.toRepaint = toRepaint ;
			this.coordinatesManager = coordinatesManager ;
			this.positionableElementsManager = positionableElementManager ;
			queue = new LinkedBlockingQueue < GUIGameMapNotificationMessage > () ;
			addedOne = null ;
			removedOne = null ;
			expected = "REMOVE" ;
			inAnimation = new AtomicBoolean ( false ) ;
			toDraw = null ;
			whereDraw = null ;
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
	
	public boolean isInAnimation () 
	{
		return inAnimation.get () ;
	}
	
	public BufferedImage getToDraw () 
	{
		return toDraw ;
	}
	
	public Ellipse2D getWhereDraw () 
	{
		return whereDraw ;
	}

	public void animate () 
	{
		Ellipse2D start ;
		Ellipse2D end ;
		System.out.println ( "ALGO_ANIMATOR - RUN : INIZIO" ) ;
		// first, determines if a Sheperd or an Animal is moving, then act as a consequence.
		if ( removedOne.getWhereType() == GameMapElementType.REGION )
		{
			start = coordinatesManager.getRegionData ( removedOne.getWhereId() ).get ( removedOne.getWhoType () ) ;
			end = coordinatesManager.getRegionData ( addedOne.getWhereId () ).get ( removedOne.getWhoType() ) ;
		}
		else
		{
			start = coordinatesManager.getRoadBorder ( removedOne.getWhereId() ) ;
			end = coordinatesManager.getRoadBorder ( addedOne.getWhereId() ) ;
		}
		animationCycle(start, end);
		// the commit the last message to the manager to update data.
		positionableElementsManager.addElement ( addedOne.getWhereType() , addedOne.getWhereId() , addedOne.getWhoType() , addedOne.getWhereId () ) ;
		// reset the state variables.
		addedOne = null ;
		removedOne = null ;
		toRepaint.repaint () ;
		// re-activate the other thread.
		synchronized ( inAnimation ) 
		{
			inAnimation.set(false) ;
			inAnimation.notifyAll () ;
		}
		System.out.println ( "ALGO_ANIMATOR - RUN : FINE" ) ;
	}
	
	/**
	 * @param start
	 * @param end 
	 */
	private void animationCycle ( Ellipse2D start , Ellipse2D end ) 
	{
		//coordinatesManager.getRoadBorder ( removedOne.get )				
		Ellipse2D current ;
		int distanceX ;
		int distanceY ;
		int dx ;
		int dy ;
		int numberOfMoves = 10 ;
		int pauseTime ;
		int i ;
		// set the pause time that will determine the speed - in millisecond.
		pauseTime = 1000 ;				
		// retrieve the image.
		toDraw = SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( removedOne.getWhoType () , false ) ;
		// now perform the moving ; start from the start point.
		current = new Ellipse2D.Double( start.getX() , start.getY() , start.getWidth() , start.getHeight() ) ;
		whereDraw = current ;
		// determine the lenght of one move
		distanceX = Math.abs ( ( int ) ( start.getCenterX() - end.getCenterX() ) ) ;
		distanceY = Math.abs ( ( int ) ( start.getCenterY() - end.getCenterY () ) ) ;
		// determine the little move.
		dx = ( int ) ( distanceX / numberOfMoves ) ;
		dy = ( int ) ( distanceY / numberOfMoves ) ;
		// determine the sign of the little move
		if ( end.getX () < start.getX() )
			dx = -dx ;
		if ( end.getY() < start.getY() )
			dy = -dy ;
		// for each move
		inAnimation.set ( true ) ;
		for ( i = 0 ; i < numberOfMoves ; i ++ )
		{
			current.setFrame ( current.getX () + dx , current.getY () + dy , current.getWidth() , current.getHeight() ) ;
			toRepaint.repaint () ;
			try 
			{
				Thread.sleep ( pauseTime ) ;
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void run () 
	{
		while ( true )
		{
			GUIGameMapNotificationMessage nextM ;
			synchronized ( inAnimation )
			{
				while ( inAnimation.get() )
					try 
					{
						inAnimation.wait () ;
					}
					catch (InterruptedException e) {}
			}
			try 
			{
				System.out.println ( "ANIMATOR - RUN : WAITING FOR A MESSAGE." ) ;
				nextM = queue.take () ;
				System.out.println ( "ANIMATOR - RUN : MESSAGE TAKEN : " + nextM ) ;
				// if an element is removed may be an animation is starting
				if ( nextM.getActionAssociated ().compareToIgnoreCase ( "ADD" ) == 0 )
				{
					// if the flow is ok
					if ( expected.compareToIgnoreCase ( "ADD" ) == 0 )
					{
						// if this is the dest position, start the animation.
						if ( removedOne != null && removedOne.getWhoType() == nextM.getWhoType() && removedOne.getWhoId() == nextM.getWhoId() )
						{
							addedOne = nextM ;
							System.out.println ( "WORKER - RUN : BEFORE LAUCHING AND ANIMATION." ) ;
							animate();
						}
						else // may be there is a problem, print the data.
						{
							// do not loss any data
							if ( removedOne != null )
							{
								positionableElementsManager.removeElement (  nextM.getWhereType() , nextM.getWhereId() , nextM.getWhoType() , nextM.getWhereId () ) ;
								toRepaint.repaint () ;
							}
							positionableElementsManager.addElement ( nextM.getWhereType() , nextM.getWhereId() , nextM.getWhoType() , nextM.getWhoId () ) ;
							toRepaint.repaint () ;
						}
					}
					else	// not in animation context, draw.
					{
						positionableElementsManager.addElement ( nextM.getWhereType() , nextM.getWhereId() , nextM.getWhoType() , nextM.getWhoId () ) ;
						toRepaint.repaint () ;
					}	
				}
				else
				{
					if ( nextM.getActionAssociated ().compareToIgnoreCase ( "REMOVE" ) == 0 )
					{
						positionableElementsManager.removeElement (  nextM.getWhereType() , nextM.getWhereId() , nextM.getWhoType() , nextM.getWhoId () ) ;
						toRepaint.repaint () ;
						if ( expected.compareToIgnoreCase ( "REMOVE" ) == 0 ) // if the flow is ok.
						{
							removedOne = nextM ;
							expected = "ADD" ;
						}
					}
					else
					{
						// error, log.
					}
				}
			}
			catch (InterruptedException e) 
			{
				System.err.println ( e ) ;
			}
		}
	}	
		
}
