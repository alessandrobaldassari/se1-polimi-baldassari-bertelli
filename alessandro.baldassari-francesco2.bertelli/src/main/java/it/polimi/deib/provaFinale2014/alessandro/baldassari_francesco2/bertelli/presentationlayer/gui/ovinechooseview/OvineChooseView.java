package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.ovinechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.WithBackgroundImagePanel;

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

public class OvineChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < OvineChooseViewObserver >
{


	/***/
	private InputView view ;
	
	/***/
	private OvineListPanel moveListPanel ;
	
	/**
	 * @param observer an Observer for this MoveChooseView to notity of the events.
	 */
	public OvineChooseView ( OvineChooseViewObserver observer ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		addObserver ( observer ) ;
	}
	
	public void obscureOvines ( Iterable < PositionableElementType > m ) 
	{
		moveListPanel.obscureOvines( m ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		view = new InputView () ;
		moveListPanel = new OvineListPanel () ;
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
		view.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCoverImage(true)) ; 
		view.setTitle ( "Seleziona il tipo di ovino : " );
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
			PositionableElementType res ;
			res = moveListPanel.getSelectedMove () ;
			if ( res != null )
				try 
				{
					notifyObservers ( "onOvineTypeChoosed" , res );
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
				notifyObservers ( "onDoNotWantToChooseAnOvineType" );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/***/
	public static PositionableElementType showDialog ( Iterable < PositionableElementType > toNotShowTypes ) 
	{
		OvineChooseView ovineChooseView ;
		AtomicReference < PositionableElementType > move ;
		move = new AtomicReference < PositionableElementType > ( null ) ;
		ovineChooseView = new OvineChooseView ( new DefaultOvineChooseViewObserver(move) ) ;
		ovineChooseView.obscureOvines ( toNotShowTypes );
		GraphicsUtilities.showUnshowWindow ( ovineChooseView , false , true ) ;
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
		GraphicsUtilities.showUnshowWindow ( ovineChooseView , false , false ) ;
		return move.get() ;
	}
	
}

/***/
class OvineListPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	/***/
	private PositionableElementType selected ;
	
	/***/
	private Map < PositionableElementType , WithBackgroundImagePanel > imagePanels ;

	/***/
	private Map < PositionableElementType , JRadioButton > selectors ;
		
	/***/
	private ButtonGroup buttonGroup ;	
	
	/***/
	public OvineListPanel () 
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
		imagePanels = new HashMap < PositionableElementType , WithBackgroundImagePanel > ( 3 ) ;
		imagePanels.put ( PositionableElementType.RAM , new WithBackgroundImagePanel () ) ;
		imagePanels.put ( PositionableElementType.SHEEP , new WithBackgroundImagePanel () ) ;
		imagePanels.put ( PositionableElementType.LAMB , new WithBackgroundImagePanel () ) ;
		selectors = new HashMap < PositionableElementType , JRadioButton > ( 3 ) ;
		selectors.put ( PositionableElementType.RAM , new JRadioButton () ) ;
		selectors.put ( PositionableElementType.SHEEP , new JRadioButton () ) ;
		selectors.put ( PositionableElementType.LAMB , new JRadioButton () ) ;		
		buttonGroup = new ButtonGroup () ;
	}

	/***/
	@Override
	protected void manageLayout () 
	{
		WithBackgroundImagePanel panel ;
		JRadioButton r ;
		Insets insets ;
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		int i ;
		setOpaque ( false ) ;
		i = 0 ;
		for ( PositionableElementType p : imagePanels.keySet() )
		{
			panel = imagePanels.get( p );
			r = selectors.get ( p );
			layoutComponent ( panel , i , 1 , 1 , 1 , 1 , 1 , 5 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( r , i , 2 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			r.setText ( p.name () ) ;
			r.setOpaque ( false ) ;
			r.setHorizontalAlignment ( SwingConstants.CENTER ) ;
			panel.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( p , false ) );
			panel.setOpaque ( false ) ;
			i ++ ;
		}
	}

	/***/
	@Override
	protected void bindListeners () 
	{
		for ( PositionableElementType p : selectors.keySet() )
			selectors.get ( p ).addItemListener ( new SelectorListener ( p ) ) ;
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

	public void obscureOvines ( Iterable < PositionableElementType > toNotShow ) 
	{
		for ( PositionableElementType p : toNotShow )
		{
			imagePanels.get ( p ).setEnabled(false);
			selectors.get ( p ).setEnabled(false); 
		}
	}
	
	/**
	 * Returns the move selected in this View. 
	 */
	public PositionableElementType getSelectedMove () 
	{
		return selected ;
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		/***/
		private PositionableElementType gameMove ;
		
		/***/
		public SelectorListener ( PositionableElementType gameMove )
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
