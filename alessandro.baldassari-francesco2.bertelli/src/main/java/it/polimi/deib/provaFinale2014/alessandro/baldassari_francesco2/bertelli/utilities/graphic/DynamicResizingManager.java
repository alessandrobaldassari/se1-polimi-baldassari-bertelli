package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DynamicResizingManager extends ComponentAdapter 
{

	private Component toResize ;
	
	public DynamicResizingManager ( Component toResize ) 
	{
		if ( toResize != null )
			this.toResize = toResize ;
	}
	
	@Override
	public void componentResized ( ComponentEvent e ) 
	{
		Dimension newSize ;
		newSize = e.getComponent().getPreferredSize () ;
		toResize.setMinimumSize ( newSize ) ;
		toResize.setPreferredSize ( newSize ) ;
		toResize.setMinimumSize ( newSize ) ;
	}
	
}
