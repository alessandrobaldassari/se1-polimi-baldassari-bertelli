package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public final class GraphicsUtilities 
{

	/***/
	private static GraphicsUtilities instance ;
	
	private static Executor exec ;
	
	/***/
	private static Dimension vgaResolution ; 
	
	// make it total.
	/***/
	public static int checkedIntInput ( int minVal , int maxVal, int exitVal , int wrongVal , String reqMsg , String errorMsg , PrintStream out , BufferedReader in ) throws IOException
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
	public static Dimension getVGAResolution () 
	{
		if ( vgaResolution == null )
			vgaResolution = new Dimension ( 640 , 480 ) ;
		return vgaResolution ;
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


	
	