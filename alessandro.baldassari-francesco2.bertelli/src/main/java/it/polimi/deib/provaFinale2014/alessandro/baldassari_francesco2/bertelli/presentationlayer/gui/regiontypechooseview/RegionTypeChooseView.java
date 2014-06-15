package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.regiontypechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class RegionTypeChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < RegionTypeChooseViewObserver >
{

	/***/
	private InputView inputView ;
	
	/***/
	private RegionTypeViewPanel regionTypeViewPanel ;
	
	/***/
	private int moneyLimit ;
	
	/**
	 * @param observer 
	 * @param moneyLimit 
	 * @throw {@link IllegalArgumentException}
	 */
	public RegionTypeChooseView ( DefaultRegionTypeChooseViewObserver observer , int moneyLimit ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		if ( moneyLimit > 0 )
		{
			this.moneyLimit = moneyLimit ;
			addObserver ( observer ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		inputView = new InputView () ;
		regionTypeViewPanel = new RegionTypeViewPanel () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( inputView , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;	
		inputView.setShowKo(false); 
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
		inputView.setOkAction ( new Couple < Boolean , Runnable > ( false , new OkAction () ) ) ; 
		inputView.setKoAction ( new Couple < Boolean , Runnable > ( false , new KoAction () ) ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		inputView.setContentsPanel ( regionTypeViewPanel ) ;
		add ( inputView ) ;
	}
	
	/***/
	public void setData ( Iterable < Couple < RegionType , Integer > > inData )
	{
		regionTypeViewPanel.setData(inData); 
	}
	
	/**
	 * This class manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{
	
		private static final String NAME_OF_THE_METHOD_TO_CALL = "onRegionTypeChoosed" ;
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			Couple < RegionType , Integer > res ;
			res = regionTypeViewPanel.getSelectedElem () ;
			if ( res != null )
			{
				if ( res.getSecondObject () <= moneyLimit )
					try 
					{
						notifyObservers ( NAME_OF_THE_METHOD_TO_CALL , res.getFirstObject () , res.getSecondObject () );
					}
					catch (MethodInvocationException e) 
					{
						e.printStackTrace();
					}
				else
					inputView.setErrors ( "Troppi pochi soldi, non te lo puoi permettere..." );
			}
			else
				inputView.setErrors ( PresentationMessages.INVALID_CHOOSE_MESSAGE ) ;
		}
		
	}
	
	/**
	 * This class manages the ko action. 
	 */
	private class KoAction implements Runnable 
	{
	
		private static final String NAME_OF_THE_METHOD_TO_CHOOSE = "onDoNotWantToChooseAnyRegion" ;
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			try 
			{
				notifyObservers ( NAME_OF_THE_METHOD_TO_CHOOSE );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/***/
	public static Couple < RegionType , Integer > showDialog ( Iterable < Couple < RegionType , Integer > > inData , int moneyLimit ) 
	{
		RegionTypeChooseView regionTypeChooseView ;
		AtomicReference < Couple < RegionType , Integer > > move ;
		move = new AtomicReference < Couple < RegionType , Integer > > ( null ) ;
		regionTypeChooseView = new RegionTypeChooseView ( new DefaultRegionTypeChooseViewObserver ( move ) , moneyLimit ) ;
		regionTypeChooseView.setData(inData); 
		GraphicsUtilities.showUnshowWindow ( regionTypeChooseView , false , true ) ;
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
		GraphicsUtilities.showUnshowWindow ( regionTypeChooseView , false , false ) ;
		return move.get() ;
	}
	
	public static void main ( String [] args ) 
	{
		ArrayList < Couple < RegionType , Integer > > in ;
		Couple < RegionType , Integer > x ;
		in = new ArrayList < Couple < RegionType , Integer > > () ;
		in.add ( new Couple <> ( RegionType.CULTIVABLE , 0 ) ) ;
		in.add ( new Couple <> ( RegionType.FOREST , 3 ) ) ;
		x = RegionTypeChooseView.showDialog ( in , 1 )  ;
		System.out.println ( x ) ;
	}
	
}

/***/
class RegionTypeViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/***/
	private Couple < RegionType , Integer > selected ;
	
	/***/
	private List < JPanel > imagePanels ;

	/***/
	private List < JRadioButton > selectors ;
		
	/***/
	private ButtonGroup buttonGroup ;	
	
	/***/
	public RegionTypeViewPanel () 
	{
		super () ;
	}
	

	@Override
	protected void createComponents () 
	{
		selected = null ;
		imagePanels = new ArrayList < JPanel > () ;
		selectors = new ArrayList < JRadioButton > () ;
		buttonGroup = new ButtonGroup () ;
	}

	@Override
	protected void manageLayout () {}

	@Override
	protected void bindListeners () {}

	@Override
	protected void injectComponents () {}
	
	public void setData ( Iterable < Couple < RegionType , Integer > > inData )
	{
		Insets insets ;
		JPanel panel ;
		JRadioButton r ;
		int i ;
		insets = new Insets(5,5,5,5) ;
		i = 0 ;
		for ( Couple < RegionType , Integer > c : inData )
		{
			panel = new JPanel () ;
			r = new JRadioButton ( c.getFirstObject() + " [ " + c.getSecondObject() + " danari ]" ) ;
			imagePanels.add(panel);
			selectors.add(r);
			r.addItemListener ( new SelectorItemListener ( c ) );
			layoutComponent ( panel , i , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets) ;
			layoutComponent ( r , i , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets) ;
			r.setHorizontalAlignment ( SwingConstants.CENTER ) ;
			add ( panel ) ;
			add ( r ) ;
			i ++ ;
		}
		repaint () ;
	}
	
	/***/
	public Couple < RegionType , Integer > getSelectedElem () 
	{
		return selected ;
	}
	
	private class SelectorItemListener implements ItemListener 
	{
		
		private Couple < RegionType , Integer > myElem ;
		
		public SelectorItemListener ( Couple < RegionType , Integer > myElem ) 
		{
			this.myElem = myElem ;
		}

		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			selected = myElem ;
		}
		
		
		
	}
	
}