package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.WithBackgroundImagePanel;

public class MoveChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < MoveChooseViewObserver >
{

	/***/
	private InputView view ;
	
	/***/
	private MoveListPanel moveListPanel ;
	
	/**
	 * @param observer an Observer for this MoveChooseView to notity of the events.
	 */
	public MoveChooseView ( DefaultMoveChooseViewObserver observer ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		addObserver ( observer ) ;
	}
	
	public void setAvailableMoves ( Iterable < GameMoveType > availableMoves ) 
	{
		moveListPanel.setAvailableMoves ( availableMoves ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		view = new InputView () ;
		moveListPanel = new MoveListPanel () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		GraphicsUtilities.setComponentLayoutProperties ( view , getLayout () , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( DO_NOTHING_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;	
		view.setShowKo(false); 
		setUndecorated(false);
		setDefaultCloseOperation ( DO_NOTHING_ON_CLOSE ) ;
 		setSize ( GraphicsUtilities.getVGAResolution() ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
		view.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCoverImage(true) ); 
		view.setTitle ( PresentationMessages.DO_MOVE_MESSAGE ) ;
		setResizable ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		view.setOkAction ( new Couple < Boolean , Runnable > ( false , new OkAction () ) ) ; 
		view.setKoAction ( new Couple < Boolean , Runnable > ( false , new KoAction () ) ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		view.setContentsPanel ( moveListPanel ) ;
		add ( view ) ;
	}
	
	/**
	 * This class manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			GameMoveType res ;
			res = moveListPanel.getSelectedMove () ;
			if ( res != null )
				try 
				{
					notifyObservers ( "onMoveChoosed" , moveListPanel.getSelectedMove () );
				}
				catch (MethodInvocationException e) 
				{
					e.printStackTrace();
				}
			else
				view.setErrors ( PresentationMessages.INVALID_CHOOSE_MESSAGE ) ;
		}
		
	}
	
	/**
	 * This class manages the ko action. 
	 */
	private class KoAction implements Runnable 
	{
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			try 
			{
				notifyObservers ( "onDoNotWantChooseMove" );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/***/
	public static GameMoveType showDialog ( Iterable < GameMoveType > availableMoves ) 
	{
		MoveChooseView moveChooseView ;
		AtomicReference < GameMoveType > move ;
		move = new AtomicReference < GameMoveType > ( null ) ;
		moveChooseView = new MoveChooseView ( new DefaultMoveChooseViewObserver(move) ) ;
		moveChooseView.setAvailableMoves(availableMoves); 
		GraphicsUtilities.showUnshowWindow ( moveChooseView , false , true ) ;
		synchronized ( move ) 
		{
			while ( move.get() == null )
				try 
				{
					move.wait () ;
				}
				catch (InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		GraphicsUtilities.showUnshowWindow ( moveChooseView , false , false ) ;
		return move.get() ;
	}
	
}

/***/
class MoveListPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	/***/
	private GameMoveType selected ;
	
	/***/
	private Map < GameMoveType , WithBackgroundImagePanel > imagePanels ;

	/***/
	private Map < GameMoveType , JRadioButton > selectors ;
		
	/***/
	private ButtonGroup buttonGroup ;	
	
	/***/
	public MoveListPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		selected = null ;
		imagePanels = new HashMap < GameMoveType , WithBackgroundImagePanel > ( GameMoveType.values().length ) ;
		for ( GameMoveType g : GameMoveType.values () )
			imagePanels.put ( g ,new WithBackgroundImagePanel () ) ;
		selectors = new HashMap < GameMoveType , JRadioButton > ( imagePanels.size() ) ;
		for ( GameMoveType g : GameMoveType.values () )
			selectors.put ( g , new JRadioButton () ) ;
		buttonGroup = new ButtonGroup () ;
	}

	/***/
	@Override
	protected void manageLayout () 
	{
		GameMoveType [] moveTypes ;
		WithBackgroundImagePanel panel ;
		JRadioButton r ;
		Insets insets ;
		moveTypes = GameMoveType.values();
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		int i ;
		setOpaque ( false ) ;
		i = 0 ;
		for ( GameMoveType g : moveTypes )
		{
			panel = imagePanels.get(g);
			r = selectors.get(g);
			layoutComponent ( panel , i , 1 , 1 , 1 , 1 , 1 , 5 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( r , i , 2 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			r.setText ( moveTypes [ i ].name () ) ;
			r.setOpaque ( false ) ;
			r.setHorizontalAlignment ( SwingConstants.CENTER ) ;
			panel.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getMoveImage ( moveTypes [ i ] , false ) );
			panel.setOpaque ( false ) ;
			i ++ ;
		}
	}

	/***/
	@Override
	protected void bindListeners () 
	{
		GameMoveType [] moveTypes ;
		int i ;
		moveTypes = GameMoveType.values () ;
		for ( GameMoveType g : moveTypes )
			selectors .get ( g ).addItemListener ( new SelectorListener ( g ) ) ;
	}

	@Override
	protected void injectComponents () 
	{
		for ( JPanel p : imagePanels.values() )
			add ( p ) ;
		for ( JRadioButton r : selectors.values() )
			add ( r ) ;
		for ( JRadioButton r : selectors.values() )
			buttonGroup.add ( r ) ;
	}

	/***/
	public void setAvailableMoves ( Iterable < GameMoveType > availableMoves ) 
	{
		for ( GameMoveType g : GameMoveType.values () )
		{
			imagePanels.get(g).setEnabled(false);
			selectors.get(g).setEnabled(false); 
		}
		for ( GameMoveType g : availableMoves )
		{
			imagePanels.get ( g ).setEnabled ( true ) ;
			selectors.get ( g ).setEnabled ( true ) ; 
		}
	}
	
	/**
	 * Returns the move selected in this View. 
	 */
	public GameMoveType getSelectedMove () 
	{
		return selected ;
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		/***/
		private GameMoveType gameMove ;
		
		/***/
		public SelectorListener ( GameMoveType gameMove )
		{
			this.gameMove = gameMove ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			selected = gameMove ;
		}	
		
	}
	
}
