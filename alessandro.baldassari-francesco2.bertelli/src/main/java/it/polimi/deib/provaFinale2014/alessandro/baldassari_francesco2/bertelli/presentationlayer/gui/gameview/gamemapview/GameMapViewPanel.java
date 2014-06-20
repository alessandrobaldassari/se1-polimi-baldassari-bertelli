package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.gui.message.GUIGameMapNotificationMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
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
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;

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
	private Iterable < ? extends Identifiable > uidOfSelectableElements ;
	
	/***/
	private Animator animator ;
	
	/***/
	private Point tlc ;
	
	/***/
	private float xZoomMult ;
	
	/***/
	private float yZoomMult ;
	
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
		animator = new Animator ( drawingPanel , coordinatesManager , positionableElementsManager ) ;
		tlc = new Point ( 0 , 0 ) ;
		xZoomMult = 1 ;
		yZoomMult = 1 ;
		Executors.newSingleThreadExecutor().execute ( animator ) ;
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
		layoutComponent ( scrollPane, 0 , 0 , 1 , 20 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;	
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
		animator.addAddMessage ( new GUIGameMapNotificationMessage ( "ADD" , whereType , whereId , whoType , whoId ) );
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	public void onPositionableElementRemoved(GameMapElementType whereType , Integer whereId , PositionableElementType whoType , Integer whoId) 
	{
		animator.addRemoveMessage ( new GUIGameMapNotificationMessage ( "REMOVE" , whereType , whereId , whoType , whoId ) );
	}
	
	/**
	 * Set the current input mode for this MapViewPanel 
	 * 
	 * @param currentInputMode the value for the currentInputModeProperty.
	 */
	public void setCurrentInputMode ( GameMapViewInputMode currentInputMode , Iterable < ? extends Identifiable > uidOfSelectableElements ) 
	{
		this.currentInputMode = currentInputMode ;
		if ( currentInputMode == null )
			this.uidOfSelectableElements = null ;
		else
			this.uidOfSelectableElements = uidOfSelectableElements ;
		repaint () ;
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
		
		private Dimension fixedSize ;
		
		/***/
		private Dimension preferredSize ;
		
		// METHODS
		
		/***/
		public DrawingPanel () 
		{
			super () ;
			Image bI ;
			setDoubleBuffered ( true ) ;
			setLayout ( null ) ;
			setOpaque ( false );
			addMouseListener ( new ClickManager () );
			bI = SheeplandClientApp.getInstance().getImagesHolder().getMapImage(false);
			fixedSize = new Dimension ( bI.getWidth(this),bI.getHeight(this) ) ; 
			preferredSize = new Dimension ( bI.getWidth(this),bI.getHeight(this) ) ; 
		}
		
		/**
		 * AS THE SUPER'S ONE.
		 */
		@Override
		public void paintComponent ( Graphics g ) 
		{ 	
			super.paintComponent ( g ) ;
			// general settings
			g.setFont ( new Font ( "Arial" , Font.ITALIC , 15 ) ) ;
			g.setColor ( Color.RED ) ;
			tlc = generateTopLeftCorner () ;
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
			// animations
			drawAnimation ( g ) ;
		}
		
		/***/
		private void drawAnimation ( Graphics g ) 
		{
			Rectangle r ;
			if ( animator.isInAnimation () )
			{
				r = scaleCoordinates ( ( int ) animator.getWhereDraw().getX() , ( int ) animator.getWhereDraw().getY() , ( int ) animator.getWhereDraw().getWidth() , ( int ) animator.getWhereDraw().getHeight() ) ;
				g.drawImage ( animator.getToDraw() , r.x , r.y , r.width , r.height , this ) ;
			}
		}
		
		/***/
		private void drawBackground ( Graphics g ) 
		{
			Image backgroundImage ;
			Rectangle r ;
			backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getMapImage ( currentInputMode == null ) ;
			// draw the map
			r = scaleCoordinates ( 0 , 0 , fixedSize.width , fixedSize.height ) ;
			g.drawImage ( backgroundImage , r.x , r.y , r.width , r.height , this );
			System.out.println ( "X = " + tlc.x + "\nY = " + tlc.y ) ;
			System.out.println ( "W = " + r.width + "\nH = " + r.height );
			preferredSize.width = r.width ;
			preferredSize.height = r.height ;
			setPreferredSize(preferredSize);
			revalidate () ;
		}

		/***/
		private void drawRegionsElement ( Graphics g ) 
		{
			Map < PositionableElementType , Collection < Integer > > animalsInRegion ;
			Map < PositionableElementType , Ellipse2D > positions ;
			BufferedImage toDraw ;
			Rectangle r ;
			Ellipse2D pos ;
			int count ;
			int i ;
			boolean transparent ;
			transparent = ! ( currentInputMode == null || currentInputMode == GameMapViewInputMode.ANIMALS )  ;
			// for each region
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
					r = scaleCoordinates ( ( int ) pos.getMinX() , ( int ) pos.getMinY() , ( int ) pos.getWidth() , ( int ) pos.getHeight() );
					g.drawImage ( toDraw , r.x , r.y , r.width , r.height , this ) ;
				}
				// if the wolf is here
				if ( animalsInRegion.get ( PositionableElementType.WOLF ).size() > 0 )
				{
					toDraw =  SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( PositionableElementType.WOLF , false ) ;
					pos = positions.get ( PositionableElementType.WOLF ) ;
					r = scaleCoordinates ( ( int ) pos.getMinX() , ( int ) pos.getMinY() , ( int ) pos.getWidth() , ( int ) pos.getHeight() );
					g.drawImage ( toDraw , r.x , r.y , r.width , r.height , this ) ;
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
					r = scaleCoordinates ( ( int ) pos.getMinX() , ( int ) pos.getMinY() , ( int ) pos.getWidth() , ( int ) pos.getHeight() );
					g.drawImage ( toDraw , r.x , r.y , r.width , r.height , this ) ;
					g.drawString ( String.valueOf ( count ) , r.x , r.y ) ;
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
			Rectangle r ;
			int i ;
			boolean transparent ;
			// for each roads
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
					transparent = ! ( currentInputMode == null || type == PositionableElementType.SHEPERD && currentInputMode == GameMapViewInputMode.SHEPERDS ) ;
					// select the appropriate image.
					toDraw = SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( type , transparent ) ;
					// determine the right coordinates.
					r = scaleCoordinates ( ( int ) ( ( RectangularShape ) shape).getMinX() , ( int ) ( ( RectangularShape ) shape).getMinY() , 2 *  coordinatesManager.ROADS_RADIUS , 2 * coordinatesManager.ROADS_RADIUS ) ;
					// draw the image effectively
					g.drawImage ( toDraw , r.x , r.y , r.width , r.height , this ) ;
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
		
		/***/
		private Point generateTopLeftCorner () 
		{
			Point res ;
			res = new Point ( 0 , 0 ) ;
			// consider the Map may not fill the entire space offered by this panel.
			System.out.println ( fixedSize.width ) ;
			if ( fixedSize.width * xZoomMult < getWidth () ) 
				res.x = (int) (( getWidth () - fixedSize.width * xZoomMult ) / 2) ;
			if ( fixedSize.height * yZoomMult < getHeight () )
				res.y = (int) (( getHeight () - fixedSize.height * yZoomMult ) / 2) ;
			return res ;
		}
		
		/***/
		private Rectangle scaleCoordinates ( int x , int y , int w , int h )
		{
			Rectangle res ;
			int dx ;
			int dy ;
			res = new Rectangle () ;
			dx = tlc.x ;
			dy = tlc.y ;
			res.x = (int) (( x + dx ) * xZoomMult) ;
			res.y = (int) (( y + dy ) * yZoomMult) ;
			res.width = (int) (w * xZoomMult) ;
			res.height = (int) (h * yZoomMult) ;
			return res ;
		}
		
		/***/
		private Rectangle unscaleCoordinates ( int x , int y , int w , int h ) 
		{
			Rectangle res ;
			int dx ;
			int dy ;
			res = new Rectangle() ;
			dx = tlc.x ;
			dy = tlc.y ;
			res.x = (int) (( x / xZoomMult ) - dx) ;
			res.y = (int) (( y / yZoomMult ) - dy) ;
			res.width = (int) (w / xZoomMult) ;
			res.height = (int) (h / yZoomMult) ;
			return res ;
		}
		
		/**
		 * Zooms this GameView, the GameMap it represents and also every component drawed on it. 
		 * 
		 * @param xFactor the factor of the zoom in the x direction.
		 * @param yFactor the factor of the zoom in the y direction.
		 */
		public void zoom ( float xFactor , float yFactor ) 
		{
			System.out.println ( "DRAWING_PANEL - ZOOM : BEGINS" ) ;
			xZoomMult =  xZoomMult * xFactor;
			yZoomMult = yZoomMult * yFactor ;
			repaint () ;
			System.out.println ( "DRAWING_PANEL - ZOOM : END" ) ;
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
				Rectangle r ;
				methodName = null ;
				if ( currentInputMode != null )
				{
					r = unscaleCoordinates ( e.getX () , e.getY () , 0 , 0 ) ;
					System.out.println ( "CLICK_MANAGER : BEFORE SWITCHING" ) ;
					switch ( currentInputMode )
					{
						case REGIONS :
							System.out.println ( "CLICK_MANAGER : REGION." ) ;
							uid = coordinatesManager.getRegionId ( r.x , r.y ) ;
							System.out.println ( "CLICK_MANAGER : REGION_UID : " + uid ) ;
							if ( uid != null )
								methodName = "onRegionSelected" ;
						break ;
						case ROADS :
							System.out.println ( "CLICK_MANAGER : ROAD." ) ;
							uid = coordinatesManager.getRoadId ( r.x , r.y ) ;
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
									for ( Identifiable j : uidOfSelectableElements )
										if ( i == j.getUID() )
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
							uid = coordinatesManager.getSheperdId ( r.x , r.y ) ;
							System.out.println ( "CLICK_MANAGER : SHEPERD, UID = " +uid ) ;
							if ( uid != null )
								methodName = "onSheperdSelected" ;
						break ;
						default :
							throw new RuntimeException () ;
					}
					try 
					{
						System.out.println ( "MAP VIEW PANEL - AFTER SWITCH : UID = " + uid ) ;
						if ( uid != null && containsId(uidOfSelectableElements , uid ) )
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

	private boolean containsId ( Iterable < ? extends Identifiable > src , int key ) 
	{
		boolean res ;
		res = false ;
		for ( Identifiable i : src )
			if ( i.getUID() == key )
			{
				res = true ;
				break ;
			}
		return res ;
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
			}
					
		}
		
	}
	
}
