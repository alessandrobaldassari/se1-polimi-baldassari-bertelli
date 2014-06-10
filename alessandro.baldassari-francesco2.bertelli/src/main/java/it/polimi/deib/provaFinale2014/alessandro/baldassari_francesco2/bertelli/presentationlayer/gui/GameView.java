package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElement.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElement.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Counter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableFrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

/**
 * This class is a JFrame wrapper for the GameView. 
 */
public class GameView extends JFrame
{

	/**
	 * The GameViewPanel object that will render the the GameView. 
	 */
	private GameViewPanel gameViewPanel ;

	/***/
	public GameView () 
	{
		super () ;
		GridBagLayout g ;
		Insets insets ;
		gameViewPanel = new GameViewPanel () ; 
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		GraphicsUtilities.setComponentLayoutProperties ( gameViewPanel , g , 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( EXIT_ON_CLOSE ) ;
		setLayout ( g ) ;
		setResizable ( true ) ;
		add ( gameViewPanel ) ;
	}

	/***/
	public void addNotification ( String notif ) 
	{
		gameViewPanel.addNotification(notif);
	} 

	public static void main ( String [] args ) 
	{
		GameView g ;
		g = new GameView () ;
		g.setExtendedState ( GameView.MAXIMIZED_BOTH );
		g.setVisible ( true ) ;
	}
	
}

/**
 * The true View for the Game 
 */
class GameViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/**
	 * A PlayersCardView to manage the Cards a User owns. 
	 */
	private PlayersCardViewPanel playersCardPanel ;
	
	/**
	 * A MapView to render the Map and let the User interact with it. 
	 */
	private MapViewPanel mapPanel ;
	
	/**
	 * A panel to show some notifications to the User. 
	 */
	private NotificationPanel notificationArea ;
	
	/**
	 * A PlayersMoveView to manage the  move a User can do.
	 */
	private PlayersMoveViewPanel playersMovePanel ;
	
	/***/
	protected GameViewPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		playersCardPanel = new PlayersCardViewPanel () ;
		mapPanel = new MapViewPanel () ;
		notificationArea = new NotificationPanel();
		playersMovePanel = new PlayersMoveViewPanel () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( playersCardPanel , 0 , 0 , 1 , 1 , 2 , 1 , 0 , 0 , GridBagConstraints.VERTICAL , GridBagConstraints.WEST , insets ) ;
		layoutComponent ( mapPanel , 1 , 0 , 4 , 1 , 2 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( notificationArea , 2 , 0 , 1 , 0.5 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.EAST , insets ) ;
		layoutComponent ( playersMovePanel , 2 , 1 , 1 , 0.5 , 1 , 1 , 0 , 0 , GridBagConstraints.VERTICAL , GridBagConstraints.EAST , insets ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( playersCardPanel ) ;
		add ( mapPanel ) ;
		add ( notificationArea ) ;
		add ( playersMovePanel ) ;
	}
	
	/**
	 * Show some notification to the User.
	 */
	public void addNotification ( String msg ) 
	{
		notificationArea.addNotification(msg); 
	}
	
}

/**
 * A comoponent that manage the rendering of the Map and the interaction of the User with it. 
 */
class MapViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel  
{

	/**
	 * A split pane to show both the map and some commands button about zoom and other things. 
	 */
	private JSplitPane splitPane ;
	
	/**
	 * A Scroll Pane to scroll the Map. 
	 */
	private JScrollPane scrollPane ;
	
	/**
	 * A Drawing Panel to effectively draw the Map.
	 */
	private DrawingPanel drawingPanel ;
	
	/**
	 * A Command Panel to host some commands about the Map ( zoom and other stuff ).
	 */
	private CommandPanel commandPanel ;
	
	/**
	 * A component to manage the coordinates of the Map and the Objects on the Map. 
	 */
	private MapMeasurementCoordinatesManager coordinatesManager ;
	
	/***/
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	/***/
	protected MapViewPanel () 
	{
		super () ;	
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		splitPane = new JSplitPane () ;
		scrollPane = new JScrollPane () ;
		drawingPanel = new DrawingPanel () ;
		commandPanel = new CommandPanel () ;	
		coordinatesManager = new MapMeasurementCoordinatesManager () ;
		positionableElementsManager = new PositionableElementCoordinatesManager () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		splitPane.setTopComponent ( scrollPane ) ;
		splitPane.setBottomComponent ( commandPanel );
		splitPane.setOrientation ( JSplitPane.VERTICAL_SPLIT );
		splitPane.setOneTouchExpandable ( true ) ;
		scrollPane.setViewportView ( drawingPanel ) ;
		scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED ) ;
		scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;
		layoutComponent ( splitPane, 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;	
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		ComponentListener componentListener ;
		componentListener = new ResizingManager () ;
		addComponentListener ( componentListener );
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents() 
	{
		add ( splitPane ) ;
	}
	
	/**
	 * The class that effectively draw the GameMap on the screen. 
	 */
	private class DrawingPanel extends JPanel 
	{
		
		/**
		 * A Map containing the images of the elements to draw on the GameMap.
		 */
		private Map < PositionableElementType , BufferedImage > positionableElementsImages ;
		
		/**
		 * The image representing the GameMap on the screen. 
		 */
		private BufferedImage backgroundImage ;
		
		/**
		 * The preferred Dimension of this component. 
		 */
		private Dimension preferredDimension ;
		
		/***/
		public DrawingPanel () 
		{
			super () ;
			try 
			{
				positionableElementsImages = new HashMap < PositionableElementType , BufferedImage > ( PositionableElementType.values().length ) ;
				backgroundImage = GraphicsUtilities.getImage ( "sheepland_map.jpg" ) ;
				positionableElementsImages.put ( PositionableElementType.FENCE , GraphicsUtilities.getImage ( "sheepland_fence.jpg" ) ) ;
				positionableElementsImages.put ( PositionableElementType.SHEPERD ,GraphicsUtilities.getImage ( "sheepland_sheperd.png" )) ;
				positionableElementsImages.put ( PositionableElementType.WOLF , GraphicsUtilities.getImage ( "sheepland_wolf.jpg" ) ) ;
				positionableElementsImages.put ( PositionableElementType.LAMB , GraphicsUtilities.getImage ( "sheepland_lamb.jpg" ) ) ;
				positionableElementsImages.put ( PositionableElementType.RAM , GraphicsUtilities.getImage ( "sheepland_ram.jpg" ) ) ;
				positionableElementsImages.put ( PositionableElementType.SHEEP , GraphicsUtilities.getImage ( "sheepland_sheep.jpg" ) ) ;
				positionableElementsImages.put ( PositionableElementType.BLACK_SHEEP , GraphicsUtilities.getImage ( "sheepland_black_sheep.png" ) ) ;
				preferredDimension = new Dimension ( backgroundImage.getWidth() , backgroundImage.getHeight() ) ;
				setDoubleBuffered ( true ) ;
				setLayout ( null ) ;
				setOpaque ( false );
			} 
			catch ( IOException e ) 
			{
				e.printStackTrace () ;
				throw new RuntimeException ( e ) ;
			}
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public Dimension getPreferredSize () 
		{
			return preferredDimension ;
		}
		
		/**
		 * AS THE SUPER'S ONE.
		 */
		@Override
		public void paintComponent ( Graphics g ) 
		{ 	
			Map < Integer , Map < PositionableElementType , Counter > > dataMap ;
			Map < PositionableElementType , Counter > specData ;
			Rectangle boundingBox ;
			Polygon p ;
			Ellipse2D e ;
			BufferedImage toDraw ;
			int figH ;
			int i ;
			int x0 ;
			int y0 ;
			super.paintComponent ( g ) ;
			if ( backgroundImage.getWidth () < getWidth () ) 
				x0 = ( getWidth () - backgroundImage.getWidth() ) / 2 ;
			else 
				x0 = 0;
			if ( backgroundImage.getHeight() < getHeight () )
				y0 = ( getHeight () - backgroundImage.getHeight() ) / 2 ;
			else
				y0 = 0 ;
			// draw the map
			g.drawImage ( backgroundImage , x0 , y0 , backgroundImage.getWidth() , backgroundImage.getHeight() , this );
			// draw animals in the regions 
			// retrieve data about regions
			dataMap = positionableElementsManager.getData ( GameMapElementType.REGION ) ;
			// for each region
			g.setFont ( new Font ( "Arial" , Font.ITALIC , 10 ) ) ;
			g.setColor ( Color.RED ) ;
			for ( i = 1 ; i <= GameMap.NUMBER_OF_REGIONS ; i ++ )
			{
				p = coordinatesManager.getRegionBorder ( i ) ;
				specData = dataMap.get ( i ) ;
				// if there is someone in this region
				if ( specData != null )
				{
					boundingBox = p.getBounds () ;
					figH = boundingBox.height / specData.size () ;
					int u ;
					u = boundingBox.height ;
					// effectively draw images.
					for ( PositionableElementType t : specData.keySet() ) 
					{
						toDraw = positionableElementsImages.get ( t ) ;
						g.drawImage ( toDraw , boundingBox.x , u , boundingBox.width , figH , this ) ;
						g.drawString ( specData.get ( t ).getValue() + "" , boundingBox.x , u + figH - 20 ) ;
						u = u + figH ;
					}
				}
			}
			// draw sheperds and fences in the roads
			// retrieve data about roads
			dataMap = positionableElementsManager.getData ( GameMapElementType.ROAD ) ;
			for ( i = 1 ; i <= GameMap.NUMBER_OF_ROADS ; i ++ )
			{
				e = coordinatesManager.getRoadBorder ( i ) ;
				specData = dataMap.get ( i ) ;
				// it there is someone in
				if ( specData != null )
				{
					toDraw = positionableElementsImages.get ( specData.keySet().iterator().next () ) ;
					x0 = ( int ) ( e.getMinX() + ( getWidth() - backgroundImage.getWidth() ) /2 ) ;
					y0 = ( int ) e.getMinY() ;
					g.drawImage ( toDraw , x0, y0 , coordinatesManager.ROADS_RADIUS * 2 , coordinatesManager.ROADS_RADIUS * 2 , this ) ;
				}
			}
		}
		
		/**
		 * Zooms this GameView, the GameMap it represents and also every component drawed on it. 
		 * 
		 * @param xFactor the factor of the zoom in the x direction.
		 * @param yFactor the factor of the zoom in the y direction.
		 */
		public void zoom ( float xFactor , float yFactor ) 
		{
			BufferedImage original ;
			int newW ;
			int newH ;
			original = backgroundImage ;
			newW = new Double ( original.getWidth() * xFactor ).intValue () ;
			newH = new Double ( original.getHeight () * yFactor ).intValue () ;
			preferredDimension.width = newW ;
			preferredDimension.height = newH ;
			backgroundImage = GraphicsUtilities.scaleImage ( original , newH , newH ) ;
		    SwingUtilities.updateComponentTreeUI ( MapViewPanel.this ) ;
		    coordinatesManager.scale ( xFactor , yFactor ) ; 
		}
		
	}

	/**
	 * A class that manages all the commands associated with a MapView.
	 */
	private class CommandPanel extends FrameworkedWithGridBagLayoutPanel 
	{
		
		/**
		 * Button for the zoom less action. 
		 */
		private JButton zoomLessButton ;
		
		/**
		 * Button for the zoom plus action. 
		 */
		private JButton zoomPlusButton ;

		/***/
		public CommandPanel () 
		{
			super () ;
		}
		
		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		protected void createComponents () 
		{
			zoomLessButton = new JButton () ;
			zoomPlusButton = new JButton () ;
			
		}

		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		protected void manageLayout () 
		{
			Insets insets ;
			insets = new Insets ( 0 , 0 , 0 , 0 ) ;
			layoutComponent ( zoomLessButton , 0 , 0 , 0.5 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( zoomPlusButton , 1 , 0 , 0.5 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;			
		}

		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		protected void bindListeners() 
		{
			Action zoomLessAction ;
			Action zoomPlusAction ;
			zoomLessAction = new ZoomLessAction () ;
			zoomPlusAction = new ZoomPlusAction () ;
			zoomLessButton.setAction ( zoomLessAction ) ;
			zoomPlusButton.setAction ( zoomPlusAction ) ;	
		}

		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		protected void injectComponents () 
		{
			add ( zoomLessButton ) ;
			add ( zoomPlusButton ) ;	
		}
		
		/**
		 * Action that manages the zoom less action. 
		 */
		private class ZoomLessAction extends AbstractAction 
		{

			private static final String LABEL = "ZOOM -" ;
			
			/***/
			public ZoomLessAction () 
			{
				super ( LABEL ) ;
			}
			
			/**
			 * AS THE SUPER'S ONE 
			 */
			@Override
			public void actionPerformed ( ActionEvent e ) 
			{
				drawingPanel.zoom ( 0.5f , 0.5f ) ;
			}
			
		}
		
		/**
		 * Action that manages the zoom plus action. 
		 */
		private class ZoomPlusAction extends AbstractAction 
		{
			
			private static final String LABEL = "ZOOM +" ;
			
			/***/
			public ZoomPlusAction () 
			{
				super ( LABEL ) ;
			}

			/**
			 * AS THE SUPER'S ONE. 
			 */
			@Override
			public void actionPerformed ( ActionEvent e ) 
			{
				drawingPanel.zoom ( 2f , 2f ) ;	
			}
					
		}
		
	}
	
	/**
	 * This class implements a mechanism that has to fix the proportion of the split pane to 3 : 4 
	 */
	private class ResizingManager extends ComponentAdapter 
	{
		
		public ResizingManager () 
		{
			super () ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void componentResized ( ComponentEvent e ) 
		{
			splitPane.setDividerLocation ( 0.95 ) ;
		}
		
	} 
	
	/**
	 * Class that manages the measures of the GameMap.
	 */
	private class MapMeasurementCoordinatesManager
	{
	
		/***/
		private final String REGIONS_CSV_FILE_PATH = "regionsPerimiterPoints.csv" ;
		
		/***/
		private final String ROADS_CSV_FILE_PATH = "roadsCentralPoints.csv" ;
		
		/***/
		int ROADS_RADIUS = 25 ;
	
		/***/
		private Map < Integer , Polygon > regionsCoordinates ;
		
		/***/
		private Map < Integer , Ellipse2D > roadsCoordinates ;
		
		/***/
		public MapMeasurementCoordinatesManager () 
		{
			InputStream regionsI ;
			InputStream roadsI ;
			try 
			{
				regionsI = Files.newInputStream ( Paths.get ( REGIONS_CSV_FILE_PATH ) , StandardOpenOption.READ ) ;
				roadsI = Files.newInputStream ( Paths.get ( ROADS_CSV_FILE_PATH ) , StandardOpenOption.READ ) ;
				regionsCoordinates = readRegionsCoordinates ( regionsI ) ;
				regionsI.close () ;
				roadsCoordinates = readRoadsCoordinates ( roadsI ) ;
				roadsI.close () ;
			}
			catch ( IOException e ) 
			{
				e.printStackTrace();
			}
		}
		
		/***/
		private Map < Integer , Polygon > readRegionsCoordinates ( InputStream in ) 
		{
			String [] values ;
			Map < Integer , Polygon > res ;
			Polygon p ;
			Scanner s ;
			String rawEntry ;
			int i ;
			res = new HashMap < Integer , Polygon > ( 19 ) ;
			s = new Scanner ( in ) ;
			while ( s.hasNextLine () )
			{
				rawEntry = s.nextLine () ;
				values = rawEntry.split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
				p = new Polygon () ;
				for ( i = 1 ; i < values.length ; i = i + 2 )
					p.addPoint ( Integer.parseInt ( values [ i ] ) , Integer.parseInt ( values [ i + 1 ] ) ) ;
				res.put ( Integer.parseInt ( values [ 0 ] ) , p ) ;
			}
			s.close () ;
			return res ;
		}
		
		/***/
		private Map < Integer , Ellipse2D > readRoadsCoordinates ( InputStream in ) 
		{
			String [] data ;
			Map < Integer , Ellipse2D > res ;
			Scanner s ;
			Ellipse2D e ;
			String rawEntry ;
			res = new HashMap < Integer , Ellipse2D > ( 42 ) ;
			s = new Scanner ( in ) ;
			while ( s.hasNextLine () )
			{
				rawEntry = s.nextLine () ;
				data = rawEntry.split ( Utilities.CSV_FILE_FIELD_DELIMITER ) ;
				e = new Ellipse2D.Float ( Integer.parseInt ( data [ 1 ] ) - ROADS_RADIUS , Integer.parseInt ( data [ 2 ] ) - ROADS_RADIUS , ROADS_RADIUS * 2 , ROADS_RADIUS * 2 ) ;
				res.put ( Integer.parseInt ( data [ 0 ] ) , e ) ;
			}
			s.close () ;
			return res ;
		} 
		
		/***/
		public void scale ( float xFactor , float yFactor ) 
		{
			int i ;
			for ( Polygon p : regionsCoordinates.values() )
				for ( i = 0 ; i < p.npoints ; i ++ )
				{
					p.xpoints [ i ] = Math.round ( p.xpoints [ i ] * xFactor ) ;
					p.ypoints [ i ] = Math.round (p.ypoints [ i ] * yFactor) ;
				}
			for ( Ellipse2D p : roadsCoordinates.values () )
				p.setFrame ( p.getX () * xFactor , p.getY () * yFactor , p.getWidth () * xFactor , p.getHeight () * yFactor ) ;
			ROADS_RADIUS = (int) (ROADS_RADIUS * xFactor) ;
		}
 		
		/***/
		public Polygon getRegionBorder ( int regionUID )
		{
			return regionsCoordinates.get ( regionUID ) ;
		}
		
		/***/
		public Ellipse2D getRoadBorder ( int roadUID ) 
		{
			return roadsCoordinates.get ( roadUID ) ;
		}
		
	}
	
	/**
	 * Class that manages the measures of the Elements which are on the GameMap. 
	 */
	private class PositionableElementCoordinatesManager implements GameMapObserver
	{
		
		// ATTRIBUTES
		
		/**
		 * A Map object that contains the element to show on the Map; the semantics is :
		 * key : first object an Element Type ( Sheep, Wolf, ... ), second object it's uid.
		 * value: first object a GameElementType ( Region or Roads), second object it's uid.  
		 */
		private Map < Couple < PositionableElementType , Integer > , Couple < GameMapElementType , Integer > > elems ; 
		
		// METHODS
		
		/***/
		public PositionableElementCoordinatesManager () 
		{
			elems = new LinkedHashMap < Couple < PositionableElementType , Integer > , Couple < GameMapElementType , Integer > > () ;
		}

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void onPositionableElementAdded ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId ) 
		{
			elems.put ( new Couple < PositionableElementType , Integer > ( whoType , whoId ) , new Couple < GameMapElementType , Integer > ( whereType , whereId ) ) ;
		}

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void onPositionableElementRemoved ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId )
		{
			elems.remove ( new Couple < PositionableElementType , Integer > ( whoType , whoId ) ) ;
		}
		
		/**
		 * 
		 * 
		 * @param whereType the GameMapElementType to search data about.
		 * @return a Map whose semantics is the sequent :
		 * 	       key : the uid of a GameMapElementType 
		 * 		   value : a Map containing, for each ElementType, the number of instances in the selected place.
		 */
		public Map < Integer , Map < PositionableElementType , Counter > > getData ( GameMapElementType whereType ) 
		{
			Map < Integer , Map < PositionableElementType , Counter > > res ;
			Couple < GameMapElementType , Integer > out ;
			res = new LinkedHashMap < Integer, Map < PositionableElementType , Counter > > () ;
			for ( Couple < PositionableElementType , Integer > in : elems.keySet () )
			{
				out = elems.get ( in ) ;
				// if the out object is involved into the request
				if ( out.getFirstObject() == whereType )
				{
					// if there is no entry for this Position
					if ( res.containsKey ( out.getSecondObject() ) == false )
						res.put ( out.getSecondObject() , new LinkedHashMap < PositionableElementType , Counter > () ) ;
					// if, in the selected position index, there is a type as the one specified by in yet, incrent
					if ( res.get ( out.getSecondObject() ).containsKey ( in.getFirstObject() ) )
						res.get ( out.getSecondObject() ).get ( in.getFirstObject () ).increment () ;
					else
						res.get ( out.getSecondObject () ).put ( in.getFirstObject () , new Counter ( 1 ) ) ;
				}
			}
			return res ;
		}
		
	}
	
}

class PlayersCardViewPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	protected PlayersCardViewPanel () 
	{
		super () ;
	}

	@Override
	protected void createComponents () {}

	@Override
	protected void manageLayout() {}

	@Override
	protected void bindListeners () {}

	@Override
	protected void injectComponents () {}

}

class PlayersMoveViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	PlayersMoveViewPanel () 
	{
		super () ;
	}
	
	@Override
	protected void createComponents () {}

	@Override
	protected void manageLayout () {}

	@Override
	protected void bindListeners () {}

	@Override
	protected void injectComponents () {}

}
