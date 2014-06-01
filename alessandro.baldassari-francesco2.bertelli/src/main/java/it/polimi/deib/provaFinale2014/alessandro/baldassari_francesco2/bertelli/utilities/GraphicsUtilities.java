package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

public final class GraphicsUtilities 
{

	/***/
	private static GraphicsUtilities instance ;
	
	/***/
	private static Dimension vgaResolution ; 
	
	/***/
	public static Dimension getVGAResolution () 
	{
		if ( vgaResolution == null )
			vgaResolution = new Dimension ( 640 , 480 ) ;
		return vgaResolution ;
	}
	
	/***/
	public static void setComponentLayoutProperties ( Component component , GridBagLayout layout , int gridx , int gridy , double weightx , double weighty , int gridwidth , int gridheight , int ipadx , int ipady , int fill , int anchor , Insets insets ) 
	{	
		GridBagConstraints gridBagConstraints ;
		gridBagConstraints = new GridBagConstraints () ;
		gridBagConstraints.gridx = gridx ;
		gridBagConstraints.gridy = gridy ;
		gridBagConstraints.weightx = weightx ;
		gridBagConstraints.weighty = weighty ;
		gridBagConstraints.gridwidth = gridwidth ;
		gridBagConstraints.gridheight = gridheight ;
		gridBagConstraints.ipadx = ipadx ;
		gridBagConstraints.ipady = ipady ;
		gridBagConstraints.fill = fill ;
		gridBagConstraints.anchor = anchor ;
		gridBagConstraints.insets = insets ;
		layout.setConstraints ( component , gridBagConstraints ) ;
	}
	
	/***/
	public static Image getImage ( String filePath ) throws IOException 
	{ 
		Image res ; 
		res = ImageIO.read ( new File ( filePath ) ) ;
		return res ;
	}
	
	public static void showWindow ( Class windowClass , Object ... args ) 
	{
		Runnable runnable ;
		if ( instance == null )
			instance = new GraphicsUtilities () ;
		runnable = instance.new WindowLauncherRunnable ( windowClass , args ) ;
	}
	
	/***/
	public static void placeInCenter ( Window w ) 
	{
		Point c ;
		c = GraphicsEnvironment.getLocalGraphicsEnvironment ().getCenterPoint () ;
		c.x = c.x -  w.getWidth () / 2 ;
		c.y = c.x - w.getHeight () / 2 ;
		w.setLocation ( c );
	}
	
	/***/
	private class WindowLauncherRunnable implements Runnable 
	{
		
		private Class c ;
		
		private Object [] args ;
		
		public WindowLauncherRunnable ( Class windowClass , Object ... args ) 
		{
			if ( windowClass != null && args != null )
			{
				this.c = windowClass ;
				this.args = args ;
			}
		}
		
		@Override
		public void run () 
		{
			Window w ;
			Constructor ctor ; 
			try 
			{
				ctor = c.getConstructor( Utilities.getTypes ( args ) ) ;
				w = ( Window ) ctor.newInstance( args ) ;
				w.setVisible ( true ) ;
			}
			catch ( NoSuchMethodException e ) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
			catch ( SecurityException e ) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;

			}
			catch ( InstantiationException e ) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;

			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
			catch ( IllegalArgumentException e ) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
			catch ( InvocationTargetException e ) 
			{
				e.printStackTrace();
				throw new RuntimeException ( e ) ;
			}
		}
		
	}
	
}


	
	