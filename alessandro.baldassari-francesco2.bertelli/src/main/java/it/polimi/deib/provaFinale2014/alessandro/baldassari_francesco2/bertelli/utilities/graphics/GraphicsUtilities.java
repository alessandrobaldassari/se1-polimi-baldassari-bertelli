package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public final class GraphicsUtilities 
{

	/***/
	private static GraphicsUtilities instance ;
	
	/***/
	private static Executor exec ;
	
	/***/
	private static Dimension vgaResolution ; 
	
	// make it total.
	/***/
	public static int checkedIntInputWithEscape ( int minVal , int maxVal, int exitVal , int wrongVal , String reqMsg , String errorMsg , PrintStream out , BufferedReader in ) throws IOException
	{
		int res ;		
		out.println ( reqMsg ) ;
		try 
		{
			res = Integer.parseInt ( in.readLine ().trim () ) ;
		}
		catch (NumberFormatException e)
		{
			res = wrongVal ;
		}
		while ( ( res < minVal || res > maxVal ) && res != exitVal  )
		{
			out.println ( errorMsg ) ;
			out.println ( reqMsg ) ;
			try 
			{
				res = Integer.parseInt ( in.readLine ().trim () ) ;
			}
			catch (NumberFormatException e)
			{
				res = wrongVal ;
			}
		}
		return res ;
	}
	
	/***/
	public static int checkedIntInputWithoutEscape ( int minVal , int maxVal, int wrongVal , String reqMsg , String errorMsg , PrintStream out , BufferedReader in ) throws IOException
	{
		int res ;		
		out.println ( reqMsg ) ;
		try 
		{
			res = Integer.parseInt ( in.readLine ().trim () ) ;
		}
		catch (NumberFormatException e)
		{
			res = wrongVal ;
		}
		while ( ( res < minVal || res > maxVal ) )
		{
			out.println ( errorMsg ) ;
			out.println ( reqMsg ) ;
			try 
			{
				res = Integer.parseInt ( in.readLine ().trim () ) ;
			}
			catch (NumberFormatException e)
			{
				res = wrongVal ;
			}
		}
		return res ;
	}
	
	/***/
	public static Dimension getVGAResolution () 
	{
		if ( vgaResolution == null )
			vgaResolution = new Dimension ( 640 , 480 ) ;
		return vgaResolution ;
	}
	
	/***/
	public static Rectangle approximateSimple ( Polygon p ) 
	{
		Rectangle res ;
		res = p.getBounds () ;
		while ( p.contains ( res ) == false )
		{
			res.x = res.x + res.width / 4 ;
			res.y = res.y + res.height / 4 ;
			res.width = res.width / 2 ;
			res.height = res.height / 2 ;
		}
		return res ;
	}
	
	public static Point findHighestY ( Polygon p ) 
	{
		Point max ;
		int i ;
		max = new Point ( p.xpoints [ 0 ] , p.ypoints [ 0 ] ) ;
		for ( i = 0 ; i < p.npoints ; i ++ )
		{
			if ( p.ypoints [ i ] < max.y )
			{
				max.x = p.xpoints [ i ] ;
				max.y = p.ypoints [ i ] ;
			}
		}
		return max ;
	}
	
	/***/
	public static Rectangle approximateComplex ( Polygon p ) 
	{  
		Rectangle boundingBox ;
		Rectangle current ;
		int divider ;
		int multiplier ;
		int w ;
		int h ;
		boolean up ;
		boundingBox = p.getBounds () ;
		current = boundingBox ;
		up = true ;
		divider = 2 ;
		multiplier = 2 ;
		while ( ! p.contains ( current ) )
		{
			// if the last time you enlarged this, reduce 
			if ( up ) 
			{
				// diminuisci le dimensioni
				w = current.width / divider ;
				h = current.height / divider ;
				current = new Rectangle ( current.x + ( boundingBox.width - w ) / 2 , ( boundingBox.height - h ) / 2 , w , h ) ;
				multiplier = multiplier / 2 ;
				up = false ;
			}
			else
			{
				// aumenta le dimensioni
				w = current.width * multiplier ;
				h = current.height * multiplier ;
				current = new Rectangle ( current.x - ( boundingBox.width - w ) / 2 ,  ( boundingBox.height - h ) , w , h ) ;
				divider = divider * 2 ;
				up = true ;
			}
		}
		return current ;
	}
	
	/***/
	public static void setComponentLayoutProperties ( Component component , GridBagLayout layout , int gridX , int gridY , double weightX , double weightY , int gridwidth , int gridheight , int ipadx , int ipady , int fill , int anchor , Insets insets ) 
	{	
		GridBagConstraints gridBagConstraints ;
		gridBagConstraints = new GridBagConstraints () ;
		if ( gridX >=0 && gridY >=0 && weightX >=0 && weightY >= 0 &&  insets != null )
		{
			gridBagConstraints.gridx = gridX ;
			gridBagConstraints.gridy = gridY ;
			gridBagConstraints.weightx = weightX ;
			gridBagConstraints.weighty = weightY ;
			gridBagConstraints.gridwidth = gridwidth ;
			gridBagConstraints.gridheight = gridheight ;
			gridBagConstraints.ipadx = ipadx ;
			gridBagConstraints.ipady = ipady ;
			gridBagConstraints.fill = fill ;
			gridBagConstraints.anchor = anchor ;
			gridBagConstraints.insets = insets ;
			layout.setConstraints ( component , gridBagConstraints ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public static BufferedImage getImage ( String filePath ) throws IOException 
	{ 
		BufferedImage res ; 
		res = ImageIO.read ( new File ( filePath ) ) ;
		return res ;
	}
	
	public static Point getCenterTopLeftCorner ( Dimension windowSize ) 
	{
		Point res ;
		res = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint () ;
		res.x = res.x - windowSize.width / 2 ;
		res.y = res.y - windowSize.height / 2 ;
		return res ;
	}
	
	/***/
	public static BufferedImage scaleImage ( BufferedImage original , int newW , int newH ) 
	{
		BufferedImage resized ;
		Graphics2D g ;
		resized = new BufferedImage ( newW , newH , original.getType());
	    g = resized.createGraphics();
	    g.setRenderingHint ( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    g.drawImage ( original , 0, 0, newW , newH , 0, 0, original.getWidth(), original.getHeight() , null ) ;
	    g.dispose();
	    return resized ;
	}
	
	public static BufferedImage makeImageTranslucent(BufferedImage source, float alpha) 
	{
		    BufferedImage target = new BufferedImage ( source.getWidth() , source.getHeight(), Transparency.TRANSLUCENT);
		    // Get the images graphics
		    Graphics2D g = target.createGraphics();
		    g.setComposite ( AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alpha ) ) ;
		    // Draw the image into the prepared reciver image
		    g.drawImage(source, null, 0, 0);
		    // let go of all system resources in this Graphics
		    g.dispose();
		    // Return the image
		    return target;
		  }
	
	public static void showUnshowWindow ( Window toShow , boolean block , boolean show )
	{
		Runnable r ;
		r = new WindowShowerHiderRunnable ( toShow , show ) ;
		if ( block )
			try
			{
				SwingUtilities.invokeAndWait ( r ) ;
			}
			catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		else
			SwingUtilities.invokeLater ( r ) ;
	}
	
	/***/
	public static void showWindow ( Class windowClass , Object ... args ) 
	{
		Runnable runnable ;
		if ( instance == null )
			instance = new GraphicsUtilities () ;
		if ( windowClass != null && Window.class.isAssignableFrom ( windowClass )  )
			runnable = instance.new WindowLauncherRunnable ( windowClass , args ) ;
		else
			throw new IllegalArgumentException () ;
		if ( exec == null )
			exec = Executors.newCachedThreadPool () ;
		exec.execute ( runnable ) ;
	}
	
	private static class WindowShowerHiderRunnable implements Runnable 
	{
		
		private final Window toShow ;

		private final boolean showHide ;
		
		public WindowShowerHiderRunnable ( Window toShow , boolean showHide ) 
		{
			if ( toShow != null )
			{
				this.toShow = toShow ;
				this.showHide = showHide ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		
		@Override
		public void run () 
		{
			if ( showHide )
				toShow.setVisible ( true ) ;
			else
				toShow.setVisible ( false ) ;
		}
		
	}
	
	
	/***/
	private class WindowLauncherRunnable implements Runnable 
	{
		
		private Class c ;
		
		private Object [] args ;
		
		public WindowLauncherRunnable ( Class windowClass , Object ... args ) 
		{
			if ( windowClass != null && Window.class.isAssignableFrom ( windowClass ) ) 
			{
				this.c = windowClass ;
				if ( args != null )
					this.args = args ;
				else
					this.args = new Object [ 0 ] ;
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
				if ( args.length == 0 )
					args = null ;
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


	
	