package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;

public class ImagesHolder 
{
	
	private BufferedImage coverImage ;
	
	private BufferedImage backgroundImage ;
	
	private BufferedImage mapImage ;
	
	private BufferedImage transparentMapImage ;
	
	private BufferedImage diceImage ;
	
	private Map < RegionType , BufferedImage > cardsImages ;
	
	private Map < Integer , BufferedImage > regionImages ;
	
	private Map < Integer , BufferedImage > roadImages ;
	
	private Map < PositionableElementType , BufferedImage > normalPositionableImages ;
	
	private Map < PositionableElementType , BufferedImage > transparentPositionableImages ;
	
	public ImagesHolder ()
	{
		cardsImages = new HashMap < RegionType , BufferedImage > ( GameConstants.NUMBER_OF_REGION_TYPE - 1 ) ;
		regionImages = new HashMap < Integer , BufferedImage > ( GameConstants.NUMBER_OF_REGIONS ) ;
		roadImages = new HashMap < Integer , BufferedImage > ( GameConstants.NUMBER_OF_ROADS ) ;
		normalPositionableImages = new HashMap < PositionableElementType , BufferedImage > () ;
		transparentPositionableImages = new HashMap < PositionableElementType , BufferedImage > () ;
		loadImages () ;
		makeImagesTransparent () ;
	}

	private void loadImages () 
	{
		BufferedImage im ;
		int i ;
		try 
		{
			coverImage = GraphicsUtilities.getImage ( FilePaths.COVER_IMAGE_PATH ) ;
			backgroundImage = GraphicsUtilities.getImage ( FilePaths.BACKGROUND_IMAGE_PATH ) ;
			mapImage = GraphicsUtilities.getImage ( FilePaths.MAP_IMAGE_PATH ) ;
			diceImage = GraphicsUtilities.getImage ( FilePaths.DICE_IMAGE_PATH ) ;
			for ( RegionType r : RegionType.values () )
				if ( r != RegionType.SHEEPSBURG )
				{
					im = GraphicsUtilities.getImage( r.getHumanName() + ".png" ) ;
					cardsImages.put ( r , im ) ;
				}
			for ( i = 0 ; i < GameConstants.NUMBER_OF_REGIONS ; i ++ )
			{
				im = GraphicsUtilities.getImage ( ( i+1 ) + ".png" ) ;
				regionImages.put ( ( i + 1 ) , im ) ;
			}
			for ( i = 0 ; i < 6 ; i ++ )
			{
				im = GraphicsUtilities.getImage ( "road_" + ( i + 1 ) + ".png" ) ;
				roadImages.put ( ( i + 1 ) , im ) ;
			}
			im = GraphicsUtilities.getImage ( FilePaths.FENCE_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.FENCE , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.BLACK_SHEEP_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.BLACK_SHEEP , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.LAMB_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.LAMB , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.SHEEP_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.SHEEP , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.WOLF_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.WOLF , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.RED_SHEPERD_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.RED_SHEPERD , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.BLUE_SHEPERD_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.BLUE_SHEPERD , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.YELLOW_SHEPERD_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.YELLOW_SHEPERD , im ) ;
			im = GraphicsUtilities.getImage ( FilePaths.GREEN_SHEPERD_IMAGE_PATH ) ;
			normalPositionableImages.put ( PositionableElementType.GREEN_SHEPERD , im ) ;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private void makeImagesTransparent () 
	{
		BufferedImage im ;
		transparentMapImage = GraphicsUtilities.makeImageTranslucent ( mapImage , 0.5f ) ;
		for ( PositionableElementType p : normalPositionableImages.keySet () )
		{
			im = GraphicsUtilities.makeImageTranslucent ( normalPositionableImages.get ( p ) , 0.5f ) ;
			transparentPositionableImages.put ( p , im ) ; 
		}
	}
	
	public Image getCoverImage () 
	{
		return coverImage ;
	}
	
	public Image getBackgroundImage () 
	{
		return backgroundImage ;
	}

	public Image getMapImage ( boolean transparent ) 
	{
		Image res ;
		if ( transparent )
			res = transparentMapImage ;
		else
			res = mapImage ;
		return res ;
	}
	
	public Image getDiceImage () 
	{
		return diceImage ;
	}

	public Image getCardImage ( RegionType r ) 
	{
		return cardsImages.get(r);
	}
	
	public Image getRegionImage ( int regionUID ) 
	{
		return regionImages.get ( regionUID ) ;
	}
	
	public Image getRoadImage ( int roadNumber ) 
	{
		return regionImages.get(roadNumber);
	}
	
	public BufferedImage getPositionableImage ( PositionableElementType key , boolean transparent ) 
	{
		BufferedImage res ;
		if ( transparent )
			res =  transparentPositionableImages.get ( key ) ;
		else
			res = normalPositionableImages.get ( key ) ;
		return res ;
	}
	
	public void zoomMapViewImages ( float xFactor , float yFactor ) 
	{
		
	}
	
}
