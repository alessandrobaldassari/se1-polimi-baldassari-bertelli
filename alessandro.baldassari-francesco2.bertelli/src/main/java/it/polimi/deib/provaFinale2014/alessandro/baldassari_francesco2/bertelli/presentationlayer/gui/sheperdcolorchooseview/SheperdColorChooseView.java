package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.WithBackgroundImagePanel;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * This class allows the User to select one color for one of his Sheperds. 
 */
public class SheperdColorChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < SheperdColorChooseViewObserver >
{
	
	/**
	 * An input view to contains the data. 
	 */
	private InputView view ;
	
	/**
	 * The effective data-content panel. 
	 */
	private ColorsListPanel colorsPanel ;
	
	/**
	 * @param observer an Observer for this View. 
	 */
	public SheperdColorChooseView ( SheperdColorChooseViewObserver observer ) 
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
		colorsPanel = new ColorsListPanel () ;		
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
		view.setTitle ( PresentationMessages.CHOOSE_COLOR_FOR_SHEPERD_MESSAGE ) ;
		view.setShowKo(false); 
		setDefaultCloseOperation ( DO_NOTHING_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;
		setUndecorated(false); 
		setSize ( GraphicsUtilities.getThreeFourthVgaResolution() ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
		view.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCoverImage(true) ) ; 
		setResizable(false); 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		view.setOkAction ( new Couple < Boolean , Runnable > ( false , new OkAction () ) ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( view ) ;
		view.setContentsPanel ( colorsPanel ) ;
	}

	/**
	 * Set the colors among which the User can choose.
	 * 
	 * @param colors the list of colors the User can choose in.
	 */
	public void setModel ( SheperColorChooserViewModel m ) 
	{
		colorsPanel.setModel(m); 
	}
	
	/**
	 * Class that manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{
		
		/***/
		private static final String NAME_OF_THE_METHOD_TO_CALL = "onColorChoosed" ;

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			NamedColor res ;
			res = colorsPanel.getSelectedColor();
			if ( res != null )
				try 
				{
					notifyObservers ( NAME_OF_THE_METHOD_TO_CALL , res );
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			else
				view.setErrors ( "Devi prima selezionare un colore!" );
		}
				
	}
	
	/**
	 * Class that manages the ko action  
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
				notifyObservers ( "onDoNotWantChooseColor" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}	
		}		
		
	}

	/***/
	public static NamedColor showDialog ( Iterable < NamedColor > inColors ) 
	{
		SheperdColorChooseView sheperdColorChooseView ;
		AtomicReference < NamedColor > color ;
		color = new AtomicReference < NamedColor > ( null ) ;
		sheperdColorChooseView = new SheperdColorChooseView ( new DefaultSheperdColorChooseViewObserver ( color ) ) ;
		sheperdColorChooseView.setModel ( new SheperColorChooserViewModel ( inColors ) ) ;
		GraphicsUtilities.showUnshowWindow ( sheperdColorChooseView , false , true ) ;
		synchronized ( color ) 
		{
			while ( color.get() == null )
				try 
				{
					color.wait () ;
				}
				catch (InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		GraphicsUtilities.showUnshowWindow ( sheperdColorChooseView , false , false ) ;
		return color.get() ;
	}
	
	public static void main ( String [] args ) 
	{
		ArrayList < NamedColor > a = new ArrayList < NamedColor > () ;
		a.add(new NamedColor ( 0 , 0 , 0 , "BLACK" ) ) ;
		a.add ( new NamedColor ( 255 , 0 , 0 , "RED" ) ) ;
		a.add ( new NamedColor ( 0 , 255 , 0 , "GREEN") ) ;
		NamedColor n ;
		n = SheperdColorChooseView.showDialog( a );
		System.out.println ( n ) ;
	}
	
}

/**
 * The Panel that effectively contains colors. 
 */
class ColorsListPanel extends FrameworkedWithGridBagLayoutPanel 
{
		
	/***/
	private SheperColorChooserViewModel model ;
	
	/***/
	private Map < String , WithBackgroundImagePanel > imagePanels ;
	
	/**
	 * Needed to ensure just one color is selected. 
	 */
	private ButtonGroup buttonGroup ;
	
	/***/
	public ColorsListPanel ( ) 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		imagePanels = new HashMap<String, WithBackgroundImagePanel>();
		buttonGroup = new ButtonGroup () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		setOpaque ( false ) ;
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
	protected void injectComponents () {}

	public void setModel (  SheperColorChooserViewModel model ) 
	{
		this.model = model ;
		setColors () ;
		repaint () ;
	}
	
	/**
	 * Return the selected color in of this View. 
	 */
	public NamedColor getSelectedColor () 
	{
		return model.getSelectedColor () ;
	}
	
	/**
	 * Fill this component with data, allowing it to effectively show something useful to the User.
	 * 
	 * @param in the colors the User can choose betweeen.
	 */
	public void setColors () 
	{
		WithBackgroundImagePanel p ;
		JRadioButton r ;
		Insets insets ;
		NamedColor n ;
		int i ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		for ( Component c : getComponents() )
			remove ( c ) ;
		for ( i = 0 ; i < model.getNumberOfColors () ; i ++ )
		{
			n = model.getColor ( i ) ;
			p = new WithBackgroundImagePanel () ;
			r = new JRadioButton () ;
			p.setOpaque ( false ) ;
			r.setOpaque(false); 
			layoutComponent ( p , i , 1 , 1 , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( r , i , 2 , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			p.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getPositionableImage ( PositionableElementType.getSheperdByColor ( n.getName () ) , true , true ) ) ;
			r.addItemListener ( new SelectorListener ( i , p ) ) ;
			r.setText ( n.getName () ) ; 
			r.setHorizontalAlignment ( SwingConstants.CENTER ) ;
			add ( p ) ;
			add ( r ) ;
			imagePanels.put ( n.getName() , p ) ;
			buttonGroup.add ( r ) ;
		}
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		private WithBackgroundImagePanel p ;
		
		/***/
		private int index ;
		
		/***/
		public SelectorListener ( int index ,WithBackgroundImagePanel p )
		{
			this.index = index ;
			this.p = p ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			if ( model.getSelectedColor() != null )
				imagePanels.get ( model.getSelectedColor().getName() ).
				setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().
				getPositionableImage ( PositionableElementType.
				getSheperdByColor ( model.getSelectedColor().getName() ) , true , true ) );
			model.setSelected ( index ) ;
			p.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().
			getPositionableImage ( PositionableElementType.getSheperdByColor 
			( model.getColor ( index ).getName () ) , false , true )) ;
			repaint () ;
		}	
		
	}
	
}

