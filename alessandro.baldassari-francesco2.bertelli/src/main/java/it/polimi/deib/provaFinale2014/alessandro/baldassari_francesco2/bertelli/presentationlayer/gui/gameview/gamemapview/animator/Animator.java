package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.animator;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;

import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JComponent;

public abstract class Animator implements Runnable
{
	
	private AtomicBoolean inAnimation ;

	private JComponent componentToRepaint ;
	
	private BufferedImage imageToDraw ;
	
	private Ellipse2D whereDraw ;
	
	public Animator ( JComponent componentToRepaint ) 
	{
		this.componentToRepaint = componentToRepaint ;
		inAnimation = new AtomicBoolean ( false ) ;
		imageToDraw = null ;
		whereDraw = null ;
	}
	
	public abstract void addAddMessage (  GUIGameMapNotificationMessage m ) ;
	
	public abstract void addRemoveMessage ( GUIGameMapNotificationMessage m ) ;
	
	protected void beginAnimation () 
	{
		inAnimation.set(true);
 	}
	
	protected void endAnimation () 
	{
		inAnimation.set(false);
 	}
	
	public boolean isInAnimation () 
	{
		return inAnimation.get () ;
	}
	
	protected void repaintComponent () 
	{
		componentToRepaint.repaint () ;
	}
	
	protected void setImageToDraw ( BufferedImage imageToDraw ) 
	{
		this.imageToDraw = imageToDraw ;
	}
	
	public BufferedImage getToDraw () 
	{
		return imageToDraw ;
	}
	
	protected void setWhereDraw ( Ellipse2D whereDraw ) 
	{
		this.whereDraw = whereDraw ;
	}
	
	public Ellipse2D getWhereDraw () 
	{
		return whereDraw ;
	}

	
}
