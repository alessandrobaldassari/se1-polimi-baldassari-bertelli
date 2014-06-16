package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.WithBackgroundImagePanel;

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
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;	
		view.setShowKo(false); 
		setUndecorated(true);
 		setSize ( GraphicsUtilities.getVGAResolution () ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
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
	public static GameMoveType showDialog () 
	{
		MoveChooseView moveChooseView ;
		AtomicReference < GameMoveType > move ;
		move = new AtomicReference < GameMoveType > ( null ) ;
		moveChooseView = new MoveChooseView ( new DefaultMoveChooseViewObserver(move) ) ;
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
	
	public static void main ( String [] args ) 
	{
		GameMoveType x ;
		x = MoveChooseView.showDialog () ;
		System.out.println ( x ) ;
	}
	
}

/***/
class MoveListPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	/***/
	private GameMoveType selected ;
	
	/***/
	private WithBackgroundImagePanel [] imagePanels ;

	/***/
	private JRadioButton [] selectors ;
		
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
		int i ;
		selected = null ;
		imagePanels = new WithBackgroundImagePanel [ GameMoveType.values().length ] ;
		for ( i = 0 ; i < imagePanels.length ; i ++ )
			imagePanels [ i ] = new WithBackgroundImagePanel () ;
		selectors = new JRadioButton [ imagePanels.length ] ;
		for ( i = 0 ; i < imagePanels.length ; i ++ )
			selectors [ i ] = new JRadioButton () ;
		buttonGroup = new ButtonGroup () ;
	}

	/***/
	@Override
	protected void manageLayout () 
	{
		GameMoveType [] moveTypes ;
		Insets insets ;
		moveTypes = GameMoveType.values();
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		int i ;
		for ( i = 0 ; i < imagePanels.length ; i ++ )
		{
			layoutComponent ( imagePanels [ i ] , i , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( selectors [ i ] , i , 2 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			selectors [ i ].setText ( moveTypes [ i ].name () ) ;
			selectors [ i ].setHorizontalAlignment ( SwingConstants.CENTER ) ;
		}
	}

	/***/
	@Override
	protected void bindListeners () 
	{
		GameMoveType [] moveTypes ;
		int i ;
		moveTypes = GameMoveType.values () ;
		for ( i = 0 ; i < selectors.length ; i ++ )
			selectors [ i ].addItemListener ( new SelectorListener ( moveTypes [ i ] ) ) ;
	}

	@Override
	protected void injectComponents () 
	{
		for ( JPanel p : imagePanels )
			add ( p ) ;
		for ( JRadioButton r : selectors )
			add ( r ) ;
		for ( JRadioButton r : selectors )
			buttonGroup.add ( r ) ;
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
