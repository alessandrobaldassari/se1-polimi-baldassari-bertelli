package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class GraphicsUtilities 
{

	private static Dimension vgaResolution = new Dimension ( 640 , 480 ) ;
	
	
	public static Dimension getVGAResolution () 
	{
		return vgaResolution ;
	}
	
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
	
	public static Image getImage ( String filePath ) throws IOException 
	{ 
		Image res ; 
		res = ImageIO.read ( new File ( filePath ) ) ;
		return res ;
	}
	
}


	
	