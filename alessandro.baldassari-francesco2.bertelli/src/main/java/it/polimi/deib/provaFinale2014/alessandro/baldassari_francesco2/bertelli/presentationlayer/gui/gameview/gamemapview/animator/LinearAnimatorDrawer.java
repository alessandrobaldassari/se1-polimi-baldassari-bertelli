package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.animator;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.MapMeasuresManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.PositionableElementCoordinatesManager;

import java.awt.geom.Ellipse2D;

public class LinearAnimatorDrawer 
{

	private Animator animator ;
	
	private PositionableElementCoordinatesManager positionableElementManager ;
	
	private MapMeasuresManager coordinatesManager ;
	
	private GUIGameMapNotificationMessage removedOne ;
	
	private GUIGameMapNotificationMessage addedOne ;
	
	public LinearAnimatorDrawer ( Animator animator , PositionableElementCoordinatesManager positionableElementManager , MapMeasuresManager coordinatesManager , GUIGameMapNotificationMessage removedOne , GUIGameMapNotificationMessage addedOne ) 
	{
		if ( animator != null && positionableElementManager != null && coordinatesManager != null && removedOne != null && addedOne != null )
		{
			this.animator = animator ;
			this.positionableElementManager = positionableElementManager ;
			this.coordinatesManager = coordinatesManager ;
			this.addedOne = addedOne ;
			this.removedOne = removedOne ;
		}
	}
	
	public void animate () 
	{
		PositionableElementType p ;
		Ellipse2D start ;
		Ellipse2D end ;
		System.out.println ( "ALGO_ANIMATOR - RUN : INIZIO" ) ;
		// first, determines if a Sheperd or an Animal is moving, then act as a consequence.
		if ( removedOne.getWhereType() == GameMapElementType.REGION )
		{
			if ( PositionableElementType.isStandardOvine ( removedOne.getWhoType() ) )
				p = PositionableElementType.STANDARD_ADULT_OVINE ;
			else
				p = removedOne.getWhoType() ;
			start = coordinatesManager.getRegionData ( removedOne.getWhereId () ).get ( p ) ;
			if ( PositionableElementType.isStandardOvine ( addedOne.getWhoType() ) )
				p = PositionableElementType.STANDARD_ADULT_OVINE ;
			else
				p = removedOne.getWhoType() ;
			end = coordinatesManager.getRegionData ( addedOne.getWhereId () ).get ( p ) ;
		}
		else
		{
			start = coordinatesManager.getRoadBorder ( removedOne.getWhereId() ) ;
			end = coordinatesManager.getRoadBorder ( addedOne.getWhereId() ) ;
		}
		positionableElementManager.removeElement ( removedOne ) ;
		animator.repaintComponent();
		// effectively perform the animation cycle.
		animationCycle(start, end);
		// the commit the last message to the manager to update data.
		positionableElementManager.addElement ( addedOne ) ;
		animator.repaintComponent();
		// re-activate the other threads
		animator.endAnimation();
		System.err.println ( "\n\n\n\n\n\n\n\n\n\n\nALGO_ANIMATOR - RUN : FINE\n\n\n\n\n\n\n\n\nn\n\n\n\n\n\n\n\n" ) ;
	}
	
	/**
	 * @param start
	 * @param end 
	 */
	private void animationCycle ( Ellipse2D start , Ellipse2D end ) 
	{
		System.out.println ( "LINEAR_ANIMATOR_DRAWER - ANIMATION_CYCLE : INIZIO\nParameters :\n-start : " + start + "\n-end : " + end ) ;
		//coordinatesManager.getRoadBorder ( removedOne.get )				
		Ellipse2D current ;
		int distanceX ;
		int distanceY ;
		int dx ;
		int dy ;
		int numberOfMoves = 20 ;
		int pauseTime ;
		int i ;
		// set the pause time that will determine the speed - in millisecond.
		pauseTime = 250 ;				
		// retrieve the image.
		animator.setImageToDraw( SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( removedOne.getWhoType () , false , false ) ) ;
		// now perform the moving ; start from the start point.
		current = new Ellipse2D.Double( start.getX() , start.getY() , start.getWidth() , start.getHeight() ) ;
		animator.setWhereDraw( current ) ;
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
		animator.beginAnimation();
		for ( i = 0 ; i < numberOfMoves ; i ++ )
		{
			current.setFrame ( current.getX () + dx , current.getY () + dy , current.getWidth() , current.getHeight() ) ;
			animator.repaintComponent();
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
	
	
}
