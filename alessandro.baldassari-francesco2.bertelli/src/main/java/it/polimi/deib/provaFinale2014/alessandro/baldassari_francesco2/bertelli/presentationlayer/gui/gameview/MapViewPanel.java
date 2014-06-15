package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Counter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

/**
 * A component that manage the rendering of the Map and the interaction of the User with it. 
 */
public class MapViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel < GameMapViewObserver > implements GameMapObserver 
{

	// ATTRIBUTES
	
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
	private MapMeasuresCoordinatesManager coordinatesManager ;
	
	/**
	 * The object that manages the position of elements on the GameMap. 
	 */
	private PositionableElementCoordinatesManager positionableElementsManager ;
		
	/**
	 * The input mode this GameView is, null if none. 
	 */
	private GameViewInputMode currentInputMode ;
	
	/***/
	private boolean highlightMode ;
	
	/***/
	private Iterable < Integer > toHighlight ;
	
	// METHODS
	
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
		scrollPane = new JScrollPane () ;
		drawingPanel = new DrawingPanel () ;
		commandPanel = new CommandPanel () ;	
		positionableElementsManager = new PositionableElementCoordinatesManager () ;
		coordinatesManager = new MapMeasuresCoordinatesManager ( positionableElementsManager ) ;
		currentInputMode = null ;
		highlightMode = false ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		scrollPane.setViewportView ( drawingPanel ) ;
		scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED ) ;
		scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;
		layoutComponent ( scrollPane, 0 , 0 , 1 , 12.5 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;	
		layoutComponent ( commandPanel, 0 , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.SOUTH , insets ) ;	
		setOpaque ( false ) ;
		scrollPane.setOpaque(false); 
		drawingPanel.setOpaque(false); 
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
	protected void injectComponents() 
	{
		add ( scrollPane ) ;
		add ( commandPanel ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , 
			PositionableElementType whoType , Integer whoId ) 
	{
		System.out.println ( "ELEMENT ADDED\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\\n\n\n" ) ;
		positionableElementsManager.addElement(whereType, whereId, whoType, whoId);
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onPositionableElementRemoved(GameMapElementType whereType , Integer whereId , 
			PositionableElementType whoType , Integer whoId) 
	{
		System.out.println ( "ELEMENT ADDED\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\\n\n\n" ) ;
		positionableElementsManager.removeElement ( whereType , whereId , whoType , whoId ) ;
	}

	/**
	 * Set the current input mode for this MapViewPanel 
	 * 
	 * @param currentInputMode the value for the currentInputModeProperty.
	 */
	public void setCurrentInputMode ( GameViewInputMode currentInputMode ) 
	{
		this.currentInputMode = currentInputMode ;
	}
	
	/***/
	public GameViewInputMode getCurrentInputMode () 
	{
		return currentInputMode ;
	}
	
	/***/
	public void beginHighlightVisualization ( Iterable < Integer > toShowElems ) 
	{
		highlightMode = true ;
		toHighlight = toShowElems ;	
	}
	
	// INNER CLASSES
	
	/**
	 * The class that effectively draw the GameMap on the screen. 
	 */
	private class DrawingPanel extends JPanel 
	{
		
		// ATTRIBUTES
		
		/**
		 * The preferred Dimension of this component. 
		 */
		private Dimension preferredDimension ;
		
		// METHODS
		
		/***/
		public DrawingPanel () 
		{
			super () ;
			Image bI ;
			bI = SheeplandClientApp.getInstance().getImagesHolder().getMapImage(false);
			preferredDimension = new Dimension ( bI.getWidth ( this ) , bI.getHeight( this ) ) ;
			setDoubleBuffered ( true ) ;
			setLayout ( null ) ;
			setOpaque ( false );
			addMouseListener ( new ClickManager () );
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
			super.paintComponent ( g ) ;
			// general settings
			g.setFont ( new Font ( "Arial" , Font.ITALIC , 10 ) ) ;
			g.setColor ( Color.RED ) ; 
			// draw the background
			drawBackground ( g ) ;
			// draw regions elements
			drawRegionsElement ( g ) ;
			// draw roads elements
			drawRoadsElements ( g ) ;
			// region highlight effects
			drawRegionsHighlighted ( g ) ;
			// road highlight effects
			drawRoadHighlighted ( g ) ;
		}
		
		/***/
		private Point generateTopLeftCorner () 
		{
			Image backgroundImage ;
			Point res ;
			res = new Point ( 0 , 0 ) ;
			backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( highlightMode ) ;
			res = new Point ( 0 , 0 ) ;
			// consider the Map may not fill the entire space offered by this panel.
			if ( backgroundImage.getWidth ( this ) < getWidth () ) 
				res.x = ( getWidth () - backgroundImage.getWidth ( this ) ) / 2 ;
			if ( backgroundImage.getHeight ( this ) < getHeight () )
				res.y = ( getHeight () - backgroundImage.getHeight ( this ) ) / 2 ;
			return res ;
		}
		
		/***/
		private void drawBackground ( Graphics g ) 
		{
			Image backgroundImage ;
			Point tlc ;
			backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( highlightMode ) ;	
			tlc = generateTopLeftCorner() ;
			// draw the map
			g.drawImage ( backgroundImage , tlc.x , tlc.y , backgroundImage.getWidth ( this ) , backgroundImage.getHeight ( this ) , this );
		}

		/***/
		private void drawRegionsElement ( Graphics g ) 
		{
			Map < Integer , Map < PositionableElementType , Counter > > dataMap ;
			Map < PositionableElementType , Counter > specData ;
			BufferedImage toDraw ;
			Rectangle boundingBox ;
			Shape shape ;
			int currentHeight ;
			int imageWidth ;
			int imageHeight ;
			int i ;
			boolean transparent ;
			// retrieve data about regions
			dataMap = positionableElementsManager.getData ( GameMapElementType.REGION ) ;
			// for each region
			for ( i = 1 ; i <= GameConstants.NUMBER_OF_REGIONS ; i ++ )
			{
				// retrieve the coordinates of this region
				shape = coordinatesManager.getRegionBorder ( i ) ;
				// retrieve data about animals in the region.
				specData = dataMap.get ( i ) ;
				// if there is someone in this region
				if ( specData != null )
				{
					// approximate the region with a Rectangle.
					boundingBox = shape.getBounds () ;
					// determine an acceptable value for the height of the images.
					imageHeight = boundingBox.height / ( 2 * specData.size () ) ;
					// determine an acceptable value for the width of the images.
					imageWidth = boundingBox.width / ( 2 * specData.size ()) ;
					// begin draw in the top left corner.
					currentHeight = 0 ;
					// for each Type of Animal in the region
					for ( PositionableElementType t : specData.keySet() ) 
					{
						// determine if this image has to be transparent or not.
						transparent = highlightMode && ( ( currentInputMode != GameViewInputMode.ANIMALS ) || ( currentInputMode == GameViewInputMode.ANIMALS && CollectionsUtilities.contains ( toHighlight , i ) ) ) ; 
						// retrieve the image to draw
						toDraw = SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage( t , transparent ) ; 
						// draw the image
						g.drawImage ( toDraw , boundingBox.x , currentHeight , imageWidth , imageHeight , this ) ;
						// the number of animals of this type.
						g.drawString ( specData.get ( t ).getValue() + "" , boundingBox.x , currentHeight + imageHeight - 20 ) ;
						// update the coordinates manager to manage the click events.
						coordinatesManager.updateAnimalsInRegionsMap ( i , t , new Rectangle ( boundingBox.x , currentHeight , imageWidth , imageHeight ) );
						// increment the current height for the next image
						currentHeight = currentHeight + 2 * imageHeight ;
					}
				}
			}
		}
		
		/***/
		private void drawRoadsElements ( Graphics g ) 
		{
			Map < Integer , Map < PositionableElementType , Counter > > dataMap ;
			Map < PositionableElementType , Counter > specData ;
			PositionableElementType type ;
			BufferedImage toDraw ;
			Shape shape ;
			Point tlc ;
			int i ;
			int x ;
			int x0 ;
			int y0 ;
			boolean transparent ;
			// retrieve data about roads
			dataMap = positionableElementsManager.getData ( GameMapElementType.ROAD ) ;
			// for each roads
			tlc = generateTopLeftCorner () ;
			for ( i = 1 ; i <= GameConstants.NUMBER_OF_ROADS ; i ++ )
			{
				// retrieve the geo info about this road
				shape = coordinatesManager.getRoadBorder ( i ) ;
				// data about thie content of this road
				specData = dataMap.get ( i ) ;
				// it there is someone in
				if ( specData != null ) 
				{
					// retrieve the type of object with which we are dealing with.
					type = specData.keySet().iterator().next () ;
					// determine if transparency is needed.
					transparent = highlightMode && ( type == PositionableElementType.FENCE || ( type == PositionableElementType.SHEPERD && currentInputMode == GameViewInputMode.SHEPERDS ) ) ;
					// select the appropriate image.
					toDraw = SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( type , transparent ) ;
					// determine the right coordinates.
					x = tlc.x == 0 ? 0 : ( getWidth () - preferredDimension.width ) / 2 ;
					x0 = ( int ) ( ( ( RectangularShape ) shape).getMinX() + x ) ;
					y0 = ( int ) ( ( RectangularShape ) shape ).getMinY () ;
					// draw the image effectively
					g.drawImage ( toDraw , x0, y0 , coordinatesManager.ROADS_RADIUS * 2 , coordinatesManager.ROADS_RADIUS * 2 , this ) ;
					// update the coordinates manager to manage the click events.
					coordinatesManager.updateObjectInRoadsMap ( i , type );
				}
			}
		}
		
		/***/
		private void drawRegionsHighlighted ( Graphics g ) 
		{
			
		}
		
		/***/
		public void drawRoadHighlighted ( Graphics g ) 
		{
			
		}
		
		/**
		 * Zooms this GameView, the GameMap it represents and also every component drawed on it. 
		 * 
		 * @param xFactor the factor of the zoom in the x direction.
		 * @param yFactor the factor of the zoom in the y direction.
		 */
		public void zoom ( float xFactor , float yFactor ) 
		{
			Image original ;
			int newW ;
			int newH ;
			original = SheeplandClientApp.getInstance().getImagesHolder().getMapImage(false) ; 
			newW = new Double ( original.getWidth  ( this ) * xFactor ).intValue () ;
			newH = new Double ( original.getHeight ( this ) * yFactor ).intValue () ;
			preferredDimension.width = newW ;
			preferredDimension.height = newH ;
		    SheeplandClientApp.getInstance().getImagesHolder().zoomMapViewImages ( xFactor , yFactor ) ;
			SwingUtilities.updateComponentTreeUI ( MapViewPanel.this ) ;
		    coordinatesManager.scale ( xFactor , yFactor ) ; 
		}
		
		/**
		 * This class managed the click actions a User do on the Map.
		 * The mouseClocked event is the base point from where launch other events relative
		 * to User choiches. 
		 */
		private class ClickManager extends MouseAdapter 
		{
			
			/**
			 * AS THE SUPER'S ONE. 
			 */
			@Override
		    public void mouseClicked(MouseEvent e) 
		    {
				String methodName ;
				Integer uid ;
				int x ;
				int y ;
				methodName = null ;
				if ( currentInputMode != null )
				{
					x = e.getX () ;
					y = e.getY () ;
					switch ( currentInputMode )
					{
						case REGIONS :
							uid = coordinatesManager.getRegionId ( x , y ) ;
							if ( uid != null )
								methodName = "onRegionSelected" ;
						break ;
						case ROADS :
							uid = coordinatesManager.getRoadId ( x , y ) ;
							if ( uid != null )
								methodName = "onRoadSelected" ;
						break ;
						case ANIMALS :
							uid = coordinatesManager.getAnimalId ( x , y ) ;
							if ( uid != null )
								methodName = "onSheperdSelected" ;
						break ;
						case SHEPERDS :
							uid = coordinatesManager.getSheperdId ( x , y ) ;
							if ( uid != null )
								methodName = "onAnimalSelected" ;
						break ;
						default :
							throw new RuntimeException () ;
					}
					try 
					{
						if ( uid != null )
						{
							System.out.println ( uid ) ;
							notifyObservers ( methodName , uid ) ;
						}
						else
						{
							JOptionPane.showMessageDialog ( DrawingPanel.this , PresentationMessages.INVALID_CHOOSE_MESSAGE , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE );
						}
					}
					catch (MethodInvocationException e1) 
					{
						e1.printStackTrace();
					}
				}
		    }
			
		}
		
	}

	/**
	 * A class that manages all the commands associated with a MapView.
	 */
	private class CommandPanel extends FrameworkedWithGridBagLayoutPanel 
	{
		
		/**
		 * Button whose triggers indicate that the user, being an a MoveChoose context, wants to change
		 * a Move. 
		 */
		private JButton changeMoveButton ;
		
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
			changeMoveButton = new JButton () ;
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
			layoutComponent ( changeMoveButton , 0 , 0 , 1 , 1, 1 , 2, 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets);
			layoutComponent ( zoomLessButton , 0 , 1 , 0.5 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( zoomPlusButton , 1 , 1 , 0.5 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;			
		}

		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		protected void bindListeners() 
		{
			Action changeMoveButtonAction ;
			Action zoomLessAction ;
			Action zoomPlusAction ;
			changeMoveButtonAction = new ChangeMoveButtonAction ( "CAMBIA MOSSA" ) ;
			zoomLessAction = new ZoomLessAction () ;
			zoomPlusAction = new ZoomPlusAction () ;
			changeMoveButton.setAction ( changeMoveButtonAction );
			zoomLessButton.setAction ( zoomLessAction ) ;
			zoomPlusButton.setAction ( zoomPlusAction ) ;	
		}

		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		protected void injectComponents () 
		{
			add ( changeMoveButton ) ;
			add ( zoomLessButton ) ;
			add ( zoomPlusButton ) ;	
		}
		
		/**
		 * Action that manages the ChangeMove action. 
		 */
		private class ChangeMoveButtonAction extends AbstractAction 
		{
			
			/**
			 * @param txt the text to display. 
			 */
			public ChangeMoveButtonAction ( String txt ) 
			{
				super ( txt ) ;
			}
			
			/**
			 * AS THE SUPER'S ONE. 
			 */
			@Override
			public void actionPerformed ( ActionEvent e ) 
			{
				try 
				{
					notifyObservers ( "onWantToChangeMove" ) ;
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			} 
			
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
	
}
