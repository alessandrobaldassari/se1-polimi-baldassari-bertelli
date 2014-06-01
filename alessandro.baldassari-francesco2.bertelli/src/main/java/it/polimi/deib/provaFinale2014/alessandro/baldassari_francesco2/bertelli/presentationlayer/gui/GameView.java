package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableFrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WithGridBagLayoutPanel;

/***/
public class GameView extends JFrame
{

	/***/
	private GameViewPanel gameViewPanel ;

	/***/
	GameView () 
	{
		super () ;
		gameViewPanel = new GameViewPanel () ; 
	}
	
}

class GameViewPanel extends WithGridBagLayoutPanel 
{

	private MapViewPanel mapViewPanel ;
	
	GameViewPanel () 
	{
		super () ;
	}
	
}

class MapViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel  
{

	private JSplitPane splitPane ;
	private JScrollPane scrollPane ;
	
	MapViewPanel () 
	{
		super () ;	
	}
	
	/*private class DrawingPanel extends JPanel 
	{
		
		private MediaTracker mediaTracker ;
		private Image backgroundImage ;
		private Dimension backgroundImageDimension ;
		private Collection < PositionableComponentView > components ;
		
		public DrawingPanel ( Image backgroundImage , int backgroundImageWidth , int backgroundImageHeight ) 
		{
			this.backgroundImage = backgroundImage ;
			components = new LinkedList < PositionableComponentView > () ;
			backgroundImageDimension = new Dimension ( backgroundImageWidth , backgroundImageHeight ) ;
			setOpaque ( false ) ;
			setLayout ( null ) ;
			addPositionableComponentView ( new PositionableComponentView ( backgroundImage , new Point ( 0 , 0 ) , new Dimension ( 50 , 50 ) ) ) ;
		}
		
		@Override
		public Dimension getSize () 
		{
			return backgroundImageDimension ;
		}
		
		@Override
		public Dimension getPreferredSize () 
		{
			return backgroundImageDimension ;			
		}
		
		@Override
		public void paintComponent ( Graphics g ) 
		{ 	
			g.drawImage ( backgroundImage , 0 , 0 , backgroundImageDimension.width , backgroundImageDimension.height , this );
			super.paintComponent ( g ) ;
		}
		
		public void setBackgroundImage ( Image backgroundImage ) 
		{
			this.backgroundImage = backgroundImage ;
		}
		
		public void zoom ( float factor  ) 
		{
			backgroundImageDimension.setSize ( backgroundImageDimension.width * factor , backgroundImageDimension.height * factor ) ;
			SwingUtilities.updateComponentTreeUI ( MapViewPanel.this ) ;
		}
		
	}
*/
	@Override
	protected void createComponents () 
	{
		splitPane = new JSplitPane () ;
		scrollPane = new JScrollPane () ;
		//drawingPanel = new DrawingPanel ( backgroundImage , backgroundImageWidth , backgroundImageHeight ) ;
		//commandPanel = new CommandPanel () ;		
	}

	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		splitPane.setTopComponent ( scrollPane ) ;
		//splitPane.setBottomComponent ( commandPanel );
		splitPane.setOrientation ( JSplitPane.VERTICAL_SPLIT );
		splitPane.setOneTouchExpandable ( true ) ;
		//scrollPane.setViewportView ( drawingPanel ) ;
		//scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED ) ;
		//scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;
		//drawingPanel.setDoubleBuffered ( true ) ;
		//setLayout ( layout ) ;
		//GraphicsUtilities.setComponentLayoutProperties ( splitPane , layout, 0 , 0 , 1 , 50 , 2 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		
	}

	@Override
	protected void bindListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void injectComponents() 
	{
		
	}
	
	private class CommandPanel extends FrameworkedWithGridBagLayoutPanel 
	{
		
		private JButton zoomLessButton ;
		private JButton zoomPlusButton ;
		
		public CommandPanel () 
		{
			Insets insets ;
			insets = new Insets ( 0 , 0 , 0 , 0 ) ;
			zoomLessButton.setAction ( new ZoomLessAction () ) ;
			zoomPlusButton.setAction ( new ZoomPlusAction () ) ;
			layoutComponent ( zoomLessButton , 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( zoomPlusButton , 1 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			add ( zoomLessButton ) ;
			add ( zoomPlusButton ) ;
		}
		
		/***/
		private class ZoomLessAction extends AbstractAction 
		{

			public ZoomLessAction () 
			{
				super ( "ZOOM -" ) ;
			}
			
			public void actionPerformed ( ActionEvent e ) 
			{
				//drawingPanel.zoom ( 0.5f ) ;
			}
			
		}
		
		private class ZoomPlusAction extends AbstractAction 
		{
			
			public ZoomPlusAction () 
			{
				super ( "ZOOM +" ) ;
			}

			public void actionPerformed ( ActionEvent e ) 
			{
				//drawingPanel.zoom ( 2f ) ;	
			}
					
		}

		@Override
		protected void createComponents () 
		{
			zoomLessButton = new JButton () ;
			zoomPlusButton = new JButton () ;
			
		}

		@Override
		protected void manageLayout() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void bindListeners() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void injectComponents() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
