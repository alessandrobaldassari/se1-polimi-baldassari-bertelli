package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.junit.Test;

public class GraphicsUtilitiesTest 
{
	
	public static final double DOUBLE_SENS = 0.00001 ;
	
	@Test
	public void getVGAResolution () 
	{
		Dimension d ;
		Dimension out ;
		d = new Dimension ( 640 , 480 ) ;
		out = GraphicsUtilities.getVGAResolution () ;
		assertEquals ( d , out ) ;
	}

	@Test
	public void setComponentLayoutProperties1 () 
	{
		GridBagConstraints c ;
		GridBagLayout g ;
		JComponent x ;
		Insets insets ;
		int gridX ;
		int gridY ;
		double wX ;
		double wY ;
		int gX ;
		int gY ;
		int iX ;
		int iY ;
		int fill ;
		int an ;
		g = new GridBagLayout () ;
		x = new JPanel () ;
		insets = new Insets ( 0 , 1 , 4 , 5 ) ;
		gridX = 3 ;
		gridY = 4 ;
		wX = 0.5 ;
		wY = 0.75 ;
		gX = 4 ;
		gY = 3 ;
		iX = 0 ;
		iY = 0 ;
		fill = GridBagConstraints.BOTH ;
		an = GridBagConstraints.CENTER ;
		GraphicsUtilities.setComponentLayoutProperties ( x , g, gridX, gridY, wX , wY , gX , gY , iX, iY , fill, an, insets );
		c = g.getConstraints ( x ) ;
		assertEquals ( c.gridx , gridX ) ;
		assertEquals ( c.gridy , gridY ) ;
		assertEquals ( c.weightx , wX , DOUBLE_SENS ) ;
		assertEquals ( c.weighty , wY , DOUBLE_SENS ) ;
		assertEquals ( c.gridwidth , gX ) ;
		assertEquals ( c.gridheight , gY ) ;
		assertEquals ( c.ipadx , iX ) ;
		assertEquals ( c.ipady , iY ) ;
		assertEquals ( c.fill , fill ) ;
		assertEquals ( c.anchor , an ) ;
		assertEquals ( c.insets , insets ) ;
	}

	@Test ( expected = IllegalArgumentException.class )
	public void setComponentLayoutProperties2 () 
	{
		GraphicsUtilities.setComponentLayoutProperties ( new JPanel () , new GridBagLayout () , - 4 , 0 , 0.0 , 0.0 , 0 , 0  , 0 , 0 , 0 , 0 , null );
	} 

	@Test
	public void getImage1 () throws IOException 
	{
		BufferedImage i ;
		i = GraphicsUtilities.getImage ( "images/sheepland_map.jpg" ) ;
		assertTrue ( i != null ) ;
	}
	
	@Test ( expected = IOException.class )
	public void getImage2 () throws IOException 
	{ 
		BufferedImage i ;
		i = GraphicsUtilities.getImage ( "not existing image" ) ;
	}
	
	@Test ( expected = IllegalArgumentException.class )
	public void showWindow () 
	{
		GraphicsUtilities.showWindow ( String.class , "Prova" ) ;
	}
	
}
