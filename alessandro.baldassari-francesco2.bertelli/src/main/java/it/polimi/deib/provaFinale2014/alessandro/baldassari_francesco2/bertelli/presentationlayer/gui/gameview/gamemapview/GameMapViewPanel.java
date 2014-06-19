package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.ObservableFrameworkedWithGridBagLayoutPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * A component that manage the rendering of the Map and the interaction of the User with it. 
 */
public class GameMapViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel < GameMapViewObserver > implements GameMapObserver 
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
	 * The object that manages the position of elements on the GameMap. 
	 */
	private PositionableElementCoordinatesManager positionableElementsManager ;
	
	/**
	 * A component to manage the coordinates of the Map and the Objects on the Map. 
	 */
	private MapMeasuresCoordinatesManager coordinatesManager ;
		
	/**
	 * The input mode this GameView is, null if none. 
	 */
	private GameMapViewInputMode currentInputMode ;
	
	/**
	 * The id's of the selectable elements 
	 */
	private Iterable < Integer > uidOfSelectableElements ;
	
	private int xZoomMult ;
	
	private int yZoomMult ;
	
	// METHODS
	
	/***/
	public GameMapViewPanel () 
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
		uidOfSelectableElements = null ;
		xZoomMult = 1 ;
		yZoomMult = 1 ;
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
		layoutComponent ( scrollPane, 0 , 0 , 1 , 25 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;	
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
	public void onPositionableElementAdded ( GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId ) 
	{
		positionableElementsManager.addElement(whereType, whereId, whoType, whoId);
		repaint () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	public void onPositionableElementRemoved(GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId) 
	{
		positionableElementsManager.removeElement ( whereType , whereId , whoType , whoId ) ;
		repaint () ;
	}

	/**
	 * Set the current input mode for this MapViewPanel 
	 * 
	 * @param currentInputMode the value for the currentInputModeProperty.
	 */
	public void setCurrentInputMode ( GameMapViewInputMode currentInputMode , Iterable < Integer > uidOfSelectableElements ) 
	{
		this.currentInputMode = currentInputMode ;
		if ( currentInputMode == null )
			this.uidOfSelectableElements = null ;
		else
			this.uidOfSelectableElements = uidOfSelectableElements ;
	}
	
	/***/
	public GameMapViewInputMode getCurrentInputMode () 
	{
		return currentInputMode ;
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
			//backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( highlightMode ) ;
			backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( false ) ;
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
			backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( currentInputMode != null ) ;	
			tlc = generateTopLeftCorner() ;
			// draw the map
			g.drawImage ( backgroundImage , tlc.x * xZoomMult , tlc.y * yZoomMult  , backgroundImage.getWidth ( this ) * xZoomMult , backgroundImage.getHeight ( this ) * yZoomMult , this );
		}

		/***/
		private void drawRegionsElement ( Graphics g ) 
		{
			Map < PositionableElementType , Collection < Integer > > animalsInRegion ;
			Map < PositionableElementType , Ellipse2D > positions ;
			BufferedImage toDraw ;
			Ellipse2D pos ;
			int count ;
			int i ;
			boolean transparent ;
			// for each region
			if ( currentInputMode == null )
				transparent = false ;
			else
				transparent = true ;
			for ( i = 1 ; i <= GameConstants.NUMBER_OF_REGIONS ; i ++ )
			{
				// retrieve the data
				animalsInRegion = positionableElementsManager.getAnimalsInRegion ( i ) ;
				// retrieve the position
				positions = coordinatesManager.getRegionData ( i ) ;
				// if the black sheep is here
				if ( animalsInRegion.get ( PositionableElementType.BLACK_SHEEP ).size() > 0 )
				{
					toDraw =  SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( PositionableElementType.BLACK_SHEEP , transparent ) ;
					pos = positions.get ( PositionableElementType.BLACK_SHEEP ) ;
					g.drawImage ( toDraw , ( int ) pos.getMinX() * xZoomMult , ( int ) pos.getMinY() * yZoomMult , ( int ) pos.getWidth() * xZoomMult , ( int ) pos.getHeight() * yZoomMult , this ) ;
				}
				// if the wolf is here
				if ( animalsInRegion.get ( PositionableElementType.WOLF ).size() > 0 )
				{
					toDraw =  SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( PositionableElementType.WOLF , false ) ;
					pos = positions.get ( PositionableElementType.WOLF ) ;
					g.drawImage ( toDraw , ( int ) pos.getMinX() * xZoomMult , ( int ) pos.getMinY() * yZoomMult , ( int ) pos.getWidth() * xZoomMult , ( int ) pos.getHeight() * yZoomMult , this ) ;
				}
				//
				count = 0 ;
				for ( PositionableElementType p : animalsInRegion.keySet () )
					if ( PositionableElementType.isStandardAdultOvine ( p ) )
						count = count + animalsInRegion.get ( p ).size () ;
				// if there is someone
				if ( count > 0 )
				{
					toDraw =  SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( PositionableElementType.SHEEP , false ) ;
					pos = positions.get ( PositionableElementType.STANDARD_ADULT_OVINE ) ;
					g.drawImage ( toDraw , ( int ) pos.getMinX() * xZoomMult , ( int ) pos.getMinY() * yZoomMult , ( int ) pos.getWidth() * xZoomMult , ( int ) pos.getHeight() * yZoomMult , this ) ;
					g.drawString ( count + "" , ( int ) pos.getMinX() * xZoomMult , ( int ) pos.getMaxY() * yZoomMult ) ;
				}
			}
		}
		
		/***/
		private void drawRoadsElements ( Graphics g ) 
		{
			Couple < PositionableElementType , Integer > elementContained ;
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
			// for each roads
			tlc = generateTopLeftCorner () ;
			for ( i = 1 ; i <= GameConstants.NUMBER_OF_ROADS ; i ++ )
			{
				// retrieve the geo info about this road
				shape = coordinatesManager.getRoadBorder ( i ) ;
				// data about thie content of this road
				elementContained = positionableElementsManager.getElementInRoad ( i ) ;
				// it there is someone in
				if ( elementContained != null ) 
				{
					// retrieve the type of object with which we are dealing with.
					type = elementContained.getFirstObject () ;
					// determine if transparency is needed.
					transparent = ( currentInputMode != null ) && ( type == PositionableElementType.FENCE || ( type == PositionableElementType.SHEPERD && currentInputMode == GameMapViewInputMode.SHEPERDS ) ) ;
					// select the appropriate image.
					toDraw = SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( type , transparent ) ;
					// determine the right coordinates.
					x = tlc.x == 0 ? 0 : ( getWidth () - preferredDimension.width ) / 2 ;
					x0 = ( int ) ( ( ( RectangularShape ) shape).getMinX() + x ) ;
					y0 = ( int ) ( ( RectangularShape ) shape ).getMinY () ;
					// draw the image effectively
					g.drawImage ( toDraw , x0 * xZoomMult, y0 * yZoomMult , coordinatesManager.ROADS_RADIUS * 2 * xZoomMult , coordinatesManager.ROADS_RADIUS * 2 * yZoomMult , this ) ;
					// update the coordinates manager to manage the click events.
				}
			}
		}
		
		/***/
		private void drawRegionsHighlighted ( Graphics g ) 
		{
			// not implemented yet.
		}
		
		/***/
		public void drawRoadHighlighted ( Graphics g ) 
		{
			// not implemented yet.
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
			original = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( false ) ; 
			newW = new Double ( original.getWidth  ( this ) * xFactor ).intValue () ;
			newH = new Double ( original.getHeight ( this ) * yFactor ).intValue () ;
			preferredDimension.width = newW ;
			preferredDimension.height = newH ;
		    coordinatesManager.scale ( xFactor , yFactor ) ; 
		    repaint () ;
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
				Collection < Integer > uids ;
				String methodName ;
				Integer uid ;
				Point tlc ;
				int x ;
				int y ;
				methodName = null ;
				if ( currentInputMode != null )
				{
					tlc = generateTopLeftCorner () ;
					x = e.getX () ;
					y = e.getY () ;
					if ( tlc.x != 0 )
						x = x - ( getWidth() - preferredDimension.width ) / 2 ;
					if ( tlc.y != 0 )
						y = y - ( getHeight() - preferredDimension.height ) / 2 ;
					System.out.println ( "CLICK_MANAGER : BEFORE SWITCHING" ) ;
					switch ( currentInputMode )
					{
						case REGIONS :
							System.out.println ( "CLICK_MANAGER : REGION." ) ;
							uid = coordinatesManager.getRegionId ( x , y ) ;
							System.out.println ( "CLICK_MANAGER : REGION_UID : " + uid ) ;
							if ( uid != null )
								methodName = "onRegionSelected" ;
						break ;
						case ROADS :
							System.out.println ( "CLICK_MANAGER : ROAD." ) ;
							uid = coordinatesManager.getRoadId ( x , y ) ;
							System.out.println ( "CLICK_MANAGER : ROAD_UID : " + uid ) ;
							if ( uid != null )
								methodName = "onRoadSelected" ;
						break ;
						case ANIMALS :
							System.out.println ( "CLICK_MANAGER : ANIMAL." ) ;
							uids = null;
							//uids = coordinatesManager.getAnimalsId ( x , y ) ;
							if ( ! uids.isEmpty() )
							{
								uid = null ;
								for ( Integer i : uids )
								{
									for ( Integer j : uidOfSelectableElements )
										if ( i == j )
										{
											uid = i ;
											break ;
										}
									if ( uid != null )
										break ;
								}
								if ( uid != null )
									methodName = "onAnimalSelected" ;
							}
							else
								uid = null ;
								
						break ;
						case SHEPERDS :
							System.out.println ( "CLICK_MANAGER : SHEPERD." ) ;
							uid = coordinatesManager.getSheperdId ( x , y ) ;
							if ( uid != null )
								methodName = "onSheperdSelected" ;
						break ;
						default :
							throw new RuntimeException () ;
					}
					try 
					{
						System.out.println ( "MAP VIEW PANEL - AFTER SWITCH : UID = " + uid ) ;
						if ( uid != null && CollectionsUtilities.contains ( uidOfSelectableElements , uid ) )
						{
							System.out.println ( "MAP VIEW PANEL - BEFORE NOTIFICATION" ) ;
							notifyObservers ( methodName , uid ) ;
							System.out.println ( "MAP VIEW PANEL - AFTER NOTIFICATION" ) ;
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
			public void actionPerformed ( ActionEvent e ) 
			{
				drawingPanel.zoom ( 0.5f , 0.5f ) ;
				xZoomMult *= 0.5f ;
				yZoomMult *= 0.5f ;
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
			public void actionPerformed ( ActionEvent e ) 
			{
				drawingPanel.zoom ( 2f , 2f ) ;	
				xZoomMult *= 2f ;
				yZoomMult *= 2f ;
			}
					
		}
		
	}
	
}
